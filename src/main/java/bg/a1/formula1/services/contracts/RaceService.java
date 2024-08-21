package bg.a1.formula1.services.contracts;

import bg.a1.formula1.models.entity.Race;

public interface RaceService {

    Race findById(long id);

    Race findByName(String name);

    Race create(Race race);
}