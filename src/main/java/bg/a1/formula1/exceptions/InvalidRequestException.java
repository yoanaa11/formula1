package bg.a1.formula1.exceptions;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.INVALID_REQUEST_EXCEPTION;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException() {
        super(INVALID_REQUEST_EXCEPTION);
    }
}