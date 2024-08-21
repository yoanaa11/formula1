package bg.a1.formula1.models.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DriverRacesRequestDto {

    @NotNull(message = "Driver id can't be null.")
    private long driverId;

    @NotNull(message = "Finished time can't be null.")
    @Min(value = 0, message = "Finished time must not be a negative and in seconds.")
    private int finishedForInSeconds;
}