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
            Field field = TimeLog.class.getDeclaredField(this.getUltimoBatimentoName(this.cont));

            field.setAccessible(true);
            field.set(this, hora);
            this.cont++;
            this.calcularHoras();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erro ao definir o campo: " + e);
        }

    }

    public void calcularHoras(){
        try {
            if (this.cont % 2 == 0) {
                Field entradaF = TimeLog.class.getDeclaredField(this.getUltimoBatimentoName(cont-2));
                Field saidaF = TimeLog.class.getDeclaredField(this.getUltimoBatimentoName(cont-1));

                entradaF.setAccessible(true);
                saidaF.setAccessible(true);

                Time entrada = (Time) entradaF.get(this);
                Time saida = (Time) saidaF.get(this);

                if (entrada != null && saida != null) {
                    long diff = saida.getTime() - entrada.getTime();
                    this.horas_trabalhadas += (float) diff / (1000 * 60 * 60);
                    System.out.println(diff);
                }
                System.out.println(this.horas_trabalhadas);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erro ao calcular horas: " + e);
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
            return null;
        }
        try {
            Field field = TimeLog.class.getDeclaredField(this.getUltimoBatimentoName(this.cont - 1));
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erro acessando atributo: " + e.getMessage(), e);
        }
    }
}