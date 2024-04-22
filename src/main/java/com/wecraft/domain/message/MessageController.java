package com.wecraft.domain.message;

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
@RequestMapping("/api/message")
public class MessageController {

  @Autowired
  private MessageService messageService;

  @GetMapping("/{id}")
  public ResponseEntity<Message> getById(@PathVariable String id) {
    return ResponseEntity.ok(messageService.getById(id));
  }

  @GetMapping
  public ResponseEntity<List<Message>> getAll() {
    return ResponseEntity.ok(messageService.getAllSurveyMessages());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Message> update(@PathVariable String id,
      @RequestBody Message request) {
    return ResponseEntity.ok(messageService.update(request, id));
  }

  @PostMapping
  public ResponseEntity<Message> create(@RequestBody Message request) {
    return ResponseEntity.ok(messageService.create(request));
  }

  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable String id) {
    messageService.delete(id);
  }
}
