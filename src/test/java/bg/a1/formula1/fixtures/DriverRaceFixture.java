package bg.a1.formula1.fixtures;

import bg.a1.formula1.models.entity.DriverRaces;

public class DriverRaceFixture {

    public static DriverRaces getDriverRaces() {
        DriverRaces driverRaces = new DriverRaces();
        driverRaces.setId(1L);
        driverRaces.setDriver(DriverFixture.getDriver());
        driverRaces.setRace(RaceFixture.getRace());
        driverRaces.setPoints(19);
        driverRaces.setFinishedForInSeconds(10);

        return driverRaces;
    }
}