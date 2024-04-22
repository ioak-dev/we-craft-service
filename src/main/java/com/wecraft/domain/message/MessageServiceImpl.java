package com.wecraft.domain.message;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MessageServiceImpl implements MessageService{

  @Autowired
  private MessageRepository messageRepository;

  @Override
  public List<Message> getAllSurveyMessages() {
    return messageRepository.findAll();
  }

  @Override
  public Message create(Message message) {
    return messageRepository.save(message);
  }

  @Override
  public Message update(Message request, String id) {
    if (id != null) {
      Message message = messageRepository.findById(id).orElseThrow(
          () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message Not found"));
      message.setQuestionId(request.getQuestionId());
      message.setMessage(request.getMessage());
      message.setSender(request.getSender());
      return messageRepository.save(message);
    }
    throw new IllegalArgumentException("Message id cannot be null for update");
  }

  @Override
  public Message getById(String id) {
    return messageRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message Not found"));
  }

  @Override
  public void delete(String id) {
    messageRepository.deleteById(id);
  }

}
