package easytime.srv.api.validacoes.ponto.finalizarPonto;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoUserAdmin implements ValidacaoFinalizarPonto{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validar(PedidoPonto pedido, String userLogin) {
        var user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new InvalidUserException("Usuário não encontrado: " + userLogin));
        if(!user.getRole().equals("admin")){
            throw new IllegalCallerException("Usuário não tem permissão para finalizar ponto. Apenas administradores podem realizar essa ação.");
        }
    }
}
