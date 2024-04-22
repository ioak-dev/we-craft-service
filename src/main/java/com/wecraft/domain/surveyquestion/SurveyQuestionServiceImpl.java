package com.wecraft.domain.surveyquestion;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SurveyQuestionServiceImpl implements SurveyQuestionService{

  @Autowired
  private SurveyQuestionRepository surveyQuestionRepository;

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
}
