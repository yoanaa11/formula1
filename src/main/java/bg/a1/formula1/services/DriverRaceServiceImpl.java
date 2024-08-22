package bg.a1.formula1.services;

import bg.a1.formula1.exceptions.*;
import bg.a1.formula1.models.entity.Driver;
import bg.a1.formula1.models.entity.DriverRaces;
import bg.a1.formula1.models.entity.Race;
import bg.a1.formula1.repositories.DriverRaceRepository;
import bg.a1.formula1.services.contracts.DriverRaceService;
import bg.a1.formula1.services.contracts.DriverService;
import bg.a1.formula1.services.contracts.RaceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DriverRaceServiceImpl implements DriverRaceService {

    public static final int ZERO_FINISHED_TIME = 0;
    public static final int MAX_POINTS = 19;
    public static final int ZERO_POINTS = 0;

    private final DriverRaceRepository driverRaceRepository;
    private final DriverService driverService;
    private final RaceService raceService;

    @Override
    public List<DriverRaces> findByDriverId(long id) {
        return driverRaceRepository.findByDriverId(id);
    }

    @Override
    public DriverRaces registerDriverToARace(long driverId, long raceId) {
        Driver driver = driverService.findById(driverId);
        Race race = raceService.findById(raceId);

        if (race.isHasFinished()) {
            throw new AlreadyFinishedException();
        }

        boolean hasParticipated = !driverRaceRepository.findByDriverIdAndRaceId(driverId, raceId).isEmpty();

        if (hasParticipated) {
            throw new DuplicateParticipationException("Driver", String.valueOf(driverId));
        }

        DriverRaces driverRaces = new DriverRaces();
        driverRaces.setDriver(driver);
        driverRaces.setRace(race);
        driverRaces.setPoints(ZERO_POINTS);

        driver.getDriverRaces().add(driverRaces);
        race.getDriverRaces().add(driverRaces);

        return driverRaceRepository.save(driverRaces);
    }

    @Override
    public void raceResults(long raceId, List<DriverRaces> requestedDriverRaces) {
        Race race = raceService.findById(raceId);

        List<DriverRaces> sortedAndValidatedResults = getSortedAndValidatedList(requestedDriverRaces, race);

        race.setHasFinished(true);
        assignPoints(sortedAndValidatedResults);
        driverRaceRepository.saveAll(sortedAndValidatedResults);
    }

    private List<DriverRaces> getSortedAndValidatedList(List<DriverRaces> requestedDriverRaces, Race race) {
        Map<Long, DriverRaces> registeredDriversIds = race.getDriverRaces().stream()
                .collect(Collectors.toMap(driverRace -> driverRace.getDriver().getId(), driverRace -> driverRace));

        isValidRequest(requestedDriverRaces, registeredDriversIds.keySet(), race);

        return sortDriversByFinishTime(requestedDriverRaces, race, registeredDriversIds);
    }

    private static void isValidRequest(List<DriverRaces> requestedDriverRaces, Set<Long> registeredDriversIds, Race race) {
        if (race.isHasFinished()) {
            throw new AlreadyFinishedException();
        }

        if (race.getDriverRaces().isEmpty()) {
            throw new EmptyRaceException(race.getName());
        }

        List<Long> driverRacesIds = requestedDriverRaces.stream()
                .peek(driver -> {
                    if (driver.getFinishedForInSeconds() <= ZERO_FINISHED_TIME) {
                        throw new InvalidFinishedTimeException();
                    }
                })
                .map(driverRace -> driverRace.getDriver().getId())
                .toList();

        if (!CollectionUtils.isEqualCollection(driverRacesIds, registeredDriversIds)) {
            throw new InvalidRequestException();
        }
    }

    private List<DriverRaces> sortDriversByFinishTime(List<DriverRaces> requestedDriverRaces, Race race,
                                                      Map<Long, DriverRaces> registeredDriversIds) {
        return requestedDriverRaces.stream()
                .peek(driverRace -> {
                    long driverId = driverRace.getDriver().getId();

                    driverRace.setId(registeredDriversIds.get(driverId).getId());

                    driverRace.setRace(race);

                    Driver driver = driverService.findById(driverId);
                    driverRace.setDriver(driver);
                })
                .sorted(Comparator.comparingDouble(DriverRaces::getFinishedForInSeconds))
                .toList();
    }

    private void assignPoints(List<DriverRaces> existingResults) {
        for (DriverRaces existingResult : existingResults) {
            int positionPoints = MAX_POINTS - existingResults.indexOf(existingResult);
            existingResult.setPoints(positionPoints);
        }
    }
}