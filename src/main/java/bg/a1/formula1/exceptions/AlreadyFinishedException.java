package bg.a1.formula1.exceptions;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.ALREADY_FINISHED_EXCEPTION;

public class AlreadyFinishedException extends RuntimeException {

    public AlreadyFinishedException() {
        super(ALREADY_FINISHED_EXCEPTION);
    }
}