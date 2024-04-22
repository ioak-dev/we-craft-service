package com.wecraft.domain.surveyquestion;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveyQuestionRepository extends MongoRepository<SurveyQuestion, String> {

  List<SurveyQuestion> findAllBySurveyId(String surveyId);
}
