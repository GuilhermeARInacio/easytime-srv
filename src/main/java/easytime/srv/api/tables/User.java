package easytime.srv.api.tables;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String sector;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false, columnDefinition = "varchar(50) default 'user'")
    private String role = "user";

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false, columnDefinition = "timestamp default current_timestamp")
    private Timestamp creationDate;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private Timestamp updateDate;

    @Column(nullable = false)
    private Timestamp lastLogin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shift> shifts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeLog> timeLogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Absence> absences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Overtime> overtimes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditLog> auditLogs;

    // Getters and Setters
}