import java.util.function.Function;

public sealed interface Result<T> permits Result.Success, Result.Failure {

    // Operations

    <U> Result<U> map(Function<T, U> f);

    <U> Result<U> flatMap(Function<T, Result<U>> f);

    T getOrElse(T defaultResult);

    T getOrThrow();

    // Static factories and subtypes

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(String errorMessage) {
        return new Failure<>(errorMessage);
    }

    static <T> Result<T> failure(RuntimeException runtimeException) {
        return new Failure<>(runtimeException);
    }

    final class Success<T> implements Result<T> {

        private final T value;


        private Success(T value) {
            this.value = value;
        }

        T getValue() {
            return value;
        }

        @Override
        public <U> Result<U> map(Function<T, U> f) {
            return new Success<>(f.apply(this.value));
        }

        @Override
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            return f.apply(this.value);
        }

        @Override
        public T  getOrElse(T defaultResult) {
            return this.value;
        }

        @Override
        public T getOrThrow() {
            return this.value;
        }
    }

    final class Failure<T> implements Result<T> {

        private final RuntimeException exception;


        private Failure(RuntimeException exception) {
            this.exception = exception;
        }

        private Failure(String errorMessage) {
            this.exception = new RuntimeException(errorMessage);
        }

        RuntimeException getException() {
            return exception;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Result<U> map(Function<T, U> f) {
            return (Failure<U>) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Result<U> flatMap(Function<T, Result<U>> f) {
            return (Failure<U>) this;
        }

        @Override
        public T getOrElse(T defaultResult) {
            return defaultResult;
        }

        @Override
        public T getOrThrow() {
            throw this.exception;
        }
    }

}