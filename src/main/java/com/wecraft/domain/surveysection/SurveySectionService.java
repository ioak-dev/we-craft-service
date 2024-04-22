package com.wecraft.domain.surveysection;

import java.util.List;

public interface SurveySectionService {

  List<SurveySection> getAllSurveySections();

  SurveySection create(SurveySection surveySection);

  SurveySection update(SurveySection request, String id);

  SurveySection getById(String id);

  void delete(String id);

}
