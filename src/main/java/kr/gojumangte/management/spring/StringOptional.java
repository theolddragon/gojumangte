package kr.gojumangte.management.spring;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.util.StringUtils;

/**
 * Custom String Optional.
 */
public class StringOptional {

  /**
   * If non-null, the value; if null, indicates no value is present.
   */
  private final String value;

  /**
   * Constructs an instance with the value present.
   *
   * @param value the non-null value to be present
   * @throws NullPointerException if value is null
   */
  private StringOptional(String value) {
    this.value = value;
  }

  /**
   * Returns an {@code Optional} with the specified present non-null value.
   *
   * @param value the value to be present, which must be non-null
   * @return an {@code Optional} with the value present
   * @throws NullPointerException if value is null
   */
  public static StringOptional of(String value) {
    return new StringOptional(value);
  }

  /**
   * If a value is present in this {@code Optional}, returns the value,
   * otherwise throws {@code NoSuchElementException}.
   *
   * @return the non-null value held by this {@code Optional}
   * @throws NoSuchElementException if there is no value present
   *
   * @see StringOptional#isPresent()
   */
  public String get() {
    if (value == null) {
      throw new NoSuchElementException("No value present");
    }
    return value;
  }

  /**
   * Return the contained value, if present, otherwise throw an exception
   * to be created by the provided supplier.
   */
  public <X extends Throwable> String orNullThrow(Supplier<? extends X> exceptionSupplier)
      throws X {

    if (value != null) {
      return value;
    } else {
      throw exceptionSupplier.get();
    }
  }

  /**
   * Return {@code true} if there is a value present, otherwise {@code false}.
   *
   * @return {@code true} if there is a value present, otherwise {@code false}
   */
  public boolean isPresent() {
    return !StringUtils.isEmpty(value);
  }

  /**
   * If a value is present, apply the provided mapping function to it.
   */
  public String map(Function<String, String> mapper) {

    Objects.requireNonNull(mapper);
    if (!isPresent()) {
      return "";
    } else {
      return mapper.apply(value);
    }
  }
}
