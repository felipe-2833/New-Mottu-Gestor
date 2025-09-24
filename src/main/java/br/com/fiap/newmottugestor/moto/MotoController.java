package br.com.fiap.newmottugestor.moto;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.Leitor.LeitorRepository;
import br.com.fiap.newmottugestor.Leitor.LeitorService;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.patio.Patio;
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
@RequestMapping("/moto")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;
    private final LeitorRepository leitorRepository;
    private final MessageSource messageSource;
    private final MessageHelper messageHelper;

    @GetMapping
    public String index(@RequestParam(value = "sort", required = false) String sort, Model model) {
        var motos =  motoService.getAllMoto();

        if("modelo".equals(sort)) {
            motos = motoService.listarTodosOrdenadoPorModelo();
        } else if("dataCadastro".equals(sort)) {
            motos = motoService.listarTodosOrdenadoPorData();
        } else if("leitor".equals(sort)) {
            motos = motoService.listarTodosOrdenadoPorLeitor();
        } else {
            motos = motoService.getAllMoto();
        }

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
        List<Leitor> leitoresAtivos = leitorRepository.findByStatus(TipoStatus.ATIVO);
        model.addAttribute("modelos", modelos);
        model.addAttribute("leitores", leitoresAtivos);
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

    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect ){
        motoService.deleteById(id);
        redirect.addFlashAttribute("message", messageHelper.get("moto.delete.success"));
        return "redirect:/moto";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
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
        return "form-moto";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid Moto moto, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) return "form-moto";

        moto.setIdMoto(id);
        motoService.save(moto);

        redirect.addFlashAttribute("message", messageHelper.get("moto.update.success"));
        return "redirect:/moto";
    }
}
