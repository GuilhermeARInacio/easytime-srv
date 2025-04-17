package easytime.srv.api.tables;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "Overtimes")
public class Overtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal hours;

    private String reason;

    @Column(columnDefinition = "boolean default false")
    private Boolean approved = false;

    // Getters and Setters
}