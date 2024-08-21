package bg.a1.formula1.controllers;

import bg.a1.formula1.models.dtos.request.TeamRequestDto;
import bg.a1.formula1.models.dtos.response.TeamResponseDto;
import bg.a1.formula1.models.entity.Team;
import bg.a1.formula1.services.contracts.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<TeamResponseDto> findAll() {
        return teamService.findAll().stream()
                .map(team -> modelMapper.map(team, TeamResponseDto.class)).toList();
    }

    @GetMapping("/{id}")
    public TeamResponseDto findById(@PathVariable long id) {
        Team team = teamService.findById(id);
        return modelMapper.map(team, TeamResponseDto.class);
    }

    @PostMapping
    public TeamResponseDto create(@Valid @RequestBody TeamRequestDto requestDto) {
        Team team = modelMapper.map(requestDto, Team.class);
        teamService.create(team);
        return modelMapper.map(team, TeamResponseDto.class);
    }
}