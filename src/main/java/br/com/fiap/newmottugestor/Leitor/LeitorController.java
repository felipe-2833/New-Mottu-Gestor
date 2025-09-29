package br.com.fiap.newmottugestor.Leitor;

import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.moto.Moto;
import br.com.fiap.newmottugestor.moto.MotoService;
import br.com.fiap.newmottugestor.movimento.Movimento;
import br.com.fiap.newmottugestor.movimento.MovimentoService;
import br.com.fiap.newmottugestor.patio.Patio;
import br.com.fiap.newmottugestor.patio.PatioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("patio-leitor")
@RequiredArgsConstructor
public class LeitorController {
    private final LeitorService leitorService;
    private final MovimentoService movimentoService;
    private final MotoService motoService;
    private final PatioService patioService;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index(@RequestParam(required = false) Long patioId, Model model, @AuthenticationPrincipal OAuth2User user) {
        if (patioId != null) {
            Patio patio = patioService.getPatio(patioId);
            List<Leitor> leitores = leitorService.getLeitorsByPatio(patioId);
            List<Movimento> ultimosMovimentos = movimentoService.getUltimosMovimentosPorPatio(patioId);
            List<Movimento> movimentosPatio = movimentoService.getMovimentosByPatio(patioId);

            model.addAttribute("movimentosPatio", movimentosPatio);
            model.addAttribute("ultimosMovimentos", ultimosMovimentos);
            model.addAttribute("leitores", leitores);
            model.addAttribute("patioSelecionado", patio);
        } else {
            model.addAttribute("leitores", leitorService.getAllLeitor());
        }
        List<Moto> motos = motoService.getMotosByPatio(patioId);

        model.addAttribute("motos", motos);
        model.addAttribute("statusList", TipoStatus.values());
        model.addAttribute("user", user);
        return "patio-leitor";
    }

    @GetMapping("/form-leitor")
    public String form(@RequestParam Long patioId, Model model, @AuthenticationPrincipal OAuth2User user){
        Patio patio = patioService.getPatio(patioId);
        model.addAttribute("patio", patio);
        Leitor leitor = new Leitor();
        leitor.setStatus(TipoStatus.ATIVO);
        model.addAttribute("leitor", leitor);
        model.addAttribute("user", user);

        return "form-leitor";
    }

    @PostMapping("/form-leitor")
    public String create(@Valid Leitor leitor, RedirectAttributes redirect, BindingResult result, @RequestParam Long patioId ){
        if(result.hasErrors()) return "form-leitor";

        Patio patio = patioService.getPatio(patioId);
        leitor.setPatio(patio);
        leitorService.save(leitor);
        redirect.addAttribute("patioId", patioId);
        redirect.addFlashAttribute("message", messageHelper.get("leitor.create.success"));
        return "redirect:/patio-leitor";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect, @RequestParam Long patioId ){
        leitorService.deleteById(id);
        redirect.addAttribute("patioId", patioId);
        redirect.addFlashAttribute("message", messageHelper.get("leitor.delete.success"));
        return "redirect:/patio-leitor";
    }

    @PutMapping("/rename/{id}")
    public String rename(@PathVariable Long id, @RequestParam String novoNome, RedirectAttributes redirect, @RequestParam Long patioId) {
        leitorService.rename(id, novoNome);
        redirect.addAttribute("patioId", patioId);
        redirect.addFlashAttribute("message", messageHelper.get("leitor.rename.success"));
        return "redirect:/patio-leitor";
    }

    @PutMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam TipoStatus novoStatus,
                               @RequestParam Long patioId,
                               RedirectAttributes redirect) {
        leitorService.updateStatus(id, novoStatus);
        redirect.addAttribute("patioId", patioId);
        return "redirect:/patio-leitor";
    }
}
