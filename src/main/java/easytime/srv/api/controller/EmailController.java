package easytime.srv.api.controller;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.model.email.EmailRequest;
import easytime.srv.api.model.email.ValidationCode;
import easytime.srv.api.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    @Operation(summary = "Enviar email com código de validação", description = "Usuário envia email que deseja receber o código de validação e sistema envia para esse endereço um email com o código de validação.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<String> sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        try{
            emailService.sendEmail(emailRequest);
            return ResponseEntity.ok("Email enviado, verifique sua caixa de entrada ou spam.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    Integer codigo = 0;

    @PostMapping("/redefine-senha")
    @Operation(summary = "Redefinir senha", description = "Usuário envia o código de validação recebido por email e a nova senha desejada, sistema então atualiza essa senha.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Object> validateCode(@RequestBody @Valid ValidationCode validationCode) {
        try {
            emailService.validateCode(validationCode.code());
            emailService.redefinirSenha(validationCode.email(), validationCode.senha());
            return ResponseEntity.ok("Senha redefinida com sucesso.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Tente enviar um novo código, ocorreu um erro pois "+e.getMessage());
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Tente enviar um novo código, ocorreu um erro pois " + e.getMessage());
        }catch (CampoInvalidoException e ) {
            return ResponseEntity.status(400).body("Tente enviar um novo código, ocorreu um erro pois "+e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("Senha não redefinida, erro: "+e);
        }
    }
}
