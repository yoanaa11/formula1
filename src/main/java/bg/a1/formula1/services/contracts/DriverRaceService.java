package bg.a1.formula1.services.contracts;

import bg.a1.formula1.models.entity.DriverRaces;

import java.util.List;

public interface DriverRaceService {

    List<DriverRaces> findByDriverId(long id);

    DriverRaces registerDriverToARace(long driverId, long raceId);

    void raceResults(long raceId, List<DriverRaces> driverRaces);
}