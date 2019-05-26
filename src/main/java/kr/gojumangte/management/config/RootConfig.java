package kr.gojumangte.management.config;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.jms.ConnectionFactory;
import javax.validation.Validator;
import kr.gojumangte.management.spring.LocalDateTimeDeserializer;
import kr.gojumangte.management.spring.LocalDateTimeSerializer;
import kr.gojumangte.management.spring.interceptor.LoggingRequestInterceptor;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
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
   * JmsListenerContainerFactory pushFactory.
   * @param connectionFactory connectionFactory
   * @param configurer configurer
   * @return JmsListenerContainerFactory
   */
  @Bean
  public JmsListenerContainerFactory<?> pushFactory(ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {

    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

    // This provides all boot's default to this factory, including the message converter
    configurer.configure(factory, connectionFactory);

    // You could still override some of Boot's default if necessary.
    return factory;
  }

  /**
   * JmsListenerContainerFactory couponStateFactory.
   * @param connectionFactory connectionFactory
   * @param configurer configurer
   * @return JmsListenerContainerFactory
   */
  @Bean
  public JmsListenerContainerFactory<?> couponStateFactory(ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {

    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

    // This provides all boot's default to this factory, including the message converter
    configurer.configure(factory, connectionFactory);

    // You could still override some of Boot's default if necessary.
    return factory;
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
