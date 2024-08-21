package bg.a1.formula1.fixtures;

import bg.a1.formula1.models.entity.Driver;

import java.util.ArrayList;

public class DriverFixture {

    private static final String FIRST_NAME = "Max";
    private static final String LAST_NAME = "Verstappen";

    public static Driver getDriver() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setFirstName(FIRST_NAME);
        driver.setLastName(LAST_NAME);
        driver.setTeam(TeamFixture.getTeam());
        driver.setDriverRaces(new ArrayList<>());

        return driver;
    }
}