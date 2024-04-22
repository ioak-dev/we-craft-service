package com.wecraft.domain.surveysection;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/section")
public class SurveySectionController {

  @Autowired
  private SurveySectionService surveySectionService;

  @GetMapping("/{id}")
  public ResponseEntity<SurveySection> getById(@PathVariable String id) {
    return ResponseEntity.ok(surveySectionService.getById(id));
  }

  @GetMapping
  public ResponseEntity<List<SurveySection>> getAll() {
    return ResponseEntity.ok(surveySectionService.getAllSurveySections());
  }

  @PutMapping("/{id}")
  public ResponseEntity<SurveySection> update(@PathVariable String id,
      @RequestBody SurveySection request) {
    return ResponseEntity.ok(surveySectionService.update(request, id));
  }

  @PostMapping
  public ResponseEntity<SurveySection> create(@RequestBody SurveySection request) {
    return ResponseEntity.ok(surveySectionService.create(request));
  }

  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable String id) {
    surveySectionService.delete(id);
  }
}
