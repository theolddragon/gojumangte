package kr.gojumangte.management.exception;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CustomException extends RuntimeException {

  private HttpStatus httpStatus;

  private String code;
  private Object[] args;

  /**
   * Constructor.
   *
   * @param httpStatus 응답 상태
   * @param code Exception Message
   */
  public CustomException(HttpStatus httpStatus, String code, @Nullable Object[] args) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.args = args;
  }

  public abstract List<String> getErrors();
}