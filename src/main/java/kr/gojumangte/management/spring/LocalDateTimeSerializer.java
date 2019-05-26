package kr.gojumangte.management.spring;

import static java.time.format.DateTimeFormatter.ofPattern;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

  private static final DateTimeFormatter FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public void serialize(
      LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeString(value.atZone(ZoneId.systemDefault()).format(FORMATTER));
  }
}
