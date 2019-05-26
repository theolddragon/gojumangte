package kr.gojumangte.management.config;

import java.nio.charset.Charset;
import java.util.List;
import javax.servlet.MultipartConfigElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Component
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(byteArrayHttpMessageConverter());
    converters.add(new StringHttpMessageConverter());
    converters.add(new SourceHttpMessageConverter<>());
    converters.add(formHttpMessageConverter());
  }

  @Bean
  public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
    return new ByteArrayHttpMessageConverter();
  }

  @Bean
  public FormHttpMessageConverter formHttpMessageConverter() {
    return new FormHttpMessageConverter();
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.setUseTrailingSlashMatch(false);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
        .maxAge(3600);
  }

  @Bean
  public HttpMessageConverter<String> responseBodyConverter() {
    return new StringHttpMessageConverter(Charset.forName("UTF-8"));
  }

  /**
   * multipartConfigElement.
   * @return MultipartConfigElement
   */
  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();

    factory.setMaxFileSize(DataSize.ofMegabytes(10));
    factory.setMaxRequestSize(DataSize.ofMegabytes(40));

    return factory.createMultipartConfig();
  }

  /**
   * multipartFilter.
   * @return MultipartFilter
   */
  @Bean
  @Order(0)
  public MultipartFilter multipartFilter() {
    MultipartFilter multipartFilter = new MultipartFilter();
    multipartFilter.setMultipartResolverBeanName("multipartResolver");
    return multipartFilter;
  }
}
