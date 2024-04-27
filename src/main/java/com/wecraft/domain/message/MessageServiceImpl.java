package com.wecraft.domain.message;

import com.wecraft.domain.surveyquestion.SurveyQuestion;
import com.wecraft.domain.surveyquestion.SurveyQuestionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MessageServiceImpl implements MessageService{

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private SurveyQuestionService surveyQuestionService;

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
      message.setContent(request.getContent());
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

  @Override
  public List<Message> messageToGpt(Message message){
    messageRepository.save(message);
    SurveyQuestion surveyQuestion = surveyQuestionService.getById(message.getQuestionId());
    List<Message> messageList = surveyQuestionService.initiateGptCall(surveyQuestion);
    return messageList;
  }

}
