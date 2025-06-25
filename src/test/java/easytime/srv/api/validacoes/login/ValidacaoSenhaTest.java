package easytime.srv.api.validacoes.login;

import easytime.srv.api.infra.exceptions.CampoInvalidoException;
import easytime.srv.api.infra.exceptions.CampoVazioException;
import easytime.srv.api.model.user.DTOUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ValidacaoSenhaTest {

    @InjectMocks
    private ValidacaoSenha validacaoSenha;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verifica se a senha é vazia no userLogin")
    void whenCampoVazio() {
        String senha = "";
        assertThrows(CampoVazioException.class, () -> validacaoSenha.validar(new DTOUsuario("user", senha)));
    }

    @Test
    @DisplayName("Verifica se a senha é válida no userLogin")
    void whenSenhaInvalida(){
        String senha = "12345678";
        assertThrows(CampoInvalidoException.class, () -> validacaoSenha.validar(new DTOUsuario("user", senha)));
    }
}