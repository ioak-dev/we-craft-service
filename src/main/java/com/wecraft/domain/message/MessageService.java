package com.wecraft.domain.message;

import java.util.List;

public interface MessageService {

  List<Message> getAllSurveyMessages();

  Message create(Message message);

  Message update(Message request, String id);

  Message getById(String id);

  void delete(String id);

}
