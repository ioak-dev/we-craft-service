package com.wecraft.domain.surveyquestion;


import com.wecraft.domain.message.Message;
import com.wecraft.domain.message.MessageRepository;
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
@RequestMapping("/api/question")
public class SurveyQuestionController {

  @Autowired
  private SurveyQuestionService surveyQuestionService;

  @Autowired
  private MessageRepository messageRepository;

  @GetMapping("/{id}")
  public ResponseEntity<SurveyQuestion> getById(@PathVariable String id) {
    return ResponseEntity.ok(surveyQuestionService.getById(id));
  }

  @GetMapping
  public ResponseEntity<List<SurveyQuestion>> getAll() {
    return ResponseEntity.ok(surveyQuestionService.getAllSurveyQuestion());
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable String id,
      @RequestBody SurveyQuestion request) {
    return ResponseEntity.ok(surveyQuestionService.update(request, id));
  }

  @PostMapping
  public ResponseEntity<SurveyQuestion> create(@RequestBody SurveyQuestion request) {
    return ResponseEntity.ok(surveyQuestionService.create(request));
  }

  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable String id) {
    surveyQuestionService.delete(id);
  }

  @GetMapping("message/{questionId}")
  public ResponseEntity<List<Message>> getAllMessagesByQuestionId(@PathVariable String questionId) {
    return ResponseEntity.ok(messageRepository.findAllByQuestionId(questionId));
  }
}
