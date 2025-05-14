package easytime.srv.api.service;

import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.TimeLogsRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Service
public class PontoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeLogsRepository timeLogsRepository;

    public TimeLog registrarPonto(LoginDto login, LocalDate dataHoje, LocalTime horaAgora) {
        User user = userRepository.findByLogin(login.login()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        System.out.println("Data e hora atuais: " + dataHoje + " " + horaAgora);

        var timeLog = timeLogsRepository.findByUserAndData(user, dataHoje)
                .orElse(new TimeLog(user, dataHoje)); // cria um novo TimeLog se não existir

        // validacoes

        int cont = timeLog.getCont();
        if (cont >= 6) {
            throw new IllegalArgumentException("Todas as batidas de ponto já foram registradas para hoje.");
        }

        boolean isEntrada = cont % 2 == 0;
        int indice = (cont / 2) + 1;

        timeLog.setPonto(isEntrada, indice, Time.valueOf(horaAgora));

        System.out.println(timeLog);

        timeLogsRepository.save(timeLog);
        return timeLog;
    }

}
