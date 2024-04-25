package com.wecraft.domain.surveyquestion;

import com.wecraft.domain.ai.AIResource;
import com.wecraft.domain.ai.AIService;
import com.wecraft.domain.message.Message;
import com.wecraft.domain.message.MessageRepository;
import com.wecraft.domain.message.Sender;
import com.wecraft.domain.survey.Survey;
import com.wecraft.domain.survey.SurveyRepository;
import com.wecraft.domain.surveysection.SurveySection;
import com.wecraft.domain.surveysection.SurveySectionRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SurveyQuestionServiceImpl implements SurveyQuestionService{

  @Autowired
  private SurveyQuestionRepository surveyQuestionRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private SurveySectionRepository surveySectionRepository;

  @Autowired
  private AIService aiService;

  @Override
  public List<SurveyQuestion> getAllSurveyQuestion() {
    return surveyQuestionRepository.findAll();
  }

  @Override
  public SurveyQuestion create(SurveyQuestion surveyQuestion) {
    return surveyQuestionRepository.save(surveyQuestion);
  }

  @Override
  public SurveyQuestion update(SurveyQuestion request, String id) {
    if (id != null) {
      SurveyQuestion surveyQuestion = surveyQuestionRepository.findById(id).orElseThrow(
          () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SurveyQuestion Not found"));
      surveyQuestion.setQuestionHeader(request.getQuestionHeader());
      surveyQuestion.setQuestionSubTitle(request.getQuestionSubTitle());
      return surveyQuestionRepository.save(surveyQuestion);
    }
    throw new IllegalArgumentException("SurveyQuestion id cannot be null for update");
  }

  @Override
  public SurveyQuestion getById(String id) {
    return surveyQuestionRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SurveyQuestion Not found"));
  }

  @Override
  public void delete(String id) {
    surveyQuestionRepository.deleteById(id);
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class SurveyQuestionMessageResource {

    private SurveyQuestion surveyQuestion;
    private List<Message> messageList;
  }

  private List<Message> initiateGptCall(SurveyQuestion surveyQuestion){
    List<Message> existingMessageList = messageRepository.findAllByQuestionId(surveyQuestion.getId());
    Survey survey = surveyRepository.findById(surveyQuestion.getSurveyId()).orElse(null);
    SurveySection surveySection = surveySectionRepository.findById(surveyQuestion.getSectionId()).orElse(null);
    AIResource aiResource = aiService.getAIResponse(surveyQuestion, existingMessageList, survey, surveySection);
    if(aiResource != null){
      Message message = Message.builder().questionId(surveyQuestion.getId()).message(
          aiResource.getContent()).sender(Sender.valueOf(aiResource.getRole())).build();
      messageRepository.save(message);
    }
    List<Message> savedMessageList = messageRepository.findAllByQuestionId(surveyQuestion.getId());
    return savedMessageList;
  }
}
