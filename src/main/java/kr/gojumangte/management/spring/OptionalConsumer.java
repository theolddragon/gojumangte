package kr.gojumangte.management.spring;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * optional consumer.
 * @param <T> object
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OptionalConsumer<T> {

  private Optional<T> optional;

  private OptionalConsumer(Optional<T> optional) {
    this.optional = optional;
  }

  /**
   * of creator.
   * @param optional a optional of object
   * @param <T> object
   * @return a OptionalConsumer of object
   */
  public static <T> OptionalConsumer<T> of(Optional<T> optional) {
    return new OptionalConsumer<>(optional);
  }

  /**
   * if present.
   * @param consumer consumer
   * @return a OptionalConsumer of object
   */
  @SuppressWarnings("unused")
  public OptionalConsumer<T> ifPresent(Consumer<T> consumer) {
    optional.ifPresent(consumer);
    return this;
  }

  /**
   * if not present.
   * @param runnable runnable function
   * @return a OptionalConsumer of object
   */
  @SuppressWarnings("unused")
  public OptionalConsumer<T> elsePresent(Runnable runnable) {
    if (!optional.isPresent()) {
      runnable.run();
    }
    return this;
  }

  /**
   * Return the value if present, otherwise return {@code other}.
   *
   * @param other the value to be returned if there is no value present, may be null
   * @return the value, if present, otherwise {@code other}
   */
  @SuppressWarnings("unused")
  public T elseGet(T other) {
    return optional.orElseGet(() -> optional.orElse(other));
  }

  /**
   * if else mapper.
   * @param ifMapper if mapper
   * @param other other
   * @param <U> return parameter
   * @return Optional value
   */
  public <U> Optional<U> map(
      Function<? super T, ? extends U> ifMapper,
      Supplier<? extends U> other) {

    Objects.requireNonNull(ifMapper);
    if (optional.isPresent()) {
      return Optional.ofNullable(ifMapper.apply(optional.get()));
    } else {
      if (other != null) {
        return Optional.ofNullable(other.get());
      }
    }

    return Optional.empty();
  }
}
