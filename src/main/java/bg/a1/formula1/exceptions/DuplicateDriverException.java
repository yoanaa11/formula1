package bg.a1.formula1.exceptions;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.DUPLICATE_DRIVER_EXCEPTION;

public class DuplicateDriverException extends RuntimeException{

    public DuplicateDriverException() {
        super(DUPLICATE_DRIVER_EXCEPTION);
    }
}