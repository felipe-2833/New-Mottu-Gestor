package br.com.fiap.newmottugestor.Leitor;

import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.patio.Patio;
import br.com.fiap.newmottugestor.patio.PatioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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
    private final PatioService patioService;
    private final MessageSource messageSource;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index(@RequestParam(required = false) Long patioId, Model model){
        if (patioId != null) {
            Patio patio = patioService.getPatio(patioId);
            List<Leitor> leitores = leitorService.getLeitorsByPatio(patioId);
            model.addAttribute("leitores", leitores);
            model.addAttribute("patioSelecionado", patio);
        } else {
            model.addAttribute("leitores", leitorService.getAllLeitor());
        }

        return "patio-leitor";
    }

    @GetMapping("/form-leitor")
    public String form(@RequestParam Long patioId, Model model){
        Patio patio = patioService.getPatio(patioId);
        model.addAttribute("patio", patio);
        Leitor leitor = new Leitor();
        //leitor.setStatus("ATIVO");
        model.addAttribute("leitor", leitor);

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
    public String delete(@PathVariable Long id, RedirectAttributes redirect ){
        leitorService.deleteById(id);
        redirect.addFlashAttribute("message", messageHelper.get("leitor.delete.success"));
        return "redirect:/patio-leitor";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
       Leitor leitor = leitorService.getLeitor(id);
        model.addAttribute("leitor", leitor);
        return "form-leitor";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid Leitor leitor, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) return "form-leitor";

        leitor.setId_leitor(id);
        leitorService.save(leitor);

        redirect.addFlashAttribute("message", messageHelper.get("leitor.update.success"));
        return "redirect:/patio-leitor";
    }
}
