package bg.a1.formula1.services;

import bg.a1.formula1.exceptions.FullCapacityException;
import bg.a1.formula1.exceptions.EntityNotFoundException;
import bg.a1.formula1.fixtures.DriverFixture;
import bg.a1.formula1.fixtures.TeamFixture;
import bg.a1.formula1.models.entity.Driver;
import bg.a1.formula1.models.entity.Team;
import bg.a1.formula1.repositories.DriverRepository;
import bg.a1.formula1.services.contracts.TeamService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.ENTITY_NOT_FOUND_EXCEPTION;
import static org.mockito.Mockito.*;

public class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;
    @Mock
    private TeamService teamService;

    @InjectMocks
    private DriverServiceImpl driverService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findById_shouldReturnDriver() {
        Driver driver = DriverFixture.getDriver();

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Driver foundDriver = driverService.findById(1L);

        Assertions.assertNotNull(foundDriver);
        Assertions.assertEquals("Max", foundDriver.getFirstName());
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    public void findById_shouldReturnNotFound() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        Exception entityNotFoundException = Assertions.assertThrows(EntityNotFoundException.class, () ->
                driverService.findById(1L));

        String expectedMessage = String.format(ENTITY_NOT_FOUND_EXCEPTION, "Driver", DriverFixture.getDriver().getId());
        String actualMessage = entityNotFoundException.getMessage();

        Assertions.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void create_shouldCreateDriver() {
        Team team = TeamFixture.getTeam();
        when(teamService.findByName("RedBull")).thenReturn(team);

        Driver driver = DriverFixture.getDriver();
        when(driverRepository.save(driver)).thenReturn(driver);

        Driver createdDriver = driverService.create(driver);

        Assertions.assertNotNull(createdDriver);
        Assertions.assertEquals("Max", createdDriver.getFirstName());
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    public void transferDriverToTeam_shouldTransferDriverWhenCapacityIsAvailable() {
        Driver driver = DriverFixture.getDriver();
        Team team = TeamFixture.getTeam();
        team.setDrivers(List.of(new Driver(), new Driver(), new Driver()));

        when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        when(teamService.findById(team.getId())).thenReturn(team);

        driverService.transferDriverToTeam(driver.getId(), team.getId());

        verify(driverRepository).save(driver);
        Assertions.assertEquals(team, driver.getTeam());
    }

    @Test
    public void transferDriverToTeam_shouldThrowCapacityExceptionWhenTeamIsFull() {
        Driver driver = DriverFixture.getDriver();
        Team team = TeamFixture.getTeam();
        team.setDrivers(List.of(new Driver(), new Driver(), new Driver(), new Driver(), new Driver()));

        when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        when(teamService.findById(team.getId())).thenReturn(team);

        Assertions.assertThrows(FullCapacityException.class, () ->
                driverService.transferDriverToTeam(driver.getId(), team.getId()));
        verify(driverRepository, never()).save(driver);
    }

    @Test
    public void transferDriverToTeam_shouldThrowEntityNotFoundExceptionWhenDriverNotFound() {
        when(driverRepository.findById(DriverFixture.getDriver().getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                driverService.transferDriverToTeam(DriverFixture.getDriver().getId(), TeamFixture.getTeam().getId()));
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    public void retiredDriver_shouldRetireDriver() {
        long driverId = 1L;

        doNothing().when(driverRepository).deleteById(driverId);

        driverService.retireDriver(driverId);

        verify(driverRepository, times(1)).deleteById(driverId);
    }
}