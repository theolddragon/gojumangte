package kr.gojumangte.management.common.controller;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexRestController {

  @GetMapping("/api")
  public ResourceSupport index() {

    ResourceSupport index = new ResourceSupport();
    // index.add(linkTo(IndexRestController.class).withRel("events"));
    return index;
    }

}
