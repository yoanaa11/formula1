package bg.a1.formula1.models.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DriverRacesResponseDto {

    private long id;

    private long driverId;

    private long raceId;

    private int finishedForInSeconds;
}