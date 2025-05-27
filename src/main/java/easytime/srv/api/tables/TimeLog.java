package easytime.srv.api.tables;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.Duration;
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

    private Time horas_trabalhadas = Time.valueOf("00:00:00");

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDENTE;

    public void setPonto(Time hora) {
        try {
            String fieldName = this.getUltimoBatimentoName(this.cont);
            Field field = TimeLog.class.getDeclaredField(fieldName);

            field.setAccessible(true);
            field.set(this, hora);

            if (fieldName.startsWith("S")) {
                this.calcularHoras(this.cont);
            }

            this.cont++;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erro ao definir o campo: " + e);
        }

    }

    public void calcularHoras(int cont) {
        try {
            if (this.cont > 0) {
                Field entradaF = TimeLog.class.getDeclaredField(this.getUltimoBatimentoName(cont - 1));
                Field saidaF = TimeLog.class.getDeclaredField(this.getUltimoBatimentoName(cont));

                entradaF.setAccessible(true);
                saidaF.setAccessible(true);

                Time entrada = (Time) entradaF.get(this);
                Time saida = (Time) saidaF.get(this);

                if (entrada != null && saida != null) {
                    long diff = saida.getTime() - entrada.getTime();
                    this.horas_trabalhadas = new Time(this.horas_trabalhadas.getTime() + diff);
                }
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

    public TimeLog alterarPonto(AlterarPontoDto dto) {
        if (dto.data() != null && !String.valueOf(data).isEmpty()) {
            this.setData(LocalDate.parse(dto.data()));
        }
        if (dto.entrada1() != null && !String.valueOf(E1).isEmpty()) {
            this.setE1(Time.valueOf(dto.entrada1()));
        }
        if (dto.saida1() != null && !String.valueOf(S1).isEmpty()) {
            this.setS1(Time.valueOf(dto.saida1()));
        }
        if (dto.entrada2() != null && !String.valueOf(E2).isEmpty()) {
            this.setE2(Time.valueOf(dto.entrada2()));
        }
        if (dto.saida2() != null && !String.valueOf(S2).isEmpty()) {
            this.setS2(Time.valueOf(dto.saida2()));
        }
        if (dto.entrada3() != null && !String.valueOf(E3).isEmpty()) {
            this.setE3(Time.valueOf(dto.entrada3()));
        }
        if (dto.saida3() != null && !String.valueOf(S3).isEmpty()) {
            this.setS3(Time.valueOf(dto.saida3()));
        }

        this.horas_trabalhadas = Time.valueOf("00:00:00");

        if (this.E1 != null && this.S1 != null) {
            this.calcularHoras(1);
        }
        if (this.E2 != null && this.S2 != null) {
            this.calcularHoras(3);
        }
        if (this.E3 != null && this.S3 != null) {
            this.calcularHoras(5);
        }

        return this;
    }
}