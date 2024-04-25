package com.wecraft.domain.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wecraft.domain.message.Message;
import com.wecraft.domain.survey.Survey;
import com.wecraft.domain.surveyquestion.SurveyQuestion;
import com.wecraft.domain.surveysection.SurveySection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIServiceImpl implements AIService{

  @Autowired
  private RestTemplate restTemplate;

  @Value("${gpt.apiKey}")
  private String gptApiKey;

  @Value("${gpt.url}")
  private String url;

  private String systemQuery = "Overall Objective: Assist user in filling in a survey. You should keep probing questions specific to the survey question. In particular look for missing details. \n"
      + "Instruction: When summing up, write as if you are the user, e.g. \"our fleet consists of\" not \"your fleet consists of\n";

  private static final String initialAssistantQuery = "How can I help you with this question?  You can ask me things such as: \n"
      + "Clarify: Rephrase my response to make it clearer. (you can also say details such as \"Clarify using bullet points\" or \"Clarify as paragraph\") \n"
      + "Probe: Ask me for more details \n"
      + "Requirements: Express my response as requirements     \n"
      + "User Story:  Express my response as a user stories and acceptance criteria      \n"
      + "Sum Up: Provide a revised response including the details from our conversation";

  private String userQuery = "My Current Response to the survey question is: ";

  @Override
  public AIResource getAIResponse(SurveyQuestion surveyQuestion,
      List<Message> messageList, Survey survey, SurveySection surveySection){
    return new AIResource();
  }

  private HttpEntity<String> createHttpEntity(SurveyQuestion surveyQuestion,
      List<Message> messageList, Survey survey, SurveySection surveySection){
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + gptApiKey);

    try{
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.findAndRegisterModules();
      ObjectNode payload = objectMapper.createObjectNode();
      payload.put("model", "gpt-4");
      payload.put("temperature", 1);
      payload.put("max_tokens", 3071);
      payload.put("top_p", 1);
      payload.put("frequency_penalty", 0);
      payload.put("presence_penalty", 0);
    } catch (Exception exception){
      exception.printStackTrace();
    }
    return null;
  }

  private List<ArrayNode> createMessagePayload(SurveyQuestion surveyQuestion,
      List<Message> messageList, Survey survey, SurveySection surveySection, ObjectMapper objectMapper){
    ArrayNode messagesArray = objectMapper.createArrayNode();
    ObjectNode message = objectMapper.createObjectNode();
    StringBuilder systemMessage = new StringBuilder();
    systemMessage.append(systemQuery);
    systemMessage.append("The survey scope is: ").append(survey.getSurveyHeader()).append("\n");
    if(survey.getSurveySubTitle()!=null && !survey.getSurveySubTitle().isEmpty())
      systemMessage.append("Subtitled: ").append(survey.getSurveySubTitle()).append("\n");
    systemMessage.append("The survey section being completed regards: ")
        .append(surveySection.getSectionHeader()).append("\n");
    if(surveySection.getSectionSubTitle()!=null && !surveySection.getSectionSubTitle().isEmpty())
      systemMessage.append("Subtitled: ").append(surveySection.getSectionSubTitle()).append("\n");
    systemMessage.append("The survey question is: ").append(surveyQuestion.getQuestionHeader())
        .append("\n");
    if(surveyQuestion.getQuestionSubTitle()!=null && !surveyQuestion.getQuestionSubTitle().isEmpty())
      systemMessage.append("Subtitled: ").append(surveyQuestion.getQuestionSubTitle()).append("\n");
    message.put("role", "system");
    message.put("content" , systemMessage.toString());
    return Collections.emptyList();
  }

}
