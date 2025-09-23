package br.com.fiap.newmottugestor.movimento;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movimento")
public class MovimentoController {

    private final MovimentoService movimentoService;

    public MovimentoController(MovimentoService movimentoService) {
        this.movimentoService = movimentoService;
    }

    @GetMapping
    public String index(Model model) {
        var movimentacoes = movimentoService.getAllMovimento();
        model.addAttribute("movimentacoes", movimentacoes);
        return "index";
    }
}
