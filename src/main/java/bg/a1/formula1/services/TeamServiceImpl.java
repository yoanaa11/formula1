package bg.a1.formula1.services;

import bg.a1.formula1.exceptions.EntityNotFoundException;
import bg.a1.formula1.models.entity.Team;
import bg.a1.formula1.repositories.TeamRepository;
import bg.a1.formula1.services.contracts.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    @Override
    public Team findById(long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team", String.valueOf(id)));
    }

    @Override
    public Team findByName(String name) {
        return teamRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Team"));
    }

    @Override
    public Team create(Team team) {
        return teamRepository.save(team);
    }
}