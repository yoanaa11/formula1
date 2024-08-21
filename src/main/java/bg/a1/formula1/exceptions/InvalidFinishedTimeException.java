package bg.a1.formula1.exceptions;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.INVALID_FINISHED_TIME_EXCEPTION;

public class InvalidFinishedTimeException extends RuntimeException {

    public InvalidFinishedTimeException() {
        super(INVALID_FINISHED_TIME_EXCEPTION);
    }
}