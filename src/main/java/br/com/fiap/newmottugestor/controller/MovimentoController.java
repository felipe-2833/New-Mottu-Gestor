package br.com.fiap.newmottugestor.controller;

import br.com.fiap.newmottugestor.mongo.model.MovimentacaoDocument;
import br.com.fiap.newmottugestor.oracle.model.Leitor;
import br.com.fiap.newmottugestor.service.*;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.oracle.model.Moto;
import br.com.fiap.newmottugestor.oracle.model.Patio;
import br.com.fiap.newmottugestor.oracle.model.User; // Importe o seu User do Oracle
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/movimento")
@RequiredArgsConstructor
public class MovimentoController {

    private final MovimentoService movimentoService;
    private final PatioService patioService;
    private final LeitorService leitorService;
    private final UserService userService;
    private final MotoService motoService;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index(@RequestParam(required = false) Long patioId,
                        @RequestParam(required = false) Long leitorId,
                        @RequestParam(required = false) TipoMovimento tipo,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                        @RequestParam(required = false) String modelo,
                        @RequestParam(required = false) String placa,
                        @PageableDefault(size = 50, sort = {"dataEvento", "id"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @AuthenticationPrincipal OAuth2User user,
                        Model model) {

        Page<MovimentacaoDocument> movimentoPage = movimentoService.buscarComFiltros(
                patioId, leitorId, tipo, data, modelo, placa, pageable // Passa o pageable
        );
        List<String> modelos = List.of(
                "Mottu Sport",
                "Mottu Sport ESD",
                "Mottu E (elétrica)",
                "Mottu Pop"
        );
        var patios = patioService.getAllPatio();
        var leitores = leitorService.getAllLeitor().stream()
                .filter(l -> l.getStatus().equals(TipoStatus.ATIVO))
                .toList();

        model.addAttribute("modelos", modelos);
        model.addAttribute("movimentoPage", movimentoPage);
        model.addAttribute("tipos", TipoMovimento.values());
        model.addAttribute("patios", patios);
        model.addAttribute("leitores", leitores);
        model.addAttribute("user", user);

        return "movimento";
    }

    @PostMapping("/registrar")
    public String registrarMovimento(@RequestParam Long motoId,
                                     @RequestParam TipoMovimento tipoMovimento,
                                     @RequestParam(required = false) Long novoLeitorId,
                                     @AuthenticationPrincipal OAuth2User principal,
                                     RedirectAttributes redirect) {

        var user = userService.register(principal);
        Moto moto = motoService.getMoto(motoId);
        Leitor leitorAtual = moto.getLeitor();
        Patio patioAtual = leitorAtual.getPatio();

        moto = movimentoService.registrarNovaMovimentacao(
                moto.getIdMoto(),
                user.getId(),
                patioAtual.getIdPatio(),
                leitorAtual.getIdLeitor(),
                tipoMovimento
        );

        moto.setLeitor(null);
        motoService.save(moto);

        if (tipoMovimento == TipoMovimento.REALOCACAO && novoLeitorId != null) {
            Leitor novoLeitor = leitorService.getLeitor(novoLeitorId);
            moto.setLeitor(novoLeitor);
            motoService.save(moto);
        }
        redirect.addFlashAttribute("message", messageHelper.get("movimento.create.success"));
        return "redirect:/moto/leitor-moto/" + leitorAtual.getIdLeitor();
    }

    @PostMapping("/entrada")
    public String darEntrada(@RequestParam Long motoId,
                             @RequestParam Long leitorId,
                             @AuthenticationPrincipal OAuth2User principal,
                             RedirectAttributes redirect) {

        var user = userService.register(principal);
        Leitor novoLeitor = leitorService.getLeitor(leitorId);
        Patio patio = novoLeitor.getPatio();
        Moto moto = motoService.getMoto(motoId);

        movimentoService.registrarNovaMovimentacao(
                moto.getIdMoto(),
                user.getId(),
                patio.getIdPatio(),
                novoLeitor.getIdLeitor(),
                TipoMovimento.ENTRADA
        );

        moto.setLeitor(novoLeitor);
        motoService.save(moto);

        redirect.addFlashAttribute("message", messageHelper.get("movimento.create.success"));
        return "redirect:/moto";
    }
}