package bg.a1.formula1.exceptions;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.DUPLICATE_PARTICIPATION_EXCEPTION;

public class DuplicateParticipationException extends RuntimeException {

    public DuplicateParticipationException(String type, String value) {
        super(String.format(DUPLICATE_PARTICIPATION_EXCEPTION, type, value));
    }
}