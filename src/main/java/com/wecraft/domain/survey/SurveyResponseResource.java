package com.wecraft.domain.survey;

import com.wecraft.domain.message.Message;
import com.wecraft.domain.surveyquestion.SurveyQuestion;
import com.wecraft.domain.surveysection.SurveySection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResponseResource {

  private Survey survey;
  private List<SurveySection> surveySectionList;
  private List<SurveyQuestion> surveyQuestionList;
  private List<Message> messageList;

}
