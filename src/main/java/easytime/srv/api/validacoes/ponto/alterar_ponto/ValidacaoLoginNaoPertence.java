package easytime.srv.api.validacoes.ponto.alterar_ponto;

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

    public void validar(AlterarPontoDto dto, TimeLog timeLog, String userLogin) {
        var user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new InvalidUserException("Login não encontrado. Verifique o nome de usuário e tente novamente."));

        // Verifica se o usuário que está alterando o ponto é o mesmo do ponto ou um administrador
        if(!user.getRole().equals("admin") && !timeLog.getUser().getLogin().equals(userLogin)) {
            throw new IllegalCallerException("Você não tem permissão para alterar registros de ponto de outros usuários.");
        }
    }
}
