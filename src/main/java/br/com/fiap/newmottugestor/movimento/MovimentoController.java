package br.com.fiap.newmottugestor.movimento;

import br.com.fiap.newmottugestor.Leitor.LeitorService;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.patio.PatioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/movimento")
@RequiredArgsConstructor
public class MovimentoController {

    private final MovimentoService movimentoService;
    private final PatioService patioService;
    private final LeitorService leitorService;

    @GetMapping
    public String index( @RequestParam(required = false) Long patioId,
                         @RequestParam(required = false) Long leitorId,
                         @RequestParam(required = false) TipoMovimento tipo,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                         @RequestParam(required = false) String modelo,
                         @AuthenticationPrincipal OAuth2User user,
                         Model model) {

        List<Movimento> movimentos = movimentoService.buscarComFiltros(patioId, leitorId, tipo, data, modelo);

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
}
