package br.com.fiap.newmottugestor.movimento;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.Leitor.LeitorRepository;
import br.com.fiap.newmottugestor.Leitor.LeitorService;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.moto.Moto;
import br.com.fiap.newmottugestor.moto.MotoService;
import br.com.fiap.newmottugestor.patio.Patio;
import br.com.fiap.newmottugestor.patio.PatioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/movimento")
@RequiredArgsConstructor
public class MovimentoController {

    private final MovimentoService movimentoService;
    private final PatioService patioService;
    private final LeitorService leitorService;
    private final LeitorRepository leitorRepository;
    private final MotoService motoService;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index( @RequestParam(required = false) Long patioId,
                         @RequestParam(required = false) Long leitorId,
                         @RequestParam(required = false) TipoMovimento tipo,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                         @RequestParam(required = false) String modelo,
                         @AuthenticationPrincipal OAuth2User user,
                         Model model) {

        List<Movimento> movimentos = movimentoService.buscarComFiltros(patioId, leitorId, tipo, data, modelo);
        List<String> modelos = List.of(
                "Mottu Sport",
                "Mottu Sport ESD",
                "Mottu E (elétrica)",
                "Mottu Pop"
        );

        model.addAttribute("modelos", modelos);

        model.addAttribute("movimentos", movimentos);
        model.addAttribute("tipos", TipoMovimento.values());

        var patios = patioService.getAllPatio();
        var leitores = leitorService.getAllLeitor().stream()
                .filter(l -> l.getStatus().equals(TipoStatus.ATIVO)) // só ativos
                .toList();

        model.addAttribute("patios", patios);
        model.addAttribute("leitores", leitores);
        model.addAttribute("user", user);
        return "movimento";
    }

    @PostMapping("/registrar")
    public String registrarMovimento(@RequestParam Long motoId,
                                     @RequestParam TipoMovimento tipoMovimento,
                                     @RequestParam(required = false) Long novoLeitorId,
                                     RedirectAttributes redirect) {

        Moto moto = motoService.getMoto(motoId);
        Leitor leitorAtual = moto.getLeitor();
        Patio patioAtual = leitorAtual.getPatio();

        Movimento movimento = Movimento.builder()
                .moto(moto)
                .leitor(leitorAtual)
                .patio(patioAtual)
                .dataEvento(LocalDate.now())
                .tipoMovimento(tipoMovimento)
                .build();

        movimentoService.save(movimento);
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
                             RedirectAttributes redirect) {

        Moto moto = motoService.getMoto(motoId);
        Leitor novoLeitor = leitorService.getLeitor(leitorId);
        Patio patio = novoLeitor.getPatio();
        Movimento movimento = Movimento.builder()
                .moto(moto)
                .leitor(novoLeitor)
                .patio(patio)
                .dataEvento(LocalDate.now())
                .tipoMovimento(TipoMovimento.ENTRADA)
                .build();

        movimentoService.save(movimento);
        moto.setLeitor(novoLeitor);
        motoService.save(moto);

        redirect.addFlashAttribute("message", messageHelper.get("moto.create.success"));
        return "redirect:/moto";
    }
}
