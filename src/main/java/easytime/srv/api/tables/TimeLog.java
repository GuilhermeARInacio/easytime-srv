package easytime.srv.api.tables;

import easytime.srv.api.model.pontos.TimeLogDto;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@NoArgsConstructor
@ToString
@Table(name = "TimeLogs")
public class TimeLog {

    public TimeLog(User user, LocalDate date) {
        this.user = user;
        this.data = date;
        this.E1 = LocalTime.now();
        this.cont++;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime E1;

    private LocalTime S1;

    private LocalTime E2;

    private LocalTime S2;

    private LocalTime E3;

    private LocalTime S3 = LocalTime.now();

    private String location;

    private String device;

    private int cont = 0;

    private float horas_trabalhadas = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDENTE;

    public static TimeLog toEntity(TimeLogDto dto) {
        TimeLog timeLog = new TimeLog();
        timeLog.setUser(dto.user());

        if (dto.data() != null) {
            timeLog.setData(dto.data());
        }
        if (dto.E1() != null) {
            timeLog.setE1(dto.E1());
        }
        if (dto.S1() != null) {
            timeLog.setS1(dto.S1());
        }
        if (dto.E2() != null) {
            timeLog.setE2(dto.E2());
        }
        if (dto.S2() != null) {
            timeLog.setS2(dto.S2());
        }
        if (dto.E3() != null) {
            timeLog.setE3(dto.E3());
        }
        if (dto.S3() != null) {
            timeLog.setS3(dto.S3());
        }

        return timeLog;
    }

    // Getters and Setters
    public Integer getCont(){
        return cont;
    }

    public enum Status {
        PENDENTE,
        APROVADO,
        REPROVADO
    }

    public void setPonto(boolean isEntrada, int indice, LocalTime hora) {
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogType logType = LogType.IN;

    public enum LogType {
        IN, OUT, BREAK_START, BREAK_END
    }
}