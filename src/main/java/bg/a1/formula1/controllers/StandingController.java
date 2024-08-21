package bg.a1.formula1.controllers;

import bg.a1.formula1.models.dtos.response.DriverStandingDto;
import bg.a1.formula1.models.dtos.response.TeamStandingDto;
import bg.a1.formula1.services.contracts.StandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/standing")
public class StandingController {

    private final StandingService standingService;

    @GetMapping
    public List<DriverStandingDto> getDriverStandings() {
        return standingService.getDriverStandings();
    }

    @GetMapping("/teams")
    public List<TeamStandingDto> getTeamStandings() {
        return standingService.getTeamStandings();
    }
}