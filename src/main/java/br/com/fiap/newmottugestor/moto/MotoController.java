package br.com.fiap.newmottugestor.moto;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.Leitor.LeitorRepository;
import br.com.fiap.newmottugestor.Leitor.LeitorService;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.movimento.Movimento;
import br.com.fiap.newmottugestor.patio.Patio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/moto")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;
    private final LeitorRepository leitorRepository;
    private final LeitorService leitorService;
    private final MessageSource messageSource;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index(@RequestParam(required = false) Long leitorId,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                        @RequestParam(required = false) String modelo, Model model, @AuthenticationPrincipal OAuth2User user) {
        List<Moto> motos = motoService.buscarComFiltros(leitorId, data, modelo);
        var leitores = leitorService.getAllLeitor().stream()
                .filter(l -> l.getStatus().equals(TipoStatus.ATIVO))
                .toList();
        List<String> modelos = List.of(
                "Mottu Sport",
                "Mottu Sport ESD",
                "Mottu E (elétrica)",
                "Mottu Pop"
        );
        model.addAttribute("modelos", modelos);
        model.addAttribute("leitores", leitores);
        model.addAttribute("motos", motos);
        model.addAttribute("user", user);
        return "moto";
    }

    @GetMapping("/form-moto")
    public String form(Model model, @AuthenticationPrincipal OAuth2User user){
        List<String> modelos = List.of(
                "Mottu Sport",
                "Mottu Sport ESD",
                "Mottu E (elétrica)",
                "Mottu Pop"
        );
        List<Leitor> leitoresAtivos = leitorRepository.findByStatus(TipoStatus.ATIVO);
        model.addAttribute("modelos", modelos);
        model.addAttribute("leitores", leitoresAtivos);
        model.addAttribute("moto", new Moto());
        model.addAttribute("user", user);
        return "form-moto";
    }

    @PostMapping("/form-moto")
    public String create(@Valid Moto moto, RedirectAttributes redirect, BindingResult result ){
        if(result.hasErrors()) return "form-moto";
        motoService.save(moto);
        redirect.addFlashAttribute("message", messageHelper.get("moto.create.success"));
        return "redirect:/moto"; //301
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect ){
        motoService.deleteById(id);
        redirect.addFlashAttribute("message", messageHelper.get("moto.delete.success"));
        return "redirect:/moto";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, @AuthenticationPrincipal OAuth2User user) {
        List<String> modelos = List.of(
                "Mottu Sport",
                "Mottu Sport ESD",
                "Mottu E (elétrica)",
                "Mottu Pop"
        );
        List<Leitor> leitoresAtivos = leitorRepository.findByStatus(TipoStatus.ATIVO);
        model.addAttribute("modelos", modelos);
        model.addAttribute("leitores", leitoresAtivos);
        Moto moto = motoService.getMoto(id);
        model.addAttribute("moto", moto);
        model.addAttribute("user", user);
        return "form-moto";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid Moto moto, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) return "form-moto";
        Moto antigaMoto = motoService.getMoto(id);
        Leitor leitor = antigaMoto.getLeitor();
        moto.setIdMoto(id);
        moto.setLeitor(leitor);
        motoService.save(moto);

        redirect.addFlashAttribute("message", messageHelper.get("moto.update.success"));
        return "redirect:/moto";
    }
}
