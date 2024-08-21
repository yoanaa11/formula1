package bg.a1.formula1.repositories;

import bg.a1.formula1.models.entity.DriverRaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRaceRepository extends JpaRepository<DriverRaces, Long> {

    List<DriverRaces> findByDriverId(long id);
}