package easytime.srv.api.validacoes.Login;

import easytime.srv.api.infra.exceptions.UsuarioESenhaNotFoundException;
import easytime.srv.api.model.DTOUsuario;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ValidacaoUsuarioESenhaExistsTest {

    @InjectMocks
    private ValidacaoUsuarioESenhaExists validacaoUsuarioESenhaExists;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenUserIsEmpty() {
        when(userRepository.findByLogin(any())).thenReturn(Optional.empty());
        assertThrows(UsuarioESenhaNotFoundException.class, () -> validacaoUsuarioESenhaExists.validar(new DTOUsuario("user", "password")));
    }

    @Test
    void whenPasswordIsInvalid() {
        when(userRepository.findByLogin(any())).thenReturn(Optional.of(new User()));
        assertThrows(UsuarioESenhaNotFoundException.class, () -> validacaoUsuarioESenhaExists.validar(new DTOUsuario("user", "wrongPassword")));
    }
}