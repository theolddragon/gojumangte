package kr.gojumangte.management.exception;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class CustomException extends RuntimeException {

  private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

  private String code;
  private Object[] args;

  /**
   * Constructor.
   *
   * @param status 응답 상태
   * @param code Exception Message
   */
  public CustomException(HttpStatus status, String code, @Nullable Object[] args) {
    this.status = status;
    this.code = code;
    this.args = args;
  }

  public abstract List<String> getErrors();
}