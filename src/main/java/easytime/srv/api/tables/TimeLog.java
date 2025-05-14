package easytime.srv.api.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalDate;

@Entity
@Setter
@Getter
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
    private LocalDate data;

    @Column(nullable = true, columnDefinition = "time")
    private Time E1;

    @Column(nullable = true, columnDefinition = "time")
    private Time S1;

    @Column(nullable = true, columnDefinition = "time")
    private Time E2;

    @Column(nullable = true, columnDefinition = "time")
    private Time S2;

    @Column(nullable = true, columnDefinition = "time")
    private Time E3;

    @Column(nullable = true, columnDefinition = "time")
    private Time S3;

    private String location;

    private String device;

    private int cont = 0;

    private float horas_trabalhadas = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDENTE;

    public void setPonto(Time hora) {
        try {
            Field field = TimeLog.class.getDeclaredField(this.getUltimoBatimentoName(this.cont));;

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

    public String getUltimoBatimentoName(int cont){
        boolean isEntrada = cont % 2 == 0;
        int indice = (cont / 2) + 1;

        return isEntrada ? "E" + indice : "S" + indice;
    }

    public Object getUltimoBatimentoValue() {
        if (this.cont <= 0) {
            throw new RuntimeException("No batimento exists to retrieve.");
        }
        try {
            Field field = TimeLog.class.getDeclaredField(this.getUltimoBatimentoName(this.cont - 1));
            field.setAccessible(true); // Allow access to private fields
            return field.get(this); // Get the value of the field for the current instance
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error accessing attribute: " + e.getMessage(), e);
        }
    }
}