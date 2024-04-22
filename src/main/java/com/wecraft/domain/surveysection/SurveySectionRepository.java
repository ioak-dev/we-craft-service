package com.wecraft.domain.surveysection;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveySectionRepository extends MongoRepository<SurveySection, String> {

  SurveySection findBySurveyId(String surveyId);

  List<SurveySection> findAllBySurveyId(String surveyId);

}
