package bg.a1.formula1.exceptions.utils;

public class ExceptionMessages {

    public static final String FULL_CAPACITY_EXCEPTION = "There are more than 4 drivers in team %s.";

    public static final String DUPLICATE_PARTICIPATION_EXCEPTION = "%s with id %s has already participated in this race.";

    public static final String EMPTY_RACE_EXCEPTION = "No drivers in race: %s";

    public static final String ENTITY_NOT_FOUND_EXCEPTION = "%s with id: %s not found.";

    public static final String ENTITY_WITH_NAME_NOT_FOUND_EXCEPTION = "%s with this name not found.";

    public static final String ALREADY_FINISHED_EXCEPTION = "Race has already finished.";

    public static final String INVALID_FINISHED_TIME_EXCEPTION = "Everybody should have finished time.";

    public static final String INVALID_REQUEST_EXCEPTION = "The number of drivers in the request does not match the number of drivers in the race.";
}