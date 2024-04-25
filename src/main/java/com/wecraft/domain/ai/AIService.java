package com.wecraft.domain.ai;

import com.wecraft.domain.message.Message;
import com.wecraft.domain.survey.Survey;
import com.wecraft.domain.surveyquestion.SurveyQuestion;
import com.wecraft.domain.surveysection.SurveySection;
import java.util.List;

public interface AIService {

  AIResource getAIResponse(SurveyQuestion surveyQuestion,
      List<Message> messageList, Survey survey, SurveySection surveySection);

}
