package bg.a1.formula1.services.contracts;

import bg.a1.formula1.models.entity.Team;

import java.util.List;

public interface TeamService {

    List<Team> findAll();

    Team findById(long id);

    Team findByName(String name);

    Team create(Team team);
}