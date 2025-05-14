package easytime.srv.api.service;

import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.TimeLogsRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class PontoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeLogsRepository timeLogsRepository;

    public TimeLog registrarPonto(LoginDto login) {
        User user = userRepository.findByLogin(login.login()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        LocalDate dataHoje = LocalDate.now();

        var timeLog = timeLogsRepository.findByUserAndData(user, dataHoje)
                .orElse(new TimeLog(user, dataHoje)); // cria um novo TimeLog se não existir

        LocalTime horaAgora = LocalTime.now();

        // validacoes
//       pipipi
//        popopo
//               pipip
//

        int cont = timeLog.getCont();
        if (cont >= 6) {
            throw new IllegalArgumentException("Todas as batidas de ponto já foram registradas para hoje.");
        }

        boolean isEntrada = cont % 2 == 0;
        int indice = (cont / 2) + 1;

        timeLog.setPonto(isEntrada, indice, horaAgora);

        timeLogsRepository.save(timeLog);
        System.out.println(timeLog);
        return timeLog;
    }

}
