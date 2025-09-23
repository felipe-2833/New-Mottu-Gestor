package br.com.fiap.newmottugestor.patio;

import br.com.fiap.newmottugestor.config.MessageHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patio")
@RequiredArgsConstructor
public class PatioController {

    private final PatioService patioService;
    private final MessageSource messageSource;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index(Model model) {
        var patios = patioService.getAllPatio();
        model.addAttribute("patios", patios);
        return "index";
    }

    @GetMapping("/form-patio")
    public String form(Model model){
        model.addAttribute("patio", new Patio());
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
        patioService.deleteById(id);
        redirect.addFlashAttribute("message", messageHelper.get("patio.delete.success"));
        return "redirect:/patio";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Patio patio = patioService.getPatio(id);
        model.addAttribute("patio", patio);
        return "form-patio";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid Patio patio, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) return "form-patio";

        patio.setId_patio(id);
        patioService.save(patio);

        redirect.addFlashAttribute("message", messageHelper.get("patio.update.success"));
        return "redirect:/patio";
    }

}
