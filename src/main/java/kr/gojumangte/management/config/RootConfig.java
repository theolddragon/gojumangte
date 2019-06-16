package kr.gojumangte.management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Validator;
import kr.gojumangte.management.spring.LocalDateTimeDeserializer;
import kr.gojumangte.management.spring.LocalDateTimeSerializer;
import kr.gojumangte.management.spring.interceptor.LoggingRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RootConfig {

  /**
   * rest template.
   * @return rest template
   */
  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate(
        new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())
    );
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add(new LoggingRequestInterceptor());
    restTemplate.setInterceptors(interceptors);
    return restTemplate;
  }

  /**
   * Request 로그 설정.
   *
   * @return CommonsRequestLoggingFilter
   */
  @Bean
  public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
    CommonsRequestLoggingFilter commonsRequestLoggingFilter = new CommonsRequestLoggingFilter();
    commonsRequestLoggingFilter.setIncludeClientInfo(true);
    commonsRequestLoggingFilter.setIncludeHeaders(true);
    commonsRequestLoggingFilter.setIncludePayload(true);
    commonsRequestLoggingFilter.setIncludeQueryString(true);
    commonsRequestLoggingFilter.setMaxPayloadLength(1000);

    return commonsRequestLoggingFilter;
  }

  @Bean
  public Validator validator() {
    return new LocalValidatorFactoryBean();
  }


  /**
   * methodValidationPostProcessor.
   *
   * @return MethodValidationPostProcessor
   */
  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    MethodValidationPostProcessor methodPostProcessor = new MethodValidationPostProcessor();
    methodPostProcessor.setValidator(validator());

    return methodPostProcessor;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

    objectMapper.registerModule(javaTimeModule);

    return objectMapper;
  }
}
