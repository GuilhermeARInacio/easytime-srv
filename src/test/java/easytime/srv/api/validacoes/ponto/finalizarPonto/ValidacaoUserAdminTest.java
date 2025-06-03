// File: src/test/java/easytime/srv/api/validacoes/ponto/finalizarPonto/ValidacaoUserAdminTest.java
package easytime.srv.api.validacoes.ponto.finalizarPonto;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidacaoUserAdminTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ValidacaoUserAdmin validacao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validar_usuarioNaoEncontrado_lancaInvalidUserException() {
        when(userRepository.findByLogin("user1")).thenReturn(Optional.empty());
        PedidoPonto pedido = mock(PedidoPonto.class);

        assertThrows(InvalidUserException.class, () -> validacao.validar(pedido, "user1"));
    }

    @Test
    void validar_usuarioAdmin_naoLancaExcecao() {
        User admin = mock(User.class);
        when(admin.getRole()).thenReturn("admin");
        when(userRepository.findByLogin("admin")).thenReturn(Optional.of(admin));
        PedidoPonto pedido = mock(PedidoPonto.class);

        assertDoesNotThrow(() -> validacao.validar(pedido, "admin"));
    }

    @Test
    void validar_usuarioNaoAdmin_lancaIllegalCallerException() {
        User user = mock(User.class);
        when(user.getRole()).thenReturn("user");
        when(userRepository.findByLogin("user2")).thenReturn(Optional.of(user));
        PedidoPonto pedido = mock(PedidoPonto.class);

        IllegalCallerException ex = assertThrows(IllegalCallerException.class,
                () -> validacao.validar(pedido, "user2"));
        assertEquals("Usuário não tem permissão para finalizar ponto. Apenas administradores podem realizar essa ação.", ex.getMessage());
    }
}