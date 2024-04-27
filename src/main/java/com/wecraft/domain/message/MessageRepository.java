package com.wecraft.domain.message;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

  List<Message> findAllByQuestionId(String questionId);

  Message findByQuestionId(String questionId);

  void deleteAllByQuestionId(String questionId);
}
