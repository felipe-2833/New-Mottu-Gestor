package br.com.fiap.newmottugestor.moto;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/moto")
public class MotoController {

    private final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    @GetMapping
    public String index(Model model) {
        var motos =  motoService.getAllMoto();
        model.addAttribute("motos", motos);
        return "index";
    }
}
