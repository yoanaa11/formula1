package bg.a1.formula1.services;

import bg.a1.formula1.exceptions.FullCapacityException;
import bg.a1.formula1.exceptions.EntityNotFoundException;
import bg.a1.formula1.models.entity.Driver;
import bg.a1.formula1.models.entity.Team;
import bg.a1.formula1.repositories.DriverRepository;
import bg.a1.formula1.services.contracts.DriverService;
import bg.a1.formula1.services.contracts.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final TeamService teamService;

    @Override
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    @Override
    public Driver findById(long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Driver", String.valueOf(id)));
    }

    @Override
    public Driver create(Driver driver) {
        Team team = teamService.findByName(driver.getTeam().getName());

        if (team.getDrivers().size() >= 4) {
            throw new FullCapacityException(team.getName());
        }

        team.getDrivers().add(driver);
        driver.setTeam(team);
        return driverRepository.save(driver);
    }

    @Override
    public void transferDriverToTeam(long driverId, long teamId) {
        Driver driver = findById(driverId);
        Team newTeam = teamService.findById(teamId);

        if (newTeam.getDrivers().size() >= 4) {
            throw new FullCapacityException(newTeam.getName());
        }

        driver.setTeam(newTeam);
        driverRepository.save(driver);
    }

    @Override
    public void retireDriver(long id) {
        driverRepository.deleteById(id);
    }
}