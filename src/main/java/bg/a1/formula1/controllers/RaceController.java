package bg.a1.formula1.controllers;

import bg.a1.formula1.models.dtos.request.RaceRequestDto;
import bg.a1.formula1.models.dtos.response.RaceResponseDto;
import bg.a1.formula1.models.entity.Race;
import bg.a1.formula1.services.contracts.RaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/race")
public class RaceController {

    private final RaceService raceService;
    private final ModelMapper modelMapper;

    @PostMapping
    public RaceResponseDto create(@Valid @RequestBody RaceRequestDto requestDto) {
        Race race = modelMapper.map(requestDto, Race.class);
        raceService.create(race);
        return modelMapper.map(race, RaceResponseDto.class);
    }
}