package bg.a1.formula1.services;

import bg.a1.formula1.models.dtos.response.DriverStandingDto;
import bg.a1.formula1.models.dtos.response.TeamStandingDto;
import bg.a1.formula1.models.entity.DriverRaces;
import bg.a1.formula1.services.contracts.DriverRaceService;
import bg.a1.formula1.services.contracts.DriverService;
import bg.a1.formula1.services.contracts.StandingService;
import bg.a1.formula1.services.contracts.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StandingServiceImpl implements StandingService {

    private final DriverService driverService;
    private final TeamService teamService;
    private final DriverRaceService driverRaceService;

    @Override
    public List<DriverStandingDto> getDriverStandings() {
        return getDrivingStandingsDto();
    }

    @Override
    public List<TeamStandingDto> getTeamStandings() {
        return getTeamStandingDto();
    }

    private List<DriverStandingDto> getDrivingStandingsDto() {
        return driverService.findAll().stream()
                .map(driver -> {
                    int totalPoints = driverRaceService.findByDriverId(driver.getId()).stream()
                            .mapToInt(DriverRaces::getPoints)
                            .sum();

                    String teamName = driver.getTeam() != null ? driver.getTeam().getName() : "No team";

                    return new DriverStandingDto(driver.getFirstName() + " " + driver.getLastName(), teamName, totalPoints);
                })
                .sorted((a, b) -> Integer.compare(b.getTotalPoints(), a.getTotalPoints()))
                .toList();
    }

    private List<TeamStandingDto> getTeamStandingDto() {
        return teamService.findAll().stream()
                .map(team -> {
                    int totalPoints = team.getDrivers().stream()
                            .flatMap(driver -> driverRaceService.findByDriverId(driver.getId()).stream())
                            .mapToInt(DriverRaces::getPoints)
                            .sum();

                    return new TeamStandingDto(team.getName(), totalPoints);
                })
                .sorted((a, b) -> Integer.compare(b.getTotalPoints(), a.getTotalPoints()))
                .toList();
    }
}