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
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DriverRaceServiceImpl implements DriverRaceService {

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

        for (DriverRaces driverRace : race.getDriverRaces()) {
            if (driverRace.getPoints() != 0) {
                throw new AlreadyFinishedException();
            }
        }

        boolean hasParticipated = race.getDriverRaces().stream()
                .anyMatch(driverRaces -> driverRaces.getDriver().equals(driver));

        if (hasParticipated) {
            throw new DuplicateParticipationException("Driver", String.valueOf(driverId));
        }

        DriverRaces driverRaces = new DriverRaces();
        driverRaces.setDriver(driver);
        driverRaces.setRace(race);
        driverRaces.setPoints(0);

        driver.getDriverRaces().add(driverRaces);
        race.getDriverRaces().add(driverRaces);

        return driverRaceRepository.save(driverRaces);
    }

    @Override
    public void raceResults(long raceId, List<DriverRaces> driverRaces) {
        Race race = raceService.findById(raceId);
        List<DriverRaces> existingResults = race.getDriverRaces();

        List<DriverRaces> filteredAndValidatedResults = getFilteredAndValidatedList(driverRaces, race);

        if (filteredAndValidatedResults.isEmpty()) {
            throw new EmptyRaceException(race.getName());
        }

        results(filteredAndValidatedResults, existingResults, race);
    }

    private List<DriverRaces> getFilteredAndValidatedList(List<DriverRaces> driverRaces, Race race) {
        List<Long> registeredDriversIds = race.getDriverRaces().stream()
                .map(driverRace -> driverRace.getDriver().getId())
                .toList();

        isValidRequest(driverRaces, registeredDriversIds);

        return validList(driverRaces, race, registeredDriversIds);
    }

    private static void isValidRequest(List<DriverRaces> driverRaces, List<Long> registeredDriversIds) {
        if (driverRaces.size() != registeredDriversIds.size()) {
            throw new InvalidRequestException();
        }

        Set<Long> driverIdsInRequest = new HashSet<>();
        for (DriverRaces driverRace : driverRaces) {
            long driverId = driverRace.getDriver().getId();
            driverIdsInRequest.add(driverId);
        }

        if (driverIdsInRequest.size() != registeredDriversIds.size()) {
            throw new DuplicateDriverException();
        }
    }

    private List<DriverRaces> validList(List<DriverRaces> driverRaces, Race race, List<Long> registeredDriversIds) {
        return driverRaces.stream()
                .peek(driverRace -> {
                    long driverId = driverRace.getDriver().getId();

                    if (!registeredDriversIds.contains(driverId)) {
                        throw new EntityNotFoundException("Driver", String.valueOf(driverId));
                    }

                    race.getDriverRaces().stream()
                            .filter(existingResult -> existingResult.getDriver().getId() == driverId)
                            .findFirst()
                            .ifPresent(existingResult -> {
                                if (existingResult.getPoints() > 0) {
                                    throw new AlreadyFinishedException();
                                }
                            });

                    driverRace.setRace(race);

                    Driver driver = driverService.findById(driverId);
                    driverRace.setDriver(driver);

                    if (driverRace.getFinishedForInSeconds() == 0) {
                        throw new InvalidFinishedTimeException();
                    }
                })
                .sorted(Comparator.comparingDouble(DriverRaces::getFinishedForInSeconds))
                .toList();
    }

    private void results(List<DriverRaces> filteredAndValidatedResults, List<DriverRaces> existingResults, Race race) {
        for (DriverRaces result : filteredAndValidatedResults) {
            boolean isUpdated = false;

            isUpdated = isUpdatedSuccessfully(existingResults, result, isUpdated);

            if (!isUpdated) {
                DriverRaces newResult = new DriverRaces();
                newResult.setDriver(result.getDriver());
                newResult.setRace(race);
                newResult.setFinishedForInSeconds(result.getFinishedForInSeconds());
                int positionPoints = 20 - (filteredAndValidatedResults.indexOf(result) + 1);
                newResult.setPoints(positionPoints);

                driverRaceRepository.save(newResult);
            }
        }
    }

    private boolean isUpdatedSuccessfully(List<DriverRaces> existingResults, DriverRaces result, boolean isUpdated) {
        for (DriverRaces existingResult : existingResults) {
            if ((existingResult.getDriver().getId() == (result.getDriver().getId()))
                    && existingResult.getRace().getId() == result.getRace().getId()) {

                if (existingResult.getPoints() == 0) {
                    existingResult.setFinishedForInSeconds(result.getFinishedForInSeconds());
                    int positionPoints = 20 - (existingResults.indexOf(existingResult) + 1);
                    existingResult.setPoints(positionPoints);

                    driverRaceRepository.save(existingResult);

                    isUpdated = true;
                }
            }
        }

        return isUpdated;
    }
}