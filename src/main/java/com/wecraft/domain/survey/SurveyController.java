package com.wecraft.domain.survey;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

  @Autowired
  private SurveyService surveyService;

  @GetMapping("/{id}")
  public ResponseEntity<Survey> getById(@PathVariable String id) {
    return ResponseEntity.ok(surveyService.getById(id));
  }

  @GetMapping
  public ResponseEntity<List<Survey>> getAll() {
    return ResponseEntity.ok(surveyService.getAllSurveys());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Survey> update(@PathVariable String id,
      @RequestBody Survey request) {
    return ResponseEntity.ok(surveyService.update(request, id));
  }

  @PostMapping
  public ResponseEntity<Survey> create(@RequestBody Survey request) {
    return ResponseEntity.ok(surveyService.create(request));
  }

  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable String id) {
    surveyService.delete(id);
  }

  @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public ResponseEntity<?> u0loadSurveyFile(@RequestParam("file") MultipartFile file) {
    return surveyService.uploadSurveyFile(file);
  }

  @GetMapping("/details/{id}")
  public ResponseEntity<SurveyResponseResource> getSurveyResponseForId(@PathVariable String id) {
    return surveyService.getSurveyDetailsFromId(id);
  }
}
