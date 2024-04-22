package com.wecraft.domain.survey;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
public class SurveyJsonResource {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class SectionResource {

    private String sectionHeader;
    private String sectionHeaderSubtitle;
    private List<QuestionResource> questions;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class QuestionResource {

    private String question;
    private String questionSubtitle;
    private String type;
    @JsonProperty(value="AIRelevant")
    private boolean aiRelevant;
    private List<String> options;
    private String response;

  }
}


