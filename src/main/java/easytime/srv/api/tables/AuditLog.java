package easytime.srv.api.tables;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "AuditLogs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String action;

    private String tableName;

    @Lob
    private String description;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private Timestamp timestamp;

    // Getters and Setters
}
