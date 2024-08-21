package bg.a1.formula1.services;

import bg.a1.formula1.exceptions.EntityNotFoundException;
import bg.a1.formula1.models.entity.Race;
import bg.a1.formula1.repositories.RaceRepository;
import bg.a1.formula1.services.contracts.RaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RaceServiceImpl implements RaceService {

    private final RaceRepository raceRepository;

    @Override
    public Race findById(long id) {
        return raceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Race", String.valueOf(id)));
    }

    @Override
    public Race findByName(String raceName) {
        return raceRepository.findByName(raceName);
    }

    @Override
    public Race create(Race race) {
        raceRepository.save(race);
        return race;
    }
}