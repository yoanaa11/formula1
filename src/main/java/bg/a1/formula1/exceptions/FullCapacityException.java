package bg.a1.formula1.exceptions;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.FULL_CAPACITY_EXCEPTION;

public class FullCapacityException extends RuntimeException {

    public FullCapacityException(String value) {
        super(String.format(FULL_CAPACITY_EXCEPTION, value));
    }
}