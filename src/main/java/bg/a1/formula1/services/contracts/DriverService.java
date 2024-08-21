package bg.a1.formula1.services.contracts;

import bg.a1.formula1.models.entity.Driver;

import java.util.List;

public interface DriverService {

    List<Driver> findAll();

    Driver findById(long id);

    Driver create(Driver driver);

    void transferDriverToTeam(long driverId, long teamId);

    void retireDriver(long id);
}