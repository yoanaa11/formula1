package bg.a1.formula1.services;

import bg.a1.formula1.fixtures.DriverFixture;
import bg.a1.formula1.fixtures.DriverRaceFixture;
import bg.a1.formula1.fixtures.TeamFixture;
import bg.a1.formula1.models.dtos.response.DriverStandingDto;
import bg.a1.formula1.models.dtos.response.TeamStandingDto;
import bg.a1.formula1.models.entity.Driver;
import bg.a1.formula1.models.entity.DriverRaces;
import bg.a1.formula1.models.entity.Team;
import bg.a1.formula1.services.contracts.DriverRaceService;
import bg.a1.formula1.services.contracts.DriverService;
import bg.a1.formula1.services.contracts.TeamService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

public class StandingServiceImplTest {

    @Mock
    private DriverService driverService;

    @Mock
    private TeamService teamService;

    @Mock
    private DriverRaceService driverRaceService;

    @InjectMocks
    private StandingServiceImpl standingService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getDriverStandings_shouldReturnSuccess() {
        Driver driver = DriverFixture.getDriver();
        Driver secondDriver = new Driver();
        secondDriver.setId(2L);
        secondDriver.setFirstName("Lewis");
        secondDriver.setLastName("Hamilton");

        DriverRaces driverRaces = DriverRaceFixture.getDriverRaces();
        DriverRaces secondDriverRaces = new DriverRaces();
        secondDriverRaces.setPoints(15);

        when(driverService.findAll()).thenReturn(List.of(driver, secondDriver));
        when(driverRaceService.findByDriverId(1L)).thenReturn(List.of(driverRaces));
        when(driverRaceService.findByDriverId(2L)).thenReturn(List.of(secondDriverRaces));

        List<DriverStandingDto> driverStandingDtos = standingService.getDriverStandings();

        Assertions.assertNotNull(driverStandingDtos);
        Assertions.assertEquals(2, driverStandingDtos.size());

        DriverStandingDto firstPlaceDriver = driverStandingDtos.get(0);
        Assertions.assertEquals("Max Verstappen", firstPlaceDriver.getDriverName());
        Assertions.assertEquals(19, firstPlaceDriver.getTotalPoints());

        verify(driverService, times(1)).findAll();
        verify(driverRaceService, times(1)).findByDriverId(1L);
        verify(driverRaceService, times(1)).findByDriverId(2L);
    }

    @Test
    public void getTeamStandings_shouldReturnSuccess() {
        Team team = TeamFixture.getTeam();
        Team secondTeam = new Team();
        secondTeam.setName("TeamB");

        Driver driver = DriverFixture.getDriver();
        Driver secondDriver = new Driver();
        secondDriver.setId(2L);

        team.setDrivers(List.of(driver));
        secondTeam.setDrivers(List.of(secondDriver));

        DriverRaces driverRaces = DriverRaceFixture.getDriverRaces();
        DriverRaces secondDriverRaces = new DriverRaces();
        secondDriverRaces.setPoints(15);

        when(teamService.findAll()).thenReturn(List.of(team, secondTeam));
        when(driverRaceService.findByDriverId(1L)).thenReturn(List.of(driverRaces));
        when(driverRaceService.findByDriverId(2L)).thenReturn(List.of(secondDriverRaces));

        List<TeamStandingDto> teamStandingDtos = standingService.getTeamStandings();

        Assertions.assertNotNull(teamStandingDtos);
        Assertions.assertEquals(2, teamStandingDtos.size());

        TeamStandingDto firstPlaceTeam = teamStandingDtos.get(0);
        Assertions.assertEquals("RedBull", firstPlaceTeam.getTeamName());
        Assertions.assertEquals(19, firstPlaceTeam.getTotalPoints());

        verify(teamService, times(1)).findAll();
        verify(driverRaceService, times(1)).findByDriverId(1L);
        verify(driverRaceService, times(1)).findByDriverId(2L);
    }

    @Test
    public void getDriverStandings_shouldReturnNoDrivers() {
        when(driverService.findAll()).thenReturn(List.of());

        List<DriverStandingDto> driverStandingDtos = standingService.getDriverStandings();

        Assertions.assertNotNull(driverStandingDtos);
        Assertions.assertTrue(driverStandingDtos.isEmpty());

        verify(driverService, times(1)).findAll();
        verify(driverRaceService, never()).findByDriverId(anyLong());
    }

    @Test
    public void getTeamStandings_shouldReturnNoTeams() {
        when(teamService.findAll()).thenReturn(List.of());

        List<TeamStandingDto> teamStandingDtos = standingService.getTeamStandings();

        Assertions.assertNotNull(teamStandingDtos);
        Assertions.assertTrue(teamStandingDtos.isEmpty());

        verify(teamService, times(1)).findAll();
        verify(driverRaceService, never()).findByDriverId(anyLong());
    }
}