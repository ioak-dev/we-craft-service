package com.wecraft.domain.surveyquestion;

import java.util.List;

public interface SurveyQuestionService {

  List<SurveyQuestion> getAllSurveyQuestion();

  SurveyQuestion create(SurveyQuestion surveyQuestion);

  SurveyQuestion update(SurveyQuestion request, String id);

  SurveyQuestion getById(String id);

  void delete(String id);

}
