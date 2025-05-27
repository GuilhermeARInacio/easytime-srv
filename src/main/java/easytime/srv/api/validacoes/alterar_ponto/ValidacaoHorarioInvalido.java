package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
public class ValidacaoHorarioInvalido implements ValidacaoAlterarPonto{
    public void validar(AlterarPontoDto dto, TimeLog timeLog) {
        List<String> campos = Arrays.asList("entrada1", "saida1", "entrada2", "saida2", "entrada3", "saida3");

        for(String campo : campos) {
            try{
                Field metodo = AlterarPontoDto.class.getDeclaredField(campo);
                metodo.setAccessible(true);
                LocalTime time = (LocalTime) metodo.get(dto);
                if(time != null && (time.isBefore(LocalTime.of(6, 0)) || time.isAfter(LocalTime.of(23, 0)))) {
                    throw new IllegalArgumentException("Horários entre 23h e 6h não são permitidos.");
                }
            } catch (IllegalArgumentException e){
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Erro ao validar campo: " + campo, e);
            }
        }
    }
}
