package bg.a1.formula1.services;

import bg.a1.formula1.exceptions.EntityNotFoundException;
import bg.a1.formula1.fixtures.RaceFixture;
import bg.a1.formula1.models.entity.Race;
import bg.a1.formula1.repositories.RaceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.ENTITY_NOT_FOUND_EXCEPTION;
import static org.mockito.Mockito.*;

public class RaceServiceImplTest {

    @Mock
    private RaceRepository raceRepository;

    @InjectMocks
    private RaceServiceImpl raceService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findById_shouldReturnRace() {
        Race race = RaceFixture.getRace();

        when(raceRepository.findById(1L)).thenReturn(Optional.of(race));

        Race foundRace = raceService.findById(1L);

        Assertions.assertNotNull(foundRace);
        Assertions.assertEquals("Race1", foundRace.getName());
        verify(raceRepository, times(1)).findById(1L);
    }

    @Test
    public void findById_shouldReturnNotFound() {
        when(raceRepository.findById(1L)).thenReturn(Optional.empty());

        Exception entityNotFoundException = Assertions.assertThrows(EntityNotFoundException.class, () ->
                raceService.findById(1L));

        String expectedMessage = String.format(ENTITY_NOT_FOUND_EXCEPTION, "Race", RaceFixture.getRace().getId());
        String actualMessage = entityNotFoundException.getMessage();

        Assertions.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void findByName_shouldReturnRace() {
        Race race = RaceFixture.getRace();

        when(raceRepository.findByName("Race1")).thenReturn(Optional.of(race));

        Race foundRace = raceService.findByName("Race1");

        Assertions.assertNotNull(foundRace);
        Assertions.assertEquals("Race1", foundRace.getName());
        verify(raceRepository, times(1)).findByName("Race1");
    }

    @Test
    public void create_shouldCreateRace() {
        Race race = RaceFixture.getRace();

        when(raceRepository.save(race)).thenReturn(race);

        Race createdRace = raceService.create(race);

        Assertions.assertNotNull(createdRace);
        Assertions.assertEquals("Race1", createdRace.getName());
        verify(raceRepository, times(1)).save(race);
    }
}