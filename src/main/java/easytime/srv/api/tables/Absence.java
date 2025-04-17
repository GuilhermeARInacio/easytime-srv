package easytime.srv.api.tables;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Absences")
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Date date;

    private String reason;

    @Column(columnDefinition = "boolean default false")
    private Boolean approved = false;

    @Lob
    private String justification;

    // Getters and Setters
}