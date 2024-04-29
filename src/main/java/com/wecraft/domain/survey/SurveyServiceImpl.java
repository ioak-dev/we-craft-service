package com.wecraft.domain.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wecraft.domain.message.MessageRepository;
import com.wecraft.domain.survey.SurveyResponseResource.SurveyResource;
import com.wecraft.domain.survey.SurveyResponseResource.SurveySectionResource;
import com.wecraft.domain.surveyquestion.QuestionType;
import com.wecraft.domain.surveyquestion.SurveyQuestion;
import com.wecraft.domain.surveyquestion.SurveyQuestionRepository;
import com.wecraft.domain.surveysection.SurveySection;
import com.wecraft.domain.surveysection.SurveySectionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SurveyServiceImpl implements SurveyService {

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private SurveySectionRepository surveySectionRepository;

  @Autowired
  private SurveyQuestionRepository surveyQuestionRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Override
  public List<Survey> getAllSurveys() {
    return surveyRepository.findAll();
  }

  @Override
  public Survey create(Survey survey) {
    survey.setStatus(Status.New);
    return surveyRepository.save(survey);
  }

  @Override
  public Survey update(Survey request, String id) {
    if (id != null) {
      Survey survey = surveyRepository.findById(id).orElseThrow(
          () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey Not found"));
      survey.setSurveyHeader(request.getSurveyHeader());
      survey.setSurveySubTitle(request.getSurveySubTitle());
      survey.setStatus(request.getStatus());
      return surveyRepository.save(survey);
    }
    throw new IllegalArgumentException("Survey id cannot be null for update");
  }

  @Override
  public Survey getById(String id) {
    return surveyRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey Not found"));
  }

  @Override
  public void delete(String id) {
    surveyRepository.deleteById(id);
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class SurveyUploadResponse {

    private String surveyId;
  }

  @Override
  public ResponseEntity<?> uploadSurveyFile(MultipartFile file) {
    String surveyId = "";
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      objectMapper.findAndRegisterModules();
      SurveyRequestResource resource = objectMapper
          .readValue(file.getInputStream(), SurveyRequestResource.class);
      Survey survey = Survey.builder().surveyHeader(resource.getSurveyHeader())
          .surveySubTitle(resource.getSurveyHeaderSubtitle()).build();
      Survey savedSurvey = surveyRepository.save(survey);
      surveyId = savedSurvey.getId();
      if (!resource.getSections().isEmpty()) {
        resource.getSections().forEach(sectionResource -> {
          SurveySection section = SurveySection.builder().surveyId(savedSurvey.getId())
              .sectionHeader(sectionResource.getSectionHeader())
              .sectionSubTitle(sectionResource.getSectionHeaderSubtitle()).build();
          surveySectionRepository.save(section);
          sectionResource.getQuestions().forEach(questionResource -> {
            SurveyQuestion question = SurveyQuestion.builder().surveyId(savedSurvey.getId()).sectionId(section.getId())
                .questionHeader(
                    questionResource.getQuestion()).questionSubTitle(
                    questionResource.getQuestionSubtitle())
                .questionType(QuestionType.valueOf(questionResource.getType()))
                .aiRelevant(questionResource.isAiRelevant()).options(questionResource.getOptions())
                .questionResponse(questionResource.getResponse()).questionResponseList(questionResource.getResponseList()).build();
            surveyQuestionRepository.save(question);
          });
        });
      }
    } catch (Exception exception) {
      exception.printStackTrace();
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
    return ResponseEntity.ok(SurveyUploadResponse.builder().surveyId(surveyId).build());
  }

  @Override
  public ResponseEntity<SurveyResponseResource> getSurveyDetailsFromId(String surveyId) {
    Survey survey = surveyRepository.findById(surveyId).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SurveyId does not exist"));
    List<SurveySectionResource> sectionResourceList = new ArrayList<>();
    SurveyResource.SurveyResourceBuilder builder = SurveyResource.builder();
    builder.id(surveyId).surveyHeader(survey.getSurveyHeader()).surveySubTitle(survey.getSurveySubTitle()).status(survey.getStatus());
    List<SurveySection> surveySectionList = surveySectionRepository.findAllBySurveyId(surveyId);
    surveySectionList.forEach(surveySection -> {
      SurveySectionResource surveySectionResource = new SurveySectionResource();
      surveySectionResource.setId(surveySection.getId());
      surveySectionResource.setSurveyId(surveyId);
      surveySectionResource.setSectionHeader(surveySection.getSectionHeader());
      surveySectionResource.setSectionSubTitle(surveySection.getSectionSubTitle());
      List<SurveyQuestion> questionList = surveyQuestionRepository.findAllBySectionId(surveySection.getId());
      surveySectionResource.setQuestionList(questionList);
      sectionResourceList.add(surveySectionResource);
    });
    SurveyResource surveyResource = builder.surveySectionList(sectionResourceList).build();
    SurveyResponseResource surveyResponseResource = SurveyResponseResource.builder().survey(surveyResource).build();
    return ResponseEntity.ok(surveyResponseResource);
  }
}
