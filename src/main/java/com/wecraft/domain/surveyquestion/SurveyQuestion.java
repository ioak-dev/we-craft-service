package com.wecraft.domain.surveyquestion;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "survey.question")
public class SurveyQuestion {

  @Id
  private String id;
  private String surveyId;
  private String sectionId;
  private String questionHeader;
  private String questionSubTitle;
  private boolean aiRelevant;
  private QuestionType questionType;
  private List<String> options;
  private String questionResponse;
  private List<String> questionResponseList;

  @CreatedBy
  private String createdBy;

  @CreatedDate
  private ZonedDateTime createdDate;

  @LastModifiedBy
  private String lastModifiedBy;

  @LastModifiedDate
  private ZonedDateTime lastModifiedDate;

}
