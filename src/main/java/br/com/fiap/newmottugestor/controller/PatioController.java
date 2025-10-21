package br.com.fiap.newmottugestor.controller;

import br.com.fiap.newmottugestor.oracle.model.Leitor;
import br.com.fiap.newmottugestor.oracle.repository.LeitorRepository;
import br.com.fiap.newmottugestor.service.LeitorService;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.oracle.model.Moto;
import br.com.fiap.newmottugestor.oracle.model.Patio;
import br.com.fiap.newmottugestor.service.MotoService;
import br.com.fiap.newmottugestor.mongo.model.MovimentacaoDocument;
import br.com.fiap.newmottugestor.oracle.repository.MovimentoRepository;
import br.com.fiap.newmottugestor.service.MovimentoService;
import br.com.fiap.newmottugestor.service.PatioService;
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
@RequestMapping("/patio")
@RequiredArgsConstructor
public class PatioController {

    private final PatioService patioService;
    private final MovimentoService movimentoService;
    private final MovimentoRepository  movimentoRepository;
    private final LeitorService leitorService;
    private final LeitorRepository  leitorRepository;
    private final MessageHelper messageHelper;
    private final MotoService motoService;

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal OAuth2User user) {
        var patios = patioService.getAllPatio();
        var motos = motoService.getAllMoto();

        model.addAttribute("motos", motos);
        model.addAttribute("patios", patios);
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/form-patio")
    public String form(Model model, @AuthenticationPrincipal OAuth2User user){
        model.addAttribute("patio", new Patio());
        model.addAttribute("user", user);
        return "form-patio";
    }

    @PostMapping("/form-patio")
    public String create(@Valid Patio patio, RedirectAttributes redirect, BindingResult result ){
        if(result.hasErrors()) return "form-patio";
        patioService.save(patio);
        redirect.addFlashAttribute("message", messageHelper.get("patio.create.success"));
        return "redirect:/patio"; //301
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect ){
        List<Moto> motos = motoService.getMotosByPatio(id);
        if (!motos.isEmpty()) {
            redirect.addFlashAttribute("message", messageHelper.get("patio.delete.denaid"));
            return "redirect:/patio";
        }
        List<MovimentacaoDocument> movimentacaoDocuments = movimentoService.getMovimentosByPatio(id);
        if (!movimentacaoDocuments.isEmpty()) {
            movimentoRepository.deleteAll(movimentacaoDocuments);
        }
        List<Leitor> leitores = leitorService.getLeitorsByPatio(id);
        if (!leitores.isEmpty()) {
            leitorRepository.deleteAll(leitores);
        }
        patioService.deleteById(id);
        redirect.addFlashAttribute("message", messageHelper.get("patio.delete.success"));
        return "redirect:/patio";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, @AuthenticationPrincipal OAuth2User user) {
        Patio patio = patioService.getPatio(id);
        model.addAttribute("patio", patio);
        model.addAttribute("user", user);
        return "form-patio";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid Patio patio, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) return "form-patio";

        patio.setIdPatio(id);
        patioService.save(patio);

        redirect.addFlashAttribute("message", messageHelper.get("patio.update.success"));
        return "redirect:/patio";
    }

}
