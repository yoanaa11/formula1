package bg.a1.formula1.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
//@IdClass(DriverRaceId.class)
@Table(name = "driver_races")
public class DriverRaces {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_race_id_seq")
    @SequenceGenerator(name = "driver_race_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    @JsonIgnore
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;

    @Column(name = "finished_for")
    private double finishedForInSeconds;

    @Column(name = "points", nullable = false)
    private int points;

    @Override
    public String toString() {
        return "DriverRaces{" +
                "id=" + id +
                ", driver=" + driver +
                ", race=" + race +
                ", finishedForInSeconds=" + finishedForInSeconds +
                ", points=" + points +
                '}';
    }

    //    @Override
//    public String toString() {
//        return "DriverRaces{" +
//                "driverId=" + driverId +
//                ", raceId=" + raceId +
//                ", driver=" + driver +
//                ", race=" + race +
//                ", finishedForInSeconds=" + finishedForInSeconds +
//                ", points=" + points +
//                '}';
//    }
}