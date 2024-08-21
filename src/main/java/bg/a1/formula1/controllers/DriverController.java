package bg.a1.formula1.controllers;

import bg.a1.formula1.models.dtos.request.DriverRequestDto;
import bg.a1.formula1.models.dtos.response.DriverResponseDto;
import bg.a1.formula1.models.entity.Driver;
import bg.a1.formula1.services.contracts.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/driver")
public class DriverController {

    private final DriverService driverService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<DriverResponseDto> findAll() {
        return driverService.findAll().stream()
                .map(driver -> modelMapper.map(driver, DriverResponseDto.class)).toList();
    }

    @GetMapping("/{id}")
    public DriverResponseDto findById(@PathVariable int id) {
        Driver driver = driverService.findById(id);
        return modelMapper.map(driver, DriverResponseDto.class);
    }

    @PostMapping
    public DriverResponseDto create(@Valid @RequestBody DriverRequestDto requestDto) {
        Driver driver = modelMapper.map(requestDto, Driver.class);
        driverService.create(driver);
        return modelMapper.map(driver, DriverResponseDto.class);
    }

    @PutMapping("/{driverId}/transfer-to/{teamId}")
    public DriverResponseDto transferDriver(@PathVariable long driverId, @PathVariable long teamId) {
        driverService.transferDriverToTeam(driverId, teamId);
        Driver driver = driverService.findById(driverId);
        return modelMapper.map(driver, DriverResponseDto.class);
    }

    @DeleteMapping("/{driverId}")
    public void retireDriver(@PathVariable long driverId) {
        driverService.retireDriver(driverId);
    }
}