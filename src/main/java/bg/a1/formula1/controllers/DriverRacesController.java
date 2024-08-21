package bg.a1.formula1.controllers;

import bg.a1.formula1.models.dtos.request.DriverRacesRequestDto;
import bg.a1.formula1.models.dtos.response.DriverRacesResponseDto;
import bg.a1.formula1.models.entity.DriverRaces;
import bg.a1.formula1.services.contracts.DriverRaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/race")
public class DriverRacesController {

    private final DriverRaceService driverRaceService;
    private final ModelMapper modelMapper;

    @PutMapping("/enrol/{driverId}")
    public DriverRacesResponseDto registerDriverToARace(@PathVariable long driverId, @RequestParam(name = "raceId") long raceId) {
        DriverRaces driverRaces = driverRaceService.registerDriverToARace(driverId, raceId);
        return modelMapper.map(driverRaces, DriverRacesResponseDto.class);
    }

    @PostMapping("/result")
    public void raceResults(@RequestParam(name = "raceId") long raceId,
                            @Valid @RequestBody List<DriverRacesRequestDto> driverRacesRequestDtos) {
        List<DriverRaces> driverRaces = driverRacesRequestDtos.stream()
                .map(driverRacesRequestDto -> modelMapper.map(driverRacesRequestDto, DriverRaces.class)).toList();
        driverRaceService.raceResults(raceId, driverRaces);
    }
}