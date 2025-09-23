package br.com.fiap.newmottugestor.patio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patio")
@RequiredArgsConstructor
public class PatioController {

    private final PatioService patioService;
    private final MessageSource messageSource;

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
        var message = messageSource.getMessage("patio.create.success", null, LocaleContextHolder.getLocale());
        patioService.save(patio);
        redirect.addFlashAttribute("message", message);
        return "redirect:/patio"; //301
    }
}
