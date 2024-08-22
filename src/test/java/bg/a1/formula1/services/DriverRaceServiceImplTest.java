package bg.a1.formula1.services;

import bg.a1.formula1.exceptions.AlreadyFinishedException;
import bg.a1.formula1.exceptions.DuplicateParticipationException;
import bg.a1.formula1.exceptions.EmptyRaceException;
import bg.a1.formula1.exceptions.InvalidRequestException;
import bg.a1.formula1.fixtures.DriverFixture;
import bg.a1.formula1.fixtures.DriverRaceFixture;
import bg.a1.formula1.fixtures.RaceFixture;
import bg.a1.formula1.models.entity.Driver;
import bg.a1.formula1.models.entity.DriverRaces;
import bg.a1.formula1.models.entity.Race;
import bg.a1.formula1.repositories.DriverRaceRepository;
import bg.a1.formula1.services.contracts.DriverService;
import bg.a1.formula1.services.contracts.RaceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class DriverRaceServiceImplTest {

    @Mock
    private DriverRaceRepository driverRaceRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private RaceService raceService;

    @InjectMocks
    private DriverRaceServiceImpl driverRaceService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findById_shouldReturnDriver() {
        Driver driver = DriverFixture.getDriver();

        DriverRaces driverRaces = new DriverRaces();
        driverRaces.setDriver(new Driver());

        when(driverRaceRepository.findByDriverId(driver.getId())).thenReturn(List.of(driverRaces));

        List<DriverRaces> result = driverRaceService.findByDriverId(driver.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(driverRaceRepository, times(1)).findByDriverId(driver.getId());
    }

    @Test
    public void registerDriverToARace_shouldReturnSuccess() {
        Driver driver = DriverFixture.getDriver();
        Race race = RaceFixture.getRace();

        when(driverService.findById(driver.getId())).thenReturn(driver);
        when(raceService.findById(race.getId())).thenReturn(race);
        when(driverRaceRepository.save(any(DriverRaces.class))).thenReturn(new DriverRaces());

        DriverRaces driverRaces = driverRaceService.registerDriverToARace(driver.getId(), race.getId());

        Assertions.assertNotNull(driverRaces);
        verify(driverService, times(1)).findById(driver.getId());
        verify(raceService, times(1)).findById(race.getId());
        verify(driverRaceRepository, times(1)).save(any(DriverRaces.class));
    }

    @Test
    public void registerDriverToARace_shouldReturnAlreadyFinishedException() {
        Driver driver = DriverFixture.getDriver();
        Race race = RaceFixture.getRace();

        DriverRaces driverRaces = new DriverRaces();
        race.setHasFinished(true);
        driverRaces.setPoints(19);
        race.setDriverRaces(List.of(driverRaces));

        when(driverService.findById(driver.getId())).thenReturn(driver);
        when(raceService.findById(race.getId())).thenReturn(race);

        Assertions.assertThrows(AlreadyFinishedException.class, () -> {
            driverRaceService.registerDriverToARace(driver.getId(), race.getId());
        });
    }

    @Test
    public void registerDriverToARace_shouldReturnDuplicateParticipationException() {
        Driver driver = DriverFixture.getDriver();
        Race race = RaceFixture.getRace();

        DriverRaces driverRaces = new DriverRaces();
        driverRaces.setDriver(driver);

        when(driverService.findById(driver.getId())).thenReturn(driver);
        when(raceService.findById(race.getId())).thenReturn(race);
        when(driverRaceRepository.findByDriverIdAndRaceId(driver.getId(), race.getId())).thenReturn(List.of(driverRaces));

        Assertions.assertThrows(DuplicateParticipationException.class, () -> {
            driverRaceService.registerDriverToARace(driver.getId(), race.getId());
        });
    }

    @Test
    public void raceResult_shouldReturnSuccess() {
        Race race = RaceFixture.getRace();

        DriverRaces driverRaces = DriverRaceFixture.getDriverRaces();

        Driver secondDriver = new Driver();
        secondDriver.setId(2L);

        DriverRaces secondDriverRaces = new DriverRaces();
        secondDriverRaces.setDriver(secondDriver);
        secondDriverRaces.setFinishedForInSeconds(5);

        race.setDriverRaces(Arrays.asList(driverRaces, secondDriverRaces));

        List<DriverRaces> requestedDriverRaces = Arrays.asList(driverRaces, secondDriverRaces);

        when(raceService.findById(1L)).thenReturn(race);
        when(driverRaceRepository.saveAll(anyList())).thenReturn(requestedDriverRaces);

        driverRaceService.raceResults(1L, requestedDriverRaces);

        Assertions.assertTrue(race.isHasFinished());
        verify(driverRaceRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void raceResult_shouldReturnAlreadyFinishedException() {
        Race race = RaceFixture.getRace();
        race.setHasFinished(true);

        when(raceService.findById(1L)).thenReturn(race);

        Assertions.assertThrows(AlreadyFinishedException.class, () -> {
            driverRaceService.raceResults(1L, new ArrayList<>());
        });

        verify(driverRaceRepository, never()).saveAll(anyList());
    }

    @Test
    public void raceResult_shouldReturnEmptyRaceException() {
        Race race = RaceFixture.getRace();

        when(raceService.findById(1L)).thenReturn(race);

        Assertions.assertThrows(EmptyRaceException.class, () -> {
            driverRaceService.raceResults(1L, new ArrayList<>());
        });

        verify(driverRaceRepository, never()).saveAll(new ArrayList<>());
    }

    @Test
    public void raceResult_shouldReturnInvalidRequestException() {
        Race race = RaceFixture.getRace();

        DriverRaces driverRaces = DriverRaceFixture.getDriverRaces();

        race.setDriverRaces(Arrays.asList(driverRaces));

        Driver secondDriver = new Driver();
        secondDriver.setId(2L);

        DriverRaces secondDriverRaces = new DriverRaces();
        secondDriverRaces.setDriver(secondDriver);
        secondDriverRaces.setFinishedForInSeconds(5);

        List<DriverRaces> requestedDriverRaces = Arrays.asList(secondDriverRaces);

        when(raceService.findById(1L)).thenReturn(race);

        Assertions.assertThrows(InvalidRequestException.class, () -> {
            driverRaceService.raceResults(1L, requestedDriverRaces);
        });

        verify(driverRaceRepository, never()).saveAll(anyList());
    }
}