package com.wecraft.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
public class CustomResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    return clientHttpResponse.getStatusCode().is4xxClientError() ||
        clientHttpResponse.getStatusCode().is5xxServerError();
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    String responseAsString = toString(clientHttpResponse.getBody());
    log.error("ResponseBody: {}", responseAsString);

    throw new CustomException(responseAsString);
  }

  @Override
  public void handleError(URI url, HttpMethod method, ClientHttpResponse response)
      throws IOException {
    String responseAsString = toString(response.getBody());
    log.error("URL: {}, HttpMethod: {}, ResponseBody: {}", url, method, responseAsString);

    throw new CustomException(responseAsString);
  }

  String toString(InputStream inputStream) {
    Scanner s = new Scanner(inputStream).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

  static class CustomException extends IOException {

    public CustomException(String message) {
      super(message);
    }
  }
}
