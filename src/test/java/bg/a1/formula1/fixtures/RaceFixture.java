package bg.a1.formula1.fixtures;

import bg.a1.formula1.models.entity.Race;

import java.util.ArrayList;

public class RaceFixture {

    private static final String RACE_NAME = "Race1";

    public static Race getRace() {
        Race race = new Race();
        race.setId(1L);
        race.setName(RACE_NAME);
        race.setDriverRaces(new ArrayList<>());

        return race;
    }
}