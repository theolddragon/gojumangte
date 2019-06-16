package kr.gojumangte.management.config;

import kr.gojumangte.management.common.controller.CustomErrorController;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class CustomErrorPageRegistrar implements ErrorPageRegistrar {

  @Override
  public void registerErrorPages(ErrorPageRegistry registry) {
    registry.addErrorPages(
        new ErrorPage(HttpStatus.UNAUTHORIZED,          CustomErrorController.ERROR_401),
        new ErrorPage(HttpStatus.FORBIDDEN,             CustomErrorController.ERROR_403),
        new ErrorPage(HttpStatus.NOT_FOUND,             CustomErrorController.ERROR_404),
        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, CustomErrorController.ERROR_500),
        new ErrorPage(Throwable.class,                  CustomErrorController.ERROR_DEFAULT));
  }
}
