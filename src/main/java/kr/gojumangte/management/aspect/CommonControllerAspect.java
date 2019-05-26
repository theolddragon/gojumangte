package kr.gojumangte.management.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

@Slf4j
@Component
@Aspect
public class CommonControllerAspect {

  private final ObjectMapper objectMapper;

  public CommonControllerAspect(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * In logging.
   *
   * @param proceedingJoinPoint the join point
   */
  @Around("execution(* kr.gojumangte.management.*.controller.*.*Controller.*(..))")
  public Object aroundLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

    // before advice
    StopWatch sw = new StopWatch();

    // 어떤 클래스의 메서드인지 출력하는 정보는 pjp 객체에 있다.
    String methodName = proceedingJoinPoint.getSignature().getName();

    log.info(
        "################ {} START ###########################",
        methodName
    );

    inLogging();

    sw.start();
    Object result = proceedingJoinPoint.proceed();
    sw.stop();

    outLogging(result);

    // 실행시간은 로그로 남기는 것이 좋지만, 여기서는 콘솔창에 찍도록 한다.
    log.info("###### ExecutionTime : " + sw.getTotalTimeMillis() + "(ms)");
    log.info(
        "################ {} FINISH ##########################",
        methodName
    );

    return result;
  }

  /**
   * response logging.
   * @param result result
   */
  private void outLogging(Object result) {
    if (result != null) {
      try {
        log.info("###### return  : {}", objectMapper.writeValueAsString(result));
      } catch (JsonProcessingException jsonProcessingException) {
        log.error("JsonProcessingException error={}", jsonProcessingException.getMessage());
      }
    } else {
      log.info("###### return  : null");
    }
  }

  /**
   * request logging.
   */
  private void inLogging() {

    httpServletRequestLogging(
        Objects.requireNonNull(
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes()
        ).getRequest()
    );
  }


  /**
   * http servlet request logging.
   *
   * @param request http servlet request
   */
  private static void httpServletRequestLogging(HttpServletRequest request) {

    StringBuilder url = new StringBuilder();
    url.append(request.getRequestURL());
    String queryString = request.getQueryString();
    if (!StringUtils.isEmpty(queryString)) {
      url.append('?').append(queryString);
    }

    log.info("###### URL     : {}", url);
    log.info("###### Method  : {}", request.getMethod());

    String client = request.getRemoteAddr();
    if (!StringUtils.isEmpty(client)) {
      log.info("###### Client  : {}", client);
    }
    HttpSession session = request.getSession(false);
    if (session != null) {
      log.info("###### Session : {}", session.getId());
    }
    String user = request.getRemoteUser();
    if (!StringUtils.isEmpty(user)) {
      log.info("###### User    : {}", user);
    }

    log.info("###### Headers : {}", new ServletServerHttpRequest(request).getHeaders());
    log.info("###### Body    : {}", getMessagePayload(request));
  }

  /**
   * get request message payload.
   *
   * @param request request
   * @return message payload
   */
  private static String getMessagePayload(HttpServletRequest request) {

    ContentCachingRequestWrapper wrapper =
        WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    if (wrapper != null) {
      byte[] buf = wrapper.getContentAsByteArray();
      try {
        return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
      } catch (UnsupportedEncodingException ex) {
        return "[unknown]";
      }
    }
    return null;
  }
}
