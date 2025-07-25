package easytime.srv.api.service;

import easytime.srv.api.infra.exceptions.InvalidUserException;
import easytime.srv.api.infra.security.TokenService;
import easytime.srv.api.model.Status;
import easytime.srv.api.model.pontos.*;
import easytime.srv.api.model.user.UserWithLogin;
import easytime.srv.api.tables.PedidoPonto;
import easytime.srv.api.tables.TimeLog;
import easytime.srv.api.tables.repositorys.PedidoPontoRepository;
import easytime.srv.api.tables.repositorys.TimeLogsRepository;
import easytime.srv.api.tables.repositorys.UserRepository;
import easytime.srv.api.util.DateTimeUtil;
import easytime.srv.api.validacoes.ponto.alterar_ponto.ValidacaoAlterarPonto;
import easytime.srv.api.validacoes.ponto.bater_ponto.ValidacaoPonto;
import easytime.srv.api.validacoes.ponto.finalizarPonto.ValidacaoFinalizarPonto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PontoService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeLogsRepository timeLogsRepository;

    @Autowired
    private PedidoPontoRepository pedidoPontoRepository;

    @Autowired
    private List<ValidacaoPonto> validacoesRegistrar;

    @Autowired
    private List<ValidacaoAlterarPonto> validacoesAlterar;

    @Autowired
    private List<ValidacaoFinalizarPonto> validacoesFinalizar;

    public TimeLogDto registrarPonto(BaterPonto dto, String token) {
        var userWithLogin = getUserAndLogin(token);

        LocalDate dataHoje = LocalDate.now();
        Time horaAgora = Time.valueOf(dto.horarioAtual());

        var timeLogOptional = timeLogsRepository.findByUserAndData(userWithLogin.user(), dataHoje);
        TimeLog timeLog;
        boolean isNew = false;

        if(timeLogOptional.isEmpty()){
            timeLog = new TimeLog(userWithLogin.user(), dataHoje);
            isNew = true;
        } else {
            timeLog = timeLogOptional.get();
        }

        validacoesRegistrar.forEach(v -> v.validar(timeLog, horaAgora));

        timeLog.setPonto(horaAgora);
        timeLogsRepository.save(timeLog);

        if(isNew){
            var pedidoPontoNew = new PedidoPonto(timeLog);
            pedidoPontoRepository.save(pedidoPontoNew);
        }

        var pedidoPonto = pedidoPontoRepository.findPedidoPontoByPonto_Id(timeLog.getId())
                .orElseThrow(() -> new NotFoundException("Pedido de ponto não encontrado."));

        return new TimeLogDto(pedidoPonto);
    }

    public void removerPonto(Integer id, String token) {
        var userWithLogin = getUserAndLogin(token);
        var timeLog = timeLogsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ponto não encontrado."));
        if (!userWithLogin.user().getRole().equals("admin")) {
            throw new IllegalCallerException("Você não tem permissão para remover pontos de outros usuários.");
        }
        timeLogsRepository.delete(timeLog);
    }

    public List<RegistroCompletoDto> consultar(ConsultaPontosDto dto, String token) {
        var userWithLogin = getUserAndLogin(token);

        var dateInicio = DateTimeUtil.convertUserDateToDBDate(dto.dtInicio());
        var dateFinal = DateTimeUtil.convertUserDateToDBDate(dto.dtFinal());
        if (dateInicio.isAfter(dateFinal)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data final.");
        }

        var response = timeLogsRepository.findAllByUserAndDataBetween(userWithLogin.user(), dateInicio, dateFinal);

        if (response.isEmpty()) {
            throw new NotFoundException("Nenhum ponto encontrado.");
        }

        return response.stream().map(
                timeLog -> {
                    return new RegistroCompletoDto(
                            timeLog, pedidoPontoRepository.existsByPonto_IdAndTipoPedidoAndStatusPedidoIn(
                                timeLog.getId(),
                                PedidoPonto.Tipo.ALTERACAO,
                                List.of(Status.PENDENTE)
                            )
                    );
                }
        ).collect(Collectors.toList());
    }

    public void alterarPonto(AlterarPontoDto dto, String token){
        var userWithLogin = getUserAndLogin(token);
        var timeLog = timeLogsRepository.findById(dto.idPonto())
                .orElseThrow(() -> new NotFoundException("ID de ponto não localizado. Verifique se o código está correto."));

        validacoesAlterar.forEach(validacao -> validacao.validar(dto, timeLog, userWithLogin.login()));

        var pedidoAlteracao = new PedidoPonto(dto, timeLog, userWithLogin.user());
        pedidoPontoRepository.save(pedidoAlteracao);
    }

    public String aprovarPonto(Integer id, String token) {
        var login = tokenService.getSubject(token);
        var pedidoPonto = pedidoPontoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido de ponto não localizado. Verifique se o código está correto."));

        validacoesFinalizar.forEach(v -> v.validar(pedidoPonto, login));

        return finalizarPedidoPonto(pedidoPonto, login, Status.APROVADO);
    }

    public String reprovarPonto(Integer idPedido, String token){
        var login = tokenService.getSubject(token);
        var pedidoPonto = pedidoPontoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido de ponto não localizado. Verifique se o código está correto."));

        validacoesFinalizar.forEach(v -> v.validar(pedidoPonto, login));

        return finalizarPedidoPonto(pedidoPonto, login, Status.REJEITADO);
    }

    public AlterarPontoDto consultarPedidoId(Integer idPonto){
        PedidoPonto pedidoPonto = pedidoPontoRepository.findPedidoPontoByPonto_IdAndTipoPedidoAndStatusPedido(idPonto, PedidoPonto.Tipo.ALTERACAO, Status.PENDENTE)
                .orElseThrow(() -> new NotFoundException("Pedido de ponto não localizado. Verifique se o código está correto."));

        return new AlterarPontoDto(pedidoPonto);
    }

    public List<RegistroCompletoDto> listarPontos() {
        var response = timeLogsRepository.findAll();

        if (response.isEmpty()) {
            throw new NotFoundException("Nenhum ponto encontrado.");
        }
        return response.stream().map(
                timeLog -> {
                    return new RegistroCompletoDto(
                            timeLog, pedidoPontoRepository.existsByPonto_IdAndTipoPedidoAndStatusPedidoIn(
                            timeLog.getId(),
                            PedidoPonto.Tipo.ALTERACAO,
                            List.of(Status.PENDENTE)
                    )
                    );
                }
        ).collect(Collectors.toList());
    }

    public List<PedidoPontoDto> listarAllPedidos() {
        return pedidoPontoRepository.findAll()
                .stream()
                .map(PedidoPontoDto::new)
                .collect(Collectors.toList());
    }

    public List<PedidoPontoDto> listarPedidosPorStatus(Status status) {
        return pedidoPontoRepository.findAllByStatusPedido(status)
                .stream()
                .map(PedidoPontoDto::new)
                .collect(Collectors.toList());
    }

    public List<PedidoPontoDto> listarPedidosPorTipo(PedidoPonto.Tipo tipo) {
        return pedidoPontoRepository.findAllByTipoPedido(tipo)
                .stream()
                .map(PedidoPontoDto::new)
                .collect(Collectors.toList());
    }

    public List<PedidoPontoDto> listarPedidosPorPeriodo(LocalDateTime dateInicio, LocalDateTime dateFinal) {
        return pedidoPontoRepository.findAllByHorarioCriacaoBetween(dateInicio, dateFinal)
                .stream()
                .map(PedidoPontoDto::new)
                .collect(Collectors.toList());
    }

    private UserWithLogin getUserAndLogin(String token) {
        var login = tokenService.getSubject(token);
        var user = userRepository.findByLogin(login)
                .orElseThrow(() -> new InvalidUserException("Usuário não encontrado."));
        return new UserWithLogin(user, login);
    }

    private String finalizarPedidoPonto(PedidoPonto pedidoPonto, String login, Status status) {
        pedidoPonto.setStatusPedido(status);
        pedidoPonto.setGestorLogin(login);
        pedidoPonto.setDataAprovacao(LocalDateTime.now());

        if (pedidoPonto.getTipoPedido() == PedidoPonto.Tipo.ALTERACAO && status == Status.APROVADO) {
            pedidoPonto.getPonto().alterarPonto(pedidoPonto.getAlteracaoPonto());
            pedidoPonto.getPonto().setStatusRegistro(status);
            timeLogsRepository.save(pedidoPonto.getPonto());
            pedidoPontoRepository.save(pedidoPonto);
            return "Ponto alterado com sucesso após aprovação do gestor.";
        } else if (pedidoPonto.getTipoPedido() == PedidoPonto.Tipo.ALTERACAO && status == Status.REJEITADO) {
            pedidoPontoRepository.save(pedidoPonto);
            return "Pedido de alteração de ponto reprovado pelo gestor.";
        }

        pedidoPonto.getPonto().setStatusRegistro(status);
        pedidoPontoRepository.save(pedidoPonto);

        return status == Status.APROVADO?
                "Registro ponto aprovado pelo gestor."
                :"Registro ponto reprovado pelo gestor.";
    }

    public List<PedidoPontoDto> filtrarPedidos(FiltroPedidos dto) {
        LocalDateTime dateInicio = null;
        LocalDateTime dateFinal = null;

        if (dto.dtInicio() != null && dto.dtFinal() != null) {
            dateInicio = DateTimeUtil.convertUserDateToDBDate(dto.dtInicio()).atTime(LocalTime.of(0,0));
            dateFinal = DateTimeUtil.convertUserDateToDBDate(dto.dtFinal()).atTime(LocalTime.of(23,59));

            if (dateInicio.isAfter(dateFinal)) {
                throw new IllegalArgumentException("A data de início não pode ser posterior à data final.");
            }
        }

        if ((dto.tipo() == null && dto.status() == null) && dto.dtInicio() != null && dto.dtFinal() != null) {
            return this.listarPedidosPorPeriodo(dateInicio, dateFinal);
        } else if ((dto.dtInicio() == null || dto.dtFinal() == null) && dto.tipo() == null && dto.status() != null) {
            return this.listarPedidosPorStatus(dto.status());
        } else if ((dto.dtInicio() == null || dto.dtFinal() == null) && dto.status() == null && dto.tipo() != null) {
            return this.listarPedidosPorTipo(dto.tipo());
        } else if ((dto.dtInicio() == null || dto.dtFinal() == null) && (dto.tipo() != null && dto.status() != null)) {
            return pedidoPontoRepository.findAllByTipoPedidoAndStatusPedido(dto.tipo(), dto.status())
                    .stream()
                    .map(PedidoPontoDto::new)
                    .collect(Collectors.toList());
        } else if (dto.status() == null && (dto.tipo() != null && dto.dtInicio() != null)) {
            return pedidoPontoRepository.findAllByTipoPedidoAndHorarioCriacaoBetween(dto.tipo(), dateInicio, dateFinal)
                    .stream()
                    .map(PedidoPontoDto::new)
                    .collect(Collectors.toList());
        } else if (dto.tipo() == null && (dto.status() != null && dto.dtInicio() != null)) {
            return pedidoPontoRepository.findAllByStatusPedidoAndHorarioCriacaoBetween(dto.status(), dateInicio, dateFinal)
                    .stream()
                    .map(PedidoPontoDto::new)
                    .collect(Collectors.toList());
        } else if (dto.status() != null && dto.tipo() != null && dto.dtInicio() != null && dto.dtFinal() != null) {
            return pedidoPontoRepository.findAllByStatusPedidoAndTipoPedidoAndHorarioCriacaoBetween(dto.status(), dto.tipo(), dateInicio, dateFinal)
                    .stream()
                    .map(PedidoPontoDto::new)
                    .collect(Collectors.toList());
        } else {
            return this.listarAllPedidos();
        }

    }
}
