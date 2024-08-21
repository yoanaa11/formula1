package bg.a1.formula1.models.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DriverRequestDto {

    @NotNull(message = "First name can't be null.")
    private String firstName;

    @NotNull(message = "Last name can't be null.")
    private String lastName;

    @NotNull(message = "Team can't be null.")
    private String teamName;
}