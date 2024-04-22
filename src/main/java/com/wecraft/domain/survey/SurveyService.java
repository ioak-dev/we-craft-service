package com.wecraft.domain.survey;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SurveyService {

  List<Survey> getAllSurveys();

  Survey create(Survey Survey);

  Survey update(Survey request, String id);

  Survey getById(String id);

  void delete(String id);

  ResponseEntity<?> uploadSurveyFile(MultipartFile multipartFile);

  ResponseEntity<SurveyResponseResource> getSurveyDetailsFromId(String surveyId);

}
