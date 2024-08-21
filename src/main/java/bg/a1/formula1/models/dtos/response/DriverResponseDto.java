package bg.a1.formula1.models.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class DriverResponseDto {

    private long id;

    private String firstName;

    private String lastName;

    private String teamName;

    private List<DriverRacesResponseDto> driverRaces = new ArrayList<>();
}