package easytime.srv.api.tables;

import easytime.srv.api.model.UserDTO;
import easytime.srv.api.util.PasswordUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String login;

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


    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setNome(userDTO.getNome());
        user.setEmail(userDTO.getEmail());
        user.setLogin(userDTO.getLogin());
        user.setPassword(PasswordUtil.encryptPassword(userDTO.getPassword()));
        user.setSector(userDTO.getSector());
        user.setJobTitle(userDTO.getJobTitle());
        user.setRole(userDTO.getRole());
        user.setIsActive(userDTO.getIsActive());
        user.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        user.setLastLogin(new Timestamp(new java.util.Date().getTime()));
        user.setUpdateDate(new Timestamp(new java.util.Date().getTime()));
        return user;
    }
}