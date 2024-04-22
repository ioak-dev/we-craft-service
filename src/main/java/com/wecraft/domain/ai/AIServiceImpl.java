package com.wecraft.domain.ai;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService{

  @Autowired
  private ResourceLoader resourceLoader;

  @Override
  public List<AIResponse> getAIResponse(String jobDescription) {
    List<AIResponse> aiResponseList = new ArrayList<>();
    try {
      Resource resource = resourceLoader.getResource("classpath:Chat_Gpt_Response.json");
      BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
      JsonElement jsonElement = JsonParser.parseReader(reader);
      JsonArray jsonArray = jsonElement.getAsJsonArray();

      for (JsonElement element : jsonArray) {
        JsonObject jsonObject = element.getAsJsonObject();
        AIResponse aiResponse = new AIResponse();
        aiResponse.setQuestion(jsonObject.get("question").getAsString());
        List<String> choices = new ArrayList<>();
        JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
        for (JsonElement choiceElement : choicesArray) {
          choices.add(choiceElement.getAsString());
        }
        aiResponse.setChoices(choices);
        aiResponse.setAnswer(jsonObject.get("answer").getAsString());
        aiResponseList.add(aiResponse);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return aiResponseList;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class GptResponseResource {

    private GptResponse gptResponse;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class GptResponse {

    private List<AIResponse> aiResponseList;
  }
}
