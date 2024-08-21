package bg.a1.formula1.services;

import bg.a1.formula1.fixtures.DriverFixture;
import bg.a1.formula1.models.entity.Driver;
import bg.a1.formula1.models.entity.DriverRaces;
import bg.a1.formula1.repositories.DriverRaceRepository;
import bg.a1.formula1.services.contracts.DriverService;
import bg.a1.formula1.services.contracts.RaceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        DriverRaces driverRaces = new DriverRaces();
        driverRaces.setDriver(new Driver());

        when(driverRaceRepository.findByDriverId(DriverFixture.getDriver().getId())).thenReturn(List.of(driverRaces));

        List<DriverRaces> result = driverRaceService.findByDriverId(DriverFixture.getDriver().getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(driverRaceRepository, times(1)).findByDriverId(DriverFixture.getDriver().getId());
    }
}