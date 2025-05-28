package easytime.srv.api.service;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.model.pontos.ConsultaPontosDto;
import easytime.srv.api.model.pontos.RegistroCompletoDto;
import easytime.srv.api.model.pontos.TimeLogDto;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.TimeLogsRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.util.DateUtil;
import easytime.srv.api.validacoes.alterar_ponto.ValidacaoAlterarPonto;
import easytime.srv.api.validacoes.bater_ponto.ValidacaoPonto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PontoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeLogsRepository timeLogsRepository;

    @Autowired
    private List<ValidacaoPonto> validacoes;

    @Autowired
    private List<ValidacaoAlterarPonto> validacoesAlterar;

    public TimeLogDto registrarPonto(LoginDto login) {
        User user = userRepository.findByLogin(login.login()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        LocalDate dataHoje = LocalDate.now();
        Time horaAgora = Time.valueOf(LocalTime.now());

        var timeLog = timeLogsRepository.findByUserAndData(user, dataHoje)
                .orElse(new TimeLog(user, dataHoje)); // cria um novo TimeLog se não existir

        validacoes.forEach(v -> v.validar(timeLog, horaAgora));

        timeLog.setPonto(horaAgora);

        timeLogsRepository.save(timeLog);
        return new TimeLogDto(timeLog);
    }

    public void removerPonto(Integer id) {
        var timeLog = timeLogsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ponto não encontrado."));
        timeLogsRepository.delete(timeLog);
    }

    public List<RegistroCompletoDto> consultar(ConsultaPontosDto dto) {
        var user = userRepository.findByLogin(dto.login()).orElseThrow(() -> new InvalidUserException("Usuário não encontrado."));

        var dateInicio = DateUtil.convertUserDateToDBDate(dto.dtInicio());
        var dateFinal = DateUtil.convertUserDateToDBDate(dto.dtFinal());
        if (dateInicio.isAfter(dateFinal)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data final.");
        }

        var response = timeLogsRepository.findAllByUserAndDataBetween(user, dateInicio, dateFinal);

        if (response.isEmpty()) {
            throw new NotFoundException("Nenhum ponto encontrado.");
        }

        return response.stream().map(RegistroCompletoDto::new).collect(Collectors.toList());
    }

    public RegistroCompletoDto alterarPonto(AlterarPontoDto dto){
        var timeLog = timeLogsRepository.findById(dto.idPonto())
                .orElseThrow(() -> new NotFoundException("ID de ponto não localizado. Verifique se o código está correto."));

        validacoesAlterar.forEach(validacao -> validacao.validar(dto, timeLog));

        timeLog.alterarPonto(dto);

        timeLogsRepository.save(timeLog);
        return new RegistroCompletoDto(timeLog);
    }
}
