package easytime.srv.api.tables;

import easytime.srv.api.model.pontos.TimeLogDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Entity
@Setter
@NoArgsConstructor
@ToString
@Table(name = "TimeLogs")
public class TimeLog {

    public TimeLog(User user, LocalDate date) {
        this.user = user;
        this.data = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Getter
    private LocalDate data;

    @Column(nullable = true, columnDefinition = "time")
    @Getter
    private Time E1;

    @Column(nullable = true, columnDefinition = "time")
    @Getter
    private Time S1;

    @Column(nullable = true, columnDefinition = "time")
    @Getter
    private Time E2;

    @Column(nullable = true, columnDefinition = "time")
    @Getter
    private Time S2;

    @Column(nullable = true, columnDefinition = "time")
    @Getter
    private Time E3;

    @Column(nullable = true, columnDefinition = "time")
    @Getter
    private Time S3;

    private String location;

    private String device;

    @Getter
    private int cont = 0;

    private float horas_trabalhadas = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDENTE;

    public static TimeLog toEntity(TimeLogDto dto) {
        TimeLog timeLog = new TimeLog();
        timeLog.setUser(dto.user());

        return timeLog;
    }

    public void setPonto(boolean isEntrada, int indice, Time hora) {
        try {
            Field field;
            if (isEntrada) {
                field = TimeLog.class.getDeclaredField("E"+indice);
            } else {
                field = TimeLog.class.getDeclaredField("S"+indice);
            }

            field.setAccessible(true);
            field.set(this, hora);
            this.cont++;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erro ao definir o campo: " + e);
        }

    }

    public enum Status {
        PENDENTE,
        APROVADO,
        REPROVADO
    }
}