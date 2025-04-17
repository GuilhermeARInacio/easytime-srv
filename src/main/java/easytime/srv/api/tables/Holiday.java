package easytime.srv.api.tables;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean isNational = true;

    // Getters and Setters
}
