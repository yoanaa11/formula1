package bg.a1.formula1.exceptions;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.ENTITY_NOT_FOUND_EXCEPTION;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String type, String value) {
        super(String.format(ENTITY_NOT_FOUND_EXCEPTION, type, value));
    }
}