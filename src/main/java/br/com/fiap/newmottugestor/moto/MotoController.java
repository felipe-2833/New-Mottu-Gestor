package br.com.fiap.newmottugestor.moto;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/moto")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;
    private final MessageSource messageSource;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index(Model model) {
        var motos =  motoService.getAllMoto();
        model.addAttribute("motos", motos);
        return "moto";
    }

    @GetMapping("/form-moto")
    public String form(Model model){
        List<String> modelos = List.of(
                "Mottu Sport",
                "Mottu Sport ESD",
                "Mottu E (elétrica)",
                "Mottu Pop"
        );
        model.addAttribute("modelos", modelos);
        model.addAttribute("moto", new Moto());
        return "form-moto";
    }

    @PostMapping("/form-moto")
    public String create(@Valid Moto moto, RedirectAttributes redirect, BindingResult result ){
        if(result.hasErrors()) return "form-moto";
        motoService.save(moto);
        redirect.addFlashAttribute("message", messageHelper.get("moto.create.success"));
        return "redirect:/moto"; //301
    }
}
