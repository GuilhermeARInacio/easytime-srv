package easytime.srv.api.tables;

import easytime.srv.api.model.pontos.TimeLogDto;
import jakarta.persistence.*;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Setter
@Table(name = "TimeLogs")
public class TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Date data;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private Timestamp E1;

    @Column(nullable = true, columnDefinition = "timestamp default current_timestamp")
    private Timestamp S1;

    @Column(nullable = true, columnDefinition = "timestamp default current_timestamp")
    private Timestamp E2;

    @Column(nullable = true, columnDefinition = "timestamp default current_timestamp")
    private Timestamp S2;

    @Column(nullable = true, columnDefinition = "timestamp default current_timestamp")
    private Timestamp E3;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private Timestamp S3;

    private String location;

    private String device;

    public static TimeLog toEntity(TimeLogDto dto) {
        TimeLog timeLog = new TimeLog();
        timeLog.setUser(dto.user());

        if(dto.data() != null) {
            timeLog.setData(Date.valueOf(dto.data()));
        }
        if (dto.E1() != null) {
            timeLog.setE1(Timestamp.valueOf(dto.E1()));
        }
        if (dto.S1() != null) {
            timeLog.setS1(Timestamp.valueOf(dto.S1()));
        }
        if (dto.E2() != null) {
            timeLog.setE2(Timestamp.valueOf(dto.E2()));
        }
        if (dto.S2() != null) {
            timeLog.setS2(Timestamp.valueOf(dto.S2()));
        }
        if (dto.E3() != null) {
            timeLog.setE3(Timestamp.valueOf(dto.E3()));
        }
        if (dto.S3() != null) {
            timeLog.setS3(Timestamp.valueOf(dto.S3()));
        }

        return timeLog;
    }

    // Getters and Setters
}