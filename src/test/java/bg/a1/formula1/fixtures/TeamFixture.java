package bg.a1.formula1.fixtures;

import bg.a1.formula1.models.entity.Team;

import java.util.ArrayList;

public class TeamFixture {

    private static final String TEAM_NAME = "RedBull";

    public static Team getTeam() {
        Team team = new Team();
        team.setId(1L);
        team.setName(TEAM_NAME);
        team.setDrivers(new ArrayList<>());

        return team;
    }
}