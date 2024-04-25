package com.wecraft.domain.survey;

import com.wecraft.domain.message.Sender;
import com.wecraft.domain.surveyquestion.SurveyQuestion;
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

  private SurveyResource survey;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class MessageResource {

    private String id;
    private String questionId;
    private Sender sender;
    private String message;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class SurveySectionResource {

    private String id;
    private String surveyId;
    private String sectionHeader;
    private String sectionSubTitle;
    private List<SurveyQuestion> questionList;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class SurveyResource {

    private String id;
    private String surveyHeader;
    private String surveySubTitle;
    private Status status;
    private List<SurveySectionResource> surveySectionList;
  }

}
