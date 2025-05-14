package easytime.srv.api.controller;

import easytime.srv.api.model.pontos.TimeLogDto;
import easytime.srv.api.model.user.LoginDto;
import easytime.srv.api.service.PontoService;
import easytime.srv.api.tables.TimeLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Controller
@RestController
@RequestMapping("/ponto")
public class PontoController {

    @Autowired
    private PontoService service;

    @PostMapping
    public ResponseEntity<?> registrarPonto(@RequestBody LoginDto login) {
        try{
            LocalDate dataHoje = LocalDate.now();
            LocalTime horaAgora = LocalTime.now();

            var ponto = service.registrarPonto(login, dataHoje, horaAgora);
            return ResponseEntity.ok(ponto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar ponto: " + e.getMessage());
        }
    }
}
