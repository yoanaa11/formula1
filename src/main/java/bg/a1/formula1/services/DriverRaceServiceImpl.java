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

        boolean hasParticipated = race.getDriverRaces().stream()
                .anyMatch(driverRaces -> driverRaces.getDriver().equals(driver));

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
        if (race.isHasFinished()) {
            throw new AlreadyFinishedException();
        }

        List<DriverRaces> filteredAndValidatedResults = getFilteredAndValidatedList(requestedDriverRaces, race);
        if (filteredAndValidatedResults.isEmpty()) {
            throw new EmptyRaceException(race.getName());
        }

        List<DriverRaces> existingResults = race.getDriverRaces();
        saveResults(filteredAndValidatedResults, existingResults, race);
        race.setHasFinished(true);
    }

    private List<DriverRaces> getFilteredAndValidatedList(List<DriverRaces> requestedDriverRaces, Race race) {
        List<Long> registeredDriversIds = race.getDriverRaces().stream()
                .map(driverRace -> driverRace.getDriver().getId())
                .toList();

        isValidRequest(requestedDriverRaces, registeredDriversIds);

        return filteredList(requestedDriverRaces, race);
    }

    private static void isValidRequest(List<DriverRaces> requestedDriverRaces, List<Long> registeredDriversIds) {
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

    private List<DriverRaces> filteredList(List<DriverRaces> requestedDriverRaces, Race race) {
        return requestedDriverRaces.stream()
                .peek(driverRace -> {
                    long driverId = driverRace.getDriver().getId();

                    driverRace.setRace(race);

                    Driver driver = driverService.findById(driverId);
                    driverRace.setDriver(driver);
                })
                .sorted(Comparator.comparingDouble(DriverRaces::getFinishedForInSeconds))
                .toList();
    }

    private void saveResults(List<DriverRaces> filteredAndValidatedResults, List<DriverRaces> existingResults, Race race) {
        for (DriverRaces currentResult : filteredAndValidatedResults) {
            boolean isUpdated = false;

            isUpdated = isUpdatedSuccessfully(existingResults, currentResult, isUpdated);

            if (!isUpdated) {
                DriverRaces newResult = new DriverRaces();
                newResult.setDriver(currentResult.getDriver());
                newResult.setRace(race);
                newResult.setFinishedForInSeconds(currentResult.getFinishedForInSeconds());
                int positionPoints = MAX_POINTS - filteredAndValidatedResults.indexOf(currentResult);
                newResult.setPoints(positionPoints);

                driverRaceRepository.save(newResult);
            }
        }
    }

    private boolean isUpdatedSuccessfully(List<DriverRaces> existingResults, DriverRaces currentResult, boolean isUpdated) {
        for (DriverRaces existingResult : existingResults) {
            long existingResultDriverId = existingResult.getDriver().getId();
            long currentResultDriverId = currentResult.getDriver().getId();

            long existingResultRaceId = existingResult.getRace().getId();
            long currentResultRaceId = currentResult.getRace().getId();

            if ((existingResultDriverId == currentResultDriverId) && (existingResultRaceId == currentResultRaceId)) {
                if (existingResult.getPoints() == ZERO_POINTS) {
                    existingResult.setFinishedForInSeconds(currentResult.getFinishedForInSeconds());
                    int positionPoints = MAX_POINTS - existingResults.indexOf(existingResult);
                    existingResult.setPoints(positionPoints);

                    driverRaceRepository.save(existingResult);

                    isUpdated = true;
                }
            }
        }

        return isUpdated;
    }
}