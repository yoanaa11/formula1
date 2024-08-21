package bg.a1.formula1.models.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RaceRequestDto {

    @NotNull(message = "Name can't be null.")
    @Size(max = 20, message = "Name can't be more than 20 characters.")
    private String name;
}