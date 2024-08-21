package bg.a1.formula1.repositories;

import bg.a1.formula1.models.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

    Optional<Race> findByName(String name);
}