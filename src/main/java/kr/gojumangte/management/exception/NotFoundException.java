package kr.gojumangte.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 요청한 정보가 존재하지 않는 경우에 대한 Exception.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public abstract class NotFoundException extends CustomException {

  private HttpStatus status = HttpStatus.NOT_FOUND;
}
