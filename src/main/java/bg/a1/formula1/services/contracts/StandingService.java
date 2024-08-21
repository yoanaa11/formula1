package bg.a1.formula1.services.contracts;

import bg.a1.formula1.models.dtos.response.DriverStandingDto;
import bg.a1.formula1.models.dtos.response.TeamStandingDto;

import java.util.List;

public interface StandingService {

    List<DriverStandingDto> getDriverStandings();

    List<TeamStandingDto> getTeamStandings();
}