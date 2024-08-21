package bg.a1.formula1.repositories;

import bg.a1.formula1.models.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

    Race findByName(String name);
}