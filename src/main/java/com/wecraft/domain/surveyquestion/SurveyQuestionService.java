package com.wecraft.domain.surveyquestion;

import com.wecraft.domain.message.Message;
import com.wecraft.domain.surveyquestion.SurveyQuestionServiceImpl.SurveyQuestionMessageResource;
import java.util.List;

public interface SurveyQuestionService {

  List<SurveyQuestion> getAllSurveyQuestion();

  SurveyQuestion create(SurveyQuestion surveyQuestion);

  SurveyQuestionMessageResource update(SurveyQuestion request, String id);

  SurveyQuestion getById(String id);

  void delete(String id);

  List<Message> initiateGptCall(SurveyQuestion surveyQuestion, boolean surveyQuestionResponse);

}
