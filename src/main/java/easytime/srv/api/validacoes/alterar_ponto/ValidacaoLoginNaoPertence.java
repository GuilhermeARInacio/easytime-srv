package easytime.srv.api.validacoes.alterar_ponto;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.model.pontos.AlterarPontoDto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoLoginNaoPertence implements ValidacaoAlterarPonto{

    @Autowired
    private UserRepository userRepository;

    public void validar(AlterarPontoDto dto, TimeLog timeLog) {
        if (!timeLog.getUser().getLogin().equals(dto.login())) {
            throw new IllegalArgumentException("O ponto não pertence ao usuário informado.");
        }
        if (userRepository.findByLogin(dto.login()).isEmpty()) {
            throw new InvalidUserException("Login não encontrado. Verifique o nome de usuário e tente novamente.");
        }
    }
}
