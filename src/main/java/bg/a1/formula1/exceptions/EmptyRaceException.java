package bg.a1.formula1.exceptions;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.EMPTY_RACE_EXCEPTION;

public class EmptyRaceException extends RuntimeException {

    public EmptyRaceException(String value) {
        super(String.format(EMPTY_RACE_EXCEPTION, value));
    }
}