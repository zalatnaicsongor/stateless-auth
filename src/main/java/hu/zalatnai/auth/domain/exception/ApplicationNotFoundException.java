package hu.zalatnai.auth.domain.exception;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException() {
        super();
    }

    public ApplicationNotFoundException(RuntimeException e) {
        super(e);
    }
}
