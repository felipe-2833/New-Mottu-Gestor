package br.com.fiap.newmottugestor.Leitor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("leitor")
public class LeitorController {
    private final LeitorService leitorService;

    public LeitorController(LeitorService leitorService) {
        this.leitorService = leitorService;
    }

    @GetMapping
    public String index(Model model){
        var leitores = leitorService.getAllLeitor();
        model.addAttribute("leitors", leitores);
        return "index";
    }
}
