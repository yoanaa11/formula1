package bg.a1.formula1.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TeamStandingDto {

    private String teamName;

    private int totalPoints;
}