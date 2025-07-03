// File: src/test/java/easytime/srv/api/service/PontoServiceTest.java
package easytime.srv.api.service;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.infra.security.TokenService;
import easytime.srv.api.model.Status;
import easytime.srv.api.model.pontos.*;
import easytime.srv.api.model.user.UserDTO;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.User;
import easytime.srv.api.tables.repositorys.PedidoPontoRepository;
import easytime.srv.api.tables.repositorys.TimeLogsRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.util.DateTimeUtil;
import easytime.srv.api.validacoes.ponto.alterar_ponto.ValidacaoAlterarPonto;
import easytime.srv.api.validacoes.ponto.bater_ponto.ValidacaoPonto;
import easytime.srv.api.validacoes.ponto.finalizarPonto.ValidacaoFinalizarPonto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PontoServiceTest {

    @Mock TokenService tokenService;
    @Mock UserRepository userRepository;
    @Mock TimeLogsRepository timeLogsRepository;
    @Mock PedidoPontoRepository pedidoPontoRepository;
    @Mock ValidacaoPonto validacaoPonto;
    @Mock ValidacaoAlterarPonto validacaoAlterarPonto;
    @Mock ValidacaoFinalizarPonto validacaoFinalizarPonto;

    @InjectMocks PontoService pontoService;

    @Captor ArgumentCaptor<TimeLog> timeLogCaptor;
    @Captor ArgumentCaptor<PedidoPonto> pedidoPontoCaptor;

    @BeforeEach
    void setup() throws Exception {
        setPrivateField(pontoService, "validacoesRegistrar", List.of(validacaoPonto));
        setPrivateField(pontoService, "validacoesAlterar", List.of(validacaoAlterarPonto));
        setPrivateField(pontoService, "validacoesFinalizar", List.of(validacaoFinalizarPonto));
    }

    @Test
    void registrarPonto_NewTimeLog_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNome("Test User");
        userDTO.setEmail("testuser@example.com");
        userDTO.setLogin("testuser");
        userDTO.setPassword("password123");
        userDTO.setSector("IT");
        userDTO.setJobTitle("Developer");
        userDTO.setRole("user");
        userDTO.setIsActive(true);

        BaterPonto dto = new BaterPonto("08:00:00");

        var user = User.toEntity(userDTO);
        var timelog = new TimeLog(user, LocalDate.now());


        when(tokenService.getSubject(any(String.class))).thenReturn("");
        when(userRepository.findByLogin(any(String.class))).thenReturn(Optional.of(user));
        when(timeLogsRepository.findByUserAndData(any(User.class), any(LocalDate.class))).thenReturn(Optional.empty());
        doNothing().when(validacaoPonto).validar(any(), any());
        when(timeLogsRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(pedidoPontoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PedidoPonto pedido = new PedidoPonto(timelog);

        when(pedidoPontoRepository.findPedidoPontoByPonto_Id(any())).thenReturn(Optional.of(pedido));

        pontoService.registrarPonto(dto, "");

        verify(timeLogsRepository).save(any());
        verify(pedidoPontoRepository).save(any());
    }

    @Test
    void registrarPonto_ExistingTimeLog_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNome("Test User");
        userDTO.setEmail("testuser@example.com");
        userDTO.setLogin("testuser");
        userDTO.setPassword("password123");
        userDTO.setSector("IT");
        userDTO.setJobTitle("Developer");
        userDTO.setRole("user");
        userDTO.setIsActive(true);

        var user = User.toEntity(userDTO);
        var timeLog = new TimeLog(user, LocalDate.now());
        BaterPonto dto = new BaterPonto("08:00:00");

        when(tokenService.getSubject(any(String.class))).thenReturn("login");
        when(userRepository.findByLogin(any(String.class))).thenReturn(Optional.of(user));
        when(timeLogsRepository.findByUserAndData(any(User.class), any(LocalDate.class))).thenReturn(Optional.of(timeLog));
        doNothing().when(validacaoPonto).validar(any(), any());
        when(timeLogsRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        PedidoPonto pedido = new PedidoPonto(timeLog);
        when(pedidoPontoRepository.findPedidoPontoByPonto_Id(any())).thenReturn(Optional.of(pedido));

        assertNotNull(pontoService.registrarPonto(dto, ""));
        verify(timeLogsRepository).save(any());
        verify(pedidoPontoRepository, never()).save(isA(PedidoPonto.class));
    }

    @Test
    void registrarPonto_PedidoNotFound_Throws() {
        String token = "token";
        String login = "user";
        BaterPonto dto = new BaterPonto("08:00:00");
        LocalDate today = LocalDate.now();

        var user = mock(easytime.srv.api.tables.User.class);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(timeLogsRepository.findByUserAndData(user, today)).thenReturn(Optional.empty());
        doNothing().when(validacaoPonto).validar(any(), any());
        when(timeLogsRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(pedidoPontoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(pedidoPontoRepository.findPedidoPontoByPonto_Id(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pontoService.registrarPonto(dto, token));
    }

    @Test
    void removerPonto_Success() {
        String token = "token";
        String login = "admin";
        var user = mock(easytime.srv.api.tables.User.class);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn("admin");
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLogsRepository.findById(1)).thenReturn(Optional.of(timeLog));

        pontoService.removerPonto(1, token);

        verify(timeLogsRepository).delete(timeLog);
    }

    @Test
    void removerPonto_NotAdmin_Throws() {
        String token = "token";
        String login = "user";
        var user = mock(easytime.srv.api.tables.User.class);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(user.getRole()).thenReturn("user");
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLogsRepository.findById(1)).thenReturn(Optional.of(timeLog));

        assertThrows(IllegalCallerException.class, () -> pontoService.removerPonto(1, token));
    }

    @Test
    void removerPonto_NotFound_Throws() {
        String token = "token";
        String login = "admin";
        var user = mock(easytime.srv.api.tables.User.class);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(timeLogsRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pontoService.removerPonto(1, token));
    }

    @Test
    void consultar_Success() {
        String token = "token";
        String login = "user";
        var user = mock(User.class);
        ConsultaPontosDto dto = mock(ConsultaPontosDto.class);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(dto.dtInicio()).thenReturn("01/01/2024");
        when(dto.dtFinal()).thenReturn("02/01/2024");
        LocalDate d1 = LocalDate.of(2024, 1, 1);
        LocalDate d2 = LocalDate.of(2024, 1, 2);
        // Simulate DateTimeUtil
        try (MockedStatic<DateTimeUtil> util = mockStatic(DateTimeUtil.class)) {
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("01/01/2024")).thenReturn(d1);
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("02/01/2024")).thenReturn(d2);
            TimeLog timeLog = mock(TimeLog.class);
            when(timeLog.getUser()).thenReturn(user);
            when(timeLogsRepository.findAllByUserAndDataBetween(user, d1, d2)).thenReturn(List.of(timeLog));
            assertFalse(pontoService.consultar(dto, token).isEmpty());
        }
    }

    @Test
    void consultar_DateInicioAfterFinal_Throws() {
        String token = "token";
        String login = "user";
        var user = mock(easytime.srv.api.tables.User.class);
        ConsultaPontosDto dto = mock(ConsultaPontosDto.class);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(dto.dtInicio()).thenReturn("02/01/2024");
        when(dto.dtFinal()).thenReturn("01/01/2024");
        LocalDate d1 = LocalDate.of(2024, 2, 1);
        LocalDate d2 = LocalDate.of(2024, 1, 1);
        try (MockedStatic<DateTimeUtil> util = mockStatic(DateTimeUtil.class)) {
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("02/01/2024")).thenReturn(d1);
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("01/01/2024")).thenReturn(d2);
            assertThrows(IllegalArgumentException.class, () -> pontoService.consultar(dto, token));
        }
    }

    @Test
    void consultar_EmptyResult_Throws() {
        String token = "token";
        String login = "user";
        var user = mock(easytime.srv.api.tables.User.class);
        ConsultaPontosDto dto = mock(ConsultaPontosDto.class);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(dto.dtInicio()).thenReturn("01/01/2024");
        when(dto.dtFinal()).thenReturn("02/01/2024");
        LocalDate d1 = LocalDate.of(2024, 1, 1);
        LocalDate d2 = LocalDate.of(2024, 1, 2);
        try (MockedStatic<DateTimeUtil> util = mockStatic(DateTimeUtil.class)) {
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("01/01/2024")).thenReturn(d1);
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("02/01/2024")).thenReturn(d2);
            when(timeLogsRepository.findAllByUserAndDataBetween(user, d1, d2)).thenReturn(List.of());
            assertThrows(NotFoundException.class, () -> pontoService.consultar(dto, token));
        }
    }

    @Test
    void consultarPorId_EmptyResult_Throws() {
        Integer id = 1;

        when(pedidoPontoRepository.findPedidoPontoByPonto_IdAndTipoPedidoAndStatusPedido(id, PedidoPonto.Tipo.ALTERACAO, Status.PENDENTE)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> pontoService.consultarPedidoId(id));
    }

    @Test
    void consultarPorId_Success() {
        Integer id = 1;
        PedidoPonto pedidoPonto = mock(PedidoPonto.class);
        User user = mock(User.class);
        TimeLog timeLog = mock(TimeLog.class);
        AlterarPonto alterarPonto = mock(AlterarPonto.class);

        when(pedidoPontoRepository
                .findPedidoPontoByPonto_IdAndTipoPedidoAndStatusPedido(id, PedidoPonto.Tipo.ALTERACAO, Status.PENDENTE))
                .thenReturn(Optional.ofNullable(pedidoPonto));
        when(pedidoPonto.getUser()).thenReturn(user);
        when(pedidoPonto.getPonto()).thenReturn(timeLog);
        when(pedidoPonto.getAlteracaoPonto()).thenReturn(alterarPonto);
        AlterarPontoDto dto = new AlterarPontoDto(pedidoPonto);
        assertEquals(dto, pontoService.consultarPedidoId(id));
    }

    @Test
    void consultarPedidosPorPeriodo_Success() {
        String token = "token";
        String login = "user";
        User user = mock(User.class);
        ConsultaPontosDto dto = mock(ConsultaPontosDto.class);
        TimeLog timeLog = mock(TimeLog.class);
        PedidoPonto pedidoPonto = mock(PedidoPonto.class);

        when(tokenService.getSubject(token)).thenReturn(login);
        when(dto.dtInicio()).thenReturn("01/01/2024");
        when(dto.dtFinal()).thenReturn("02/01/2024");
        LocalDate d1 = LocalDate.of(2024, 1, 1);
        LocalDate d2 = LocalDate.of(2024, 1, 2);

        try (MockedStatic<DateTimeUtil> util = mockStatic(DateTimeUtil.class)) {
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("01/01/2024")).thenReturn(d1);
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("02/01/2024")).thenReturn(d2);

            when(pedidoPontoRepository.findAllByHorarioCriacaoBetween(d1.atTime(LocalTime.of(0,0)), d2.atTime(LocalTime.of(23, 59)))).thenReturn(List.of(pedidoPonto));

            when(pedidoPonto.getUser()).thenReturn(user);
            when(pedidoPonto.getPonto()).thenReturn(timeLog);
            when(pedidoPonto.getHorarioCriacao()).thenReturn(mock(LocalDateTime.class));
            when(timeLog.getStatusRegistro()).thenReturn(Status.PENDENTE);

            when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
            when(user.getRole()).thenReturn("admin");
            assertFalse(pontoService.listarPedidosPorPeriodo(dto, token).isEmpty());
        }
    }

    @Test
    void consultarPedidosPorPeriodo_DateInicioAfterFinal_Throws() {
        String token = "token";
        ConsultaPontosDto dto = mock(ConsultaPontosDto.class);

        when(dto.dtInicio()).thenReturn("01/01/2024");
        when(dto.dtFinal()).thenReturn("02/01/2024");
        LocalDate d1 = LocalDate.of(2024, 1, 1);
        LocalDate d2 = LocalDate.of(2024, 1, 2);

        try (MockedStatic<DateTimeUtil> util = mockStatic(DateTimeUtil.class)) {
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("02/01/2024")).thenReturn(d1);
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("01/01/2024")).thenReturn(d2);

            assertThrows(IllegalArgumentException.class, () -> pontoService.listarPedidosPorPeriodo(dto, token));
        }
    }

    @Test
    void consultarPedidosPorData_InvalidUser_Throws() {
        String token = "token";
        String login = "user";
        User user = mock(User.class);
        ConsultaPontosDto dto = mock(ConsultaPontosDto.class);

        when(tokenService.getSubject(token)).thenReturn(login);
        when(dto.dtInicio()).thenReturn("01/01/2024");
        when(dto.dtFinal()).thenReturn("02/01/2024");
        LocalDate d1 = LocalDate.of(2024, 1, 1);
        LocalDate d2 = LocalDate.of(2024, 1, 2);

        try (MockedStatic<DateTimeUtil> util = mockStatic(DateTimeUtil.class)) {
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("01/01/2024")).thenReturn(d1);
            util.when(() -> DateTimeUtil.convertUserDateToDBDate("02/01/2024")).thenReturn(d2);

            when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
            when(user.getRole()).thenReturn("user");
            assertThrows(InvalidUserException.class, () -> pontoService.listarPedidosPorPeriodo(dto, token));
        }
    }

    @Test
    void alterarPonto_Success() {
        String token = "token";
        String login = "user";
        var user = mock(easytime.srv.api.tables.User.class);
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(dto.idPonto()).thenReturn(1);
        TimeLog timeLog = mock(TimeLog.class);
        when(timeLogsRepository.findById(1)).thenReturn(Optional.of(timeLog));
        doNothing().when(validacaoAlterarPonto).validar(dto, timeLog, login);
        when(pedidoPontoRepository.save(any())).thenReturn(mock(PedidoPonto.class));

        assertDoesNotThrow(() -> pontoService.alterarPonto(dto, token));
    }

    @Test
    void alterarPonto_NotFound_Throws() {
        String token = "token";
        String login = "user";
        var user = mock(easytime.srv.api.tables.User.class);
        AlterarPontoDto dto = mock(AlterarPontoDto.class);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(dto.idPonto()).thenReturn(1);
        when(timeLogsRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pontoService.alterarPonto(dto, token));
    }

    @Test
    void aprovarPonto_Success() {
        String token = "token";
        String login = "gestor";
        PedidoPonto pedido = mock(PedidoPonto.class);
        TimeLog timeLog = mock(TimeLog.class);

        when(tokenService.getSubject(token)).thenReturn(login);
        when(pedidoPontoRepository.findById(1)).thenReturn(Optional.of(pedido));
        doNothing().when(validacaoFinalizarPonto).validar(pedido, login);
        when(pedido.getTipoPedido()).thenReturn(PedidoPonto.Tipo.REGISTRO);
        when(pedido.getPonto()).thenReturn(timeLog);
        when(pedidoPontoRepository.save(any())).thenReturn(pedido);

        assertDoesNotThrow(() -> pontoService.aprovarPonto(1, token));
    }

    @Test
    void aprovarPonto_NotFound_Throws() {
        String token = "token";
        String login = "gestor";
        when(tokenService.getSubject(token)).thenReturn(login);
        when(pedidoPontoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pontoService.aprovarPonto(1, token));
    }

    @Test
    void aprovarPonto_Alteracao_Aprovado_ExecutesAlterarPonto() {
        String token = "token";
        String login = "gestor";
        int pedidoId = 1;

        PedidoPonto pedido = mock(PedidoPonto.class);
        TimeLog timeLog = mock(TimeLog.class);
        AlterarPonto alterarPonto = mock(AlterarPonto.class);

        when(tokenService.getSubject(token)).thenReturn(login);
        when(pedidoPontoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedido.getTipoPedido()).thenReturn(PedidoPonto.Tipo.ALTERACAO);
        when(pedido.getPonto()).thenReturn(timeLog);
        when(pedido.getAlteracaoPonto()).thenReturn(alterarPonto);

        doNothing().when(validacaoFinalizarPonto).validar(pedido, login);
        when(pedidoPontoRepository.save(any())).thenReturn(pedido);

        // Call the method
        pontoService.aprovarPonto(pedidoId, token);

        // Verify that alterarPonto and save were called
        verify(timeLog).alterarPonto(alterarPonto);
        verify(timeLogsRepository).save(timeLog);
    }

    @Test
    void reprovarPonto_Success() {
        String token = "token";
        String login = "gestor";
        PedidoPonto pedido = mock(PedidoPonto.class);
        TimeLog timeLog = mock(TimeLog.class);

        when(tokenService.getSubject(token)).thenReturn(login);
        when(pedidoPontoRepository.findById(1)).thenReturn(Optional.of(pedido));
        doNothing().when(validacaoFinalizarPonto).validar(pedido, login);
        when(pedido.getPonto()).thenReturn(timeLog);

        assertDoesNotThrow(() -> pontoService.reprovarPonto(1, token));
    }

    @Test
    void listarPontos_Success() {
        var user = mock(User.class);
        when(user.getLogin()).thenReturn("user");
        TimeLog timeLog = new TimeLog(user, LocalDate.now());
        when(timeLogsRepository.findAll()).thenReturn(List.of(timeLog));
        assertNotNull(pontoService.listarPontos());
    }

    @Test
    void listarPontos_Empty_Throws() {
        when(timeLogsRepository.findAll()).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> pontoService.listarPontos());
    }

    @Test
    void listarAllPedidos_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNome("Test User");
        userDTO.setEmail("testuser@example.com");
        userDTO.setLogin("testuser");
        userDTO.setPassword("password123");
        userDTO.setSector("IT");
        userDTO.setJobTitle("Developer");
        userDTO.setRole("user");
        userDTO.setIsActive(true);

        var user = User.toEntity(userDTO);
        var timelog = new TimeLog(user, LocalDate.now());

        var pedido = new PedidoPonto(timelog);

        when(pedidoPontoRepository.findAll()).thenReturn(List.of(pedido));
        assertFalse(pontoService.listarAllPedidos().isEmpty());
    }

    @Test
    void listarPedidoPendentes_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNome("Test User");
        userDTO.setEmail("testuser@example.com");
        userDTO.setLogin("testuser");
        userDTO.setPassword("password123");
        userDTO.setSector("IT");
        userDTO.setJobTitle("Developer");
        userDTO.setRole("user");
        userDTO.setIsActive(true);

        var user = User.toEntity(userDTO);
        var timelog = new TimeLog(user, LocalDate.now());

        var pedido = new PedidoPonto(timelog);
        when(pedidoPontoRepository.findAllByStatusPedido(Status.PENDENTE)).thenReturn(List.of(pedido));
        assertFalse(pontoService.listarPedidosPorStatus(Status.PENDENTE).isEmpty());
    }

    @Test
    void listarPedidoAprovados_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNome("Test User");
        userDTO.setEmail("testuser@example.com");
        userDTO.setLogin("testuser");
        userDTO.setPassword("password123");
        userDTO.setSector("IT");
        userDTO.setJobTitle("Developer");
        userDTO.setRole("user");
        userDTO.setIsActive(true);

        var user = User.toEntity(userDTO);
        var timelog = new TimeLog(user, LocalDate.now());

        var pedido = new PedidoPonto(timelog);
        when(pedidoPontoRepository.findAllByStatusPedido(Status.APROVADO)).thenReturn(List.of(pedido));
        assertFalse(pontoService.listarPedidosPorStatus(Status.APROVADO).isEmpty());
    }

    @Test
    void listarPedidoRejeitados_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNome("Test User");
        userDTO.setEmail("testuser@example.com");
        userDTO.setLogin("testuser");
        userDTO.setPassword("password123");
        userDTO.setSector("IT");
        userDTO.setJobTitle("Developer");
        userDTO.setRole("user");
        userDTO.setIsActive(true);

        var user = User.toEntity(userDTO);
        var timelog = new TimeLog(user, LocalDate.now());

        var pedido = new PedidoPonto(timelog);
        when(pedidoPontoRepository.findAllByStatusPedido(Status.REJEITADO)).thenReturn(List.of(pedido));
        assertFalse(pontoService.listarPedidosPorStatus(Status.REJEITADO).isEmpty());
    }

    @Test
    void getUserAndLogin_InvalidUser_Throws() throws Exception {
        String token = "token";
        String login = "user";
        when(tokenService.getSubject(token)).thenReturn(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        var method = PontoService.class.getDeclaredMethod("getUserAndLogin", String.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> method.invoke(pontoService, token));
        assertTrue(ex.getCause() instanceof InvalidUserException);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

}