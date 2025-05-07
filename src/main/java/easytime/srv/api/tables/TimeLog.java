package easytime.srv.api.tables;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TimeLogs")
public class TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogType logType;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private Timestamp timestamp;

    private String location;

    private String device;

    private enum LogType {
        IN, OUT, BREAK_START, BREAK_END
    }

    // Getters and Setters
}