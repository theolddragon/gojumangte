package kr.gojumangte.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 잘못된 요청에 대한 Exception.
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public abstract class NotAcceptableException extends CustomException {

  private HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
}
