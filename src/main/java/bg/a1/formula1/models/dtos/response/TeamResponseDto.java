package bg.a1.formula1.models.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class TeamResponseDto {

    private long id;

    private String name;

    private List<DriverResponseDto> drivers = new ArrayList<>();
}