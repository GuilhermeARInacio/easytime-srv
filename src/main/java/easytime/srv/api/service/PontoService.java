package easytime.srv.api.service;

import easytime.srv.api.model.pontos.TimeLogDto;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.TimeLogsRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.validacoes.bater_ponto.ValidacaoPonto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Service
public class PontoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeLogsRepository timeLogsRepository;

    @Autowired
    private List<ValidacaoPonto> validacoes;

    public TimeLogDto registrarPonto(LoginDto login, LocalDate dataHoje, Time horaAgora) {
        User user = userRepository.findByLogin(login.login()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        System.out.println("Data e hora atuais: " + dataHoje + " " + horaAgora);

        var timeLog = timeLogsRepository.findByUserAndData(user, dataHoje)
                .orElse(new TimeLog(user, dataHoje)); // cria um novo TimeLog se não existir

        validacoes.forEach(v -> v.validar(timeLog, horaAgora));

        timeLog.setPonto(horaAgora);

        System.out.println(timeLog);

        timeLogsRepository.save(timeLog);
        return new TimeLogDto(timeLog);
    }

}
