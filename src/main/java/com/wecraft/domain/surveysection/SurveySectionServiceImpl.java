package com.wecraft.domain.surveysection;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SurveySectionServiceImpl implements SurveySectionService{

  @Autowired
  private SurveySectionRepository surveySectionRepository;

  @Override
  public List<SurveySection> getAllSurveySections() {
    return surveySectionRepository.findAll();
  }

  @Override
  public SurveySection create(SurveySection surveySection) {
    return surveySectionRepository.save(surveySection);
  }

  @Override
  public SurveySection update(SurveySection request, String id) {
    if (id != null) {
      SurveySection surveySection = surveySectionRepository.findById(id).orElseThrow(
          () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SurveySection Not found"));
      surveySection.setSurveyId(request.getSurveyId());
      surveySection.setSectionHeader(request.getSectionHeader());
      surveySection.setSectionSubTitle(request.getSectionSubTitle());
      return surveySectionRepository.save(surveySection);
    }
    throw new IllegalArgumentException("SurveySection id cannot be null for update");
  }

  @Override
  public SurveySection getById(String id) {
    return surveySectionRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SurveySection Not found"));
  }

  @Override
  public void delete(String id) {
    surveySectionRepository.deleteById(id);
  }

}
