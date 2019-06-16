package kr.gojumangte.management.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import kr.gojumangte.management.common.mapper.response.ApiError;
import kr.gojumangte.management.common.mapper.response.ApiErrorResource;
import kr.gojumangte.management.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  @ExceptionHandler({ CustomException.class })
  public ResponseEntity customExceptionHandler(CustomException ex, WebRequest request) {
    logException(ex);
    ApiError apiError = new ApiError(
        ex.getStatus(),
        messageSource.getMessage(ex.getCode(), ex.getArgs(), request.getLocale()),
        ex.getErrors()
    );
    return ResponseEntity
        .status(apiError.getStatus())
        .headers(new HttpHeaders())
        .body(new ApiErrorResource(apiError));
  }

  @ExceptionHandler({ Exception.class })
  public ResponseEntity handleAll(Exception ex, WebRequest request) {
    logException(ex);
    ApiError apiError = new ApiError(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getLocalizedMessage(),
        "error occurred"
    );
    return ResponseEntity
        .status(apiError.getStatus())
        .headers(new HttpHeaders())
        .body(new ApiErrorResource(apiError));
  }


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    logException(ex);
    List<String> errors = new ArrayList<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
    ex.getBindingResult().getGlobalErrors()
        .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

    ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), errors);
    return handleExceptionInternal(
        ex, apiError, headers, apiError.getStatus(), request
    );
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    logException(ex);
    ApiError apiError = new ApiError(
        HttpStatus.BAD_REQUEST,
        ex.getLocalizedMessage(),
        ex.getParameterName() + " parameter is missing"
    );
    return ResponseEntity
        .status(apiError.getStatus())
        .headers(new HttpHeaders())
        .body(new ApiErrorResource(apiError));
  }

  @ExceptionHandler({ ConstraintViolationException.class })
  public ResponseEntity handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    logException(ex);
    List<String> errors = ex.getConstraintViolations().stream()
        .map(violation -> violation.getRootBeanClass().getName() + " " +
            violation.getPropertyPath() + ": " + violation.getMessage())
        .collect(Collectors.toList());

    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
    return ResponseEntity
        .status(apiError.getStatus())
        .headers(new HttpHeaders())
        .body(new ApiErrorResource(apiError));
  }

  @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
  public ResponseEntity handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    logException(ex);
    ApiError apiError = new ApiError(
        HttpStatus.BAD_REQUEST,
        ex.getLocalizedMessage(),
        ex.getName() + " should be of type " + ex.getRequiredType().getName()
    );
    return ResponseEntity
        .status(apiError.getStatus())
        .headers(new HttpHeaders())
        .body(new ApiErrorResource(apiError));
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    logException(ex);
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(" method is not supported for this request. Supported methods are ");
    Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));

    ApiError apiError = new ApiError(
        HttpStatus.METHOD_NOT_ALLOWED,
        ex.getLocalizedMessage(),
        builder.toString()
    );
    return ResponseEntity
        .status(apiError.getStatus())
        .headers(new HttpHeaders())
        .body(new ApiErrorResource(apiError));
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

    logException(ex);
    ApiError apiError = new ApiError(
        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        ex.getLocalizedMessage(),
        builder.substring(0, builder.length() - 2)
    );
    return ResponseEntity
        .status(apiError.getStatus())
        .headers(new HttpHeaders())
        .body(new ApiErrorResource(apiError));
  }

  private void logException(Exception exception) {
    log.warn("exception message={}", exception.getMessage());
    Arrays.stream(exception.getStackTrace()).forEach(stackTrace -> log.debug("{}", stackTrace));
  }
}
