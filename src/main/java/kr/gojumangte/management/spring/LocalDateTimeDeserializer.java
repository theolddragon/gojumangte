package kr.gojumangte.management.spring;

import static java.time.format.DateTimeFormatter.ofPattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  public static final DateTimeFormatter FORMATTER = ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

  @Override
  public LocalDateTime deserialize(
      JsonParser p, DeserializationContext deserializationContext) throws IOException {

    return LocalDateTime.parse(p.getValueAsString(), FORMATTER);
  }
}
