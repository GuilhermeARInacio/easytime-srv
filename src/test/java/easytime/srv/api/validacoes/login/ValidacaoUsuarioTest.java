package easytime.srv.api.validacoes.login;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.infra.exceptions.CampoVazioException;
import easytime.srv.api.model.user.DTOUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class ValidacaoUsuarioTest {

    @InjectMocks
    private ValidacaoUsuario validacaoUsuario;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verifica se o usuario é vazio no userLogin")
    void whenCampoVazio() {
        String usuario = "";
        assertThrows(CampoVazioException.class, () -> validacaoUsuario.validar(new DTOUsuario(usuario, "senha")));
    }

    @Test
    @DisplayName("Verifica se o usuario é valido no userLogin")
    void whenUsuarioInvalido(){
        String usuario = "12345678";
        assertThrows(CampoInvalidoException.class, () -> validacaoUsuario.validar(new DTOUsuario(usuario, "senha")));
    }
}