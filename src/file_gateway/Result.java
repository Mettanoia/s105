package file_gateway;

import java.util.function.Function;

public sealed interface Result<T> permits Result.Success, Result.Failure {


    T getOrElse(T defaultResult);

    T getOrThrow() throws Exception;

    // Static factories and subtypes

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(Exception exception) {
        return new Failure<>(exception);
    }

    final class Success<T> implements Result<T> {

        private final T value;

        private Success(T value) {
            this.value = value;
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

        private final Exception exception;


        private Failure(Exception exception) {
            this.exception = exception;
        }


        Exception getException() {
            return exception;
        }

        @Override
        public T getOrElse(T defaultResult) {
            return defaultResult;
        }

        @Override
        public T getOrThrow() throws Exception {
            throw this.exception;
        }

    }

}