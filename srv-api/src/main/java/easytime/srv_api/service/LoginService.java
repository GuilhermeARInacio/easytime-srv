package easytime.srv_api.service;

import easytime.srv_api.model.DTOUsuario;
import easytime.srv_api.validacoes.Login.ValidacaoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    private List<ValidacaoLogin> validacoes;

    public String login(DTOUsuario usuario) {

        validacoes.forEach(v -> {
            v.validar(usuario);
        });


        return null;
    }
}
