package kr.gojumangte.management.spring.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    traceRequest(request, body);
    ClientHttpResponse response = execution.execute(request, body);
    traceResponse(response);
    return response;
  }

  /**
   * request logging.
   * @param request request
   * @param body body
   */
  private void traceRequest(HttpRequest request, byte[] body) {
    log.info("===========================request begin===========================================");
    log.info("URI         : {}", request.getURI());
    log.info("Method      : {}", request.getMethod());
    log.info("Headers     : {}", request.getHeaders());
    log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
    log.info("==========================request end============================================ =");
  }

  /**
   * response logging.
   * @param response response
   * @throws IOException ioException
   */
  private void traceResponse(ClientHttpResponse response) throws IOException {
    StringBuilder inputStringBuilder = new StringBuilder();
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)
    );
    String line = bufferedReader.readLine();
    while (line != null) {
      inputStringBuilder.append(line);
      inputStringBuilder.append('\n');
      line = bufferedReader.readLine();
    }
    log.info("============================response begin=========================================");
    log.info("Status code  : {}", response.getStatusCode());
    log.info("Status text  : {}", response.getStatusText());
    log.info("Headers      : {}", response.getHeaders());
    log.info("Response body: {}", inputStringBuilder.toString());
    log.info("=======================response end================================================");
  }
}
