package bg.a1.formula1.services;

import bg.a1.formula1.exceptions.EntityNotFoundException;
import bg.a1.formula1.fixtures.TeamFixture;
import bg.a1.formula1.models.entity.Team;
import bg.a1.formula1.repositories.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static bg.a1.formula1.exceptions.utils.ExceptionMessages.ENTITY_NOT_FOUND_EXCEPTION;
import static bg.a1.formula1.exceptions.utils.ExceptionMessages.ENTITY_WITH_NAME_NOT_FOUND_EXCEPTION;
import static org.mockito.Mockito.*;

public class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findById_shouldReturnTeam() {
        Team team = TeamFixture.getTeam();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Team foundTeam = teamService.findById(1L);

        Assertions.assertNotNull(foundTeam);
        Assertions.assertEquals("RedBull", foundTeam.getName());
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    public void findById_shouldReturnNotFound() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        Exception entityNotFoundException = Assertions.assertThrows(EntityNotFoundException.class, () ->
                teamService.findById(1L));

        String expectedMessage = String.format(ENTITY_NOT_FOUND_EXCEPTION, "Team", TeamFixture.getTeam().getId());
        String actualMessage = entityNotFoundException.getMessage();

        Assertions.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void findByName_shouldReturnTeam() {
        Team team = TeamFixture.getTeam();

        when(teamRepository.findByName("RedBull")).thenReturn(Optional.of(team));

        Team foundTeam = teamService.findByName("RedBull");

        Assertions.assertNotNull(foundTeam);
        Assertions.assertEquals("RedBull", foundTeam.getName());
        verify(teamRepository, times(1)).findByName("RedBull");
    }

    @Test
    public void findByName_shouldReturnNotFound() {
        when(teamRepository.findByName("RedBull")).thenReturn(Optional.empty());

        Exception entityNotFoundException = Assertions.assertThrows(EntityNotFoundException.class, () ->
                teamService.findByName("RedBll"));

        String expectedMessage = String.format(ENTITY_WITH_NAME_NOT_FOUND_EXCEPTION, "Team");
        String actualMessage = entityNotFoundException.getMessage();

        Assertions.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void create_shouldCreateTeam() {
        Team team = TeamFixture.getTeam();

        when(teamRepository.save(team)).thenReturn(team);

        Team createdTeam = teamService.create(team);

        Assertions.assertNotNull(createdTeam);
        Assertions.assertEquals("RedBull", createdTeam.getName());
        verify(teamRepository, times(1)).save(team);
    }
}