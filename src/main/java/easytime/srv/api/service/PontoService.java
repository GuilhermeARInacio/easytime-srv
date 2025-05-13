package easytime.srv.api.service;

import easytime.srv.api.model.pontos.TimeLogDto;
import easytime.srv.api.model.pontos.EntradaESaida;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.tables.Appointment;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.TimeLogsRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PontoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeLogsRepository repository;

    public void registrarPonto(LoginDto login) {
        User user = userRepository.findByLogin(login.login()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));


    }
}
