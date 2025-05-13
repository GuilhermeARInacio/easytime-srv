package easytime.srv.api.tables;

import jakarta.persistence.*;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Setter
@Table(name = "Appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private Timestamp registrationTime;

    @Column(nullable = false)
    private String type;

    private String status;

    private String comment;

    // Getters and Setters
}