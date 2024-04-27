package com.wecraft.domain.ai;

import static com.wecraft.domain.message.Sender.assistant;
import static com.wecraft.domain.message.Sender.system;
import static com.wecraft.domain.message.Sender.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wecraft.domain.message.Message;
import com.wecraft.domain.message.MessageRepository;
import com.wecraft.domain.survey.Survey;
import com.wecraft.domain.surveyquestion.SurveyQuestion;
import com.wecraft.domain.surveysection.SurveySection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AIServiceImpl implements AIService{

  @Autowired
  private RestTemplate restTemplate;

  @Value("${gpt.apiKey}")
  private String gptApiKey;

  @Value("${gpt.url}")
  private String url;

  @Autowired
  private MessageRepository messageRepository;

  private String systemQuery =
      "Overall Objective: Assist user in filling in a survey. You should keep probing questions specific to the survey question. In particular look for missing details. \n"
          + "Instruction: When summing up, write as if you are the user, e.g. \"our fleet consists of\" not \"your fleet consists of\n";

  private static final String initialAssistantQuery =
      "How can I help you with this question?  You can ask me things such as: \n"
          + "Clarify: Rephrase my response to make it clearer. (you can also say details such as \"Clarify using bullet points\" or \"Clarify as paragraph\") \n"
          + "Probe: Ask me for more details \n"
          + "Requirements: Express my response as requirements     \n"
          + "User Story:  Express my response as a user stories and acceptance criteria      \n"
          + "Sum Up: Provide a revised response including the details from our conversation";

  private String userQuery = "My Current Response to the survey question is: ";

  @Override
  public AIResource getAIResponse(SurveyQuestion surveyQuestion,
      List<Message> messageList, Survey survey, SurveySection surveySection) {
    AIResource aiResource = new AIResource();
    try {
      HttpEntity<String> entity = createHttpEntity(surveyQuestion, messageList, survey,
          surveySection);
      ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, entity, Object.class);
      aiResource = mapToAIResource(responseEntity.getBody());
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return aiResource;
  }

  private HttpEntity<String> createHttpEntity(SurveyQuestion surveyQuestion,
      List<Message> messageList, Survey survey, SurveySection surveySection) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + gptApiKey);
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode jsonPayload = objectMapper.createObjectNode();
    try {
      objectMapper.findAndRegisterModules();
      ArrayNode messageListNode = createMessagePayload(surveyQuestion, messageList, survey,
          surveySection, objectMapper);
      jsonPayload.put("model", "gpt-4");
      jsonPayload.set("messages", messageListNode);
      jsonPayload.put("temperature", 1);
      jsonPayload.put("max_tokens", 3071);
      jsonPayload.put("top_p", 1);
      jsonPayload.put("frequency_penalty", 0);
      jsonPayload.put("presence_penalty", 0);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return new HttpEntity<>(jsonPayload.toString(), headers);
  }

  private ArrayNode createMessagePayload(SurveyQuestion surveyQuestion,
      List<Message> existingMessageList, Survey survey, SurveySection surveySection,
      ObjectMapper objectMapper) {
    List<Message> messageList = new ArrayList<>();
    ArrayNode messagesArray = objectMapper.createArrayNode();
    ObjectNode systemMessageNode = objectMapper.createObjectNode();
    StringBuilder systemMessage = new StringBuilder();
    StringBuilder userMessage = new StringBuilder();

    systemMessage.append(systemQuery);
    systemMessage.append("The survey scope is: ").append(survey.getSurveyHeader()).append("\n");
    if (survey.getSurveySubTitle() != null && !survey.getSurveySubTitle().isEmpty()) {
      systemMessage.append("Subtitled: ").append(survey.getSurveySubTitle()).append("\n");
    }
    systemMessage.append("The survey section being completed regards: ")
        .append(surveySection.getSectionHeader()).append("\n");
    if (surveySection.getSectionSubTitle() != null && !surveySection.getSectionSubTitle()
        .isEmpty()) {
      systemMessage.append("Subtitled: ").append(surveySection.getSectionSubTitle()).append("\n");
    }
    systemMessage.append("The survey question is: ").append(surveyQuestion.getQuestionHeader())
        .append("\n");
    if (surveyQuestion.getQuestionSubTitle() != null && !surveyQuestion.getQuestionSubTitle()
        .isEmpty()) {
      systemMessage.append("Subtitled: ").append(surveyQuestion.getQuestionSubTitle()).append("\n");
    }
    systemMessageNode.put("role", "system");
    systemMessageNode.put("content", systemMessage.toString());
    messageList.add(Message.builder().questionId(surveyQuestion.getId()).sender(system)
        .content(systemMessage.toString()).build());
    messagesArray.add(systemMessageNode);

    ObjectNode userMessageNode = objectMapper.createObjectNode();
    userMessage.append("My Current Response to the survey question is: ").append("\n")
        .append(surveyQuestion.getQuestionResponse());
    userMessageNode.put("role", "user");
    userMessageNode.put("content", userMessage.toString());
    messageList.add(Message.builder().questionId(surveyQuestion.getId()).sender(user)
        .content(userMessage.toString()).build());
    messagesArray.add(userMessageNode);

    ObjectNode assistantMessageNode = objectMapper.createObjectNode();
    assistantMessageNode.put("role", "assistant");
    assistantMessageNode.put("content", initialAssistantQuery);
    messageList.add(Message.builder().questionId(surveyQuestion.getId()).sender(assistant)
        .content(initialAssistantQuery).build());
    messagesArray.add(assistantMessageNode);
    if(!existingMessageList.isEmpty()){
      existingMessageList.subList(0, 3).clear();
    }

    for (Message message : existingMessageList) {
      ObjectNode existingMessageNode = objectMapper.createObjectNode();
      existingMessageNode.put("role", String.valueOf(message.getSender()));
      existingMessageNode.put("content", message.getContent());
      messageList.add(Message.builder().questionId(surveyQuestion.getId()).sender(message.getSender())
          .content(message.getContent()).build());
      messagesArray.add(existingMessageNode);
    }
    messageRepository.deleteAllByQuestionId(surveyQuestion.getId());
    messageRepository.saveAll(messageList);
    return messagesArray;
  }

  private AIResource mapToAIResource(Object body) {
    ObjectMapper objectMapper = new ObjectMapper();
    AIResource aiResource = new AIResource();
    try {
      JsonNode rootNode = null;
      if (body instanceof Map) {
        rootNode = objectMapper.valueToTree(body);
      } else if (body instanceof String) {
        rootNode = objectMapper.readTree((String) body);
      }
      if (rootNode != null) {
        JsonNode choicesNode = rootNode.get("choices");
        if (choicesNode != null && choicesNode.isArray()) {
          for (JsonNode choice : choicesNode) {
            JsonNode messageNode = choice.get("message");
            if (messageNode != null) {
              aiResource = objectMapper.treeToValue(messageNode, AIResource.class);
            }
          }
        }
      } else {
        log.error("Root node is missing in the response");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return aiResource;
  }

}
