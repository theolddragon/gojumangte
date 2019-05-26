package kr.gojumangte.management.common.mapper.response;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class ApiErrorResource extends Resource<ApiError> {
  public ApiErrorResource(ApiError content, Link... links) {
    super(content, links);
  }
}
