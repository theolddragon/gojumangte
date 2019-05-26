package kr.gojumangte.management.common.mapper.response;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiError {

  private HttpStatus status;
  private String message;
  private List<String> errors;

  public ApiError(HttpStatus status, String message, String error) {
    this.status = status;
    this.message = message;
    this.errors = Collections.singletonList(error);
  }
}
