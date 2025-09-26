package br.com.fiap.newmottugestor.moto;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.Leitor.LeitorRepository;
import br.com.fiap.newmottugestor.Leitor.LeitorService;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.movimento.Movimento;
import br.com.fiap.newmottugestor.movimento.MovimentoRepository;
import br.com.fiap.newmottugestor.patio.Patio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/moto")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;
    private final LeitorRepository leitorRepository;
    private final LeitorService leitorService;
    private final MessageHelper messageHelper;
    private final MovimentoRepository movimentoRepository;

    @GetMapping
    public String index(@RequestParam(required = false) Long leitorId,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                        @RequestParam(required = false) String modelo,@RequestParam(required = false) String placa, Model model, @AuthenticationPrincipal OAuth2User user) {
        List<Moto> motos = motoService.buscarComFiltros(leitorId, data, modelo, placa);
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

    @GetMapping("/leitor-moto/{leitorId}")
    public String leitorMoto(@PathVariable Long leitorId,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                             @RequestParam(required = false) String modelo,
                             @RequestParam(required = false) String placa,
                             Model model, @AuthenticationPrincipal OAuth2User user) {
        Leitor leitor = leitorService.getLeitor(leitorId);
        Patio patio = leitor.getPatio();
        List<Moto> motos = motoService.buscarComFiltros(leitorId, data, modelo, placa);

        var leitores = leitorService.getAllLeitor().stream()
                .filter(l -> l.getStatus().equals(TipoStatus.ATIVO))
                .toList();

        List<String> modelos = List.of(
                "Mottu Sport",
                "Mottu Sport ESD",
                "Mottu E (elétrica)",
                "Mottu Pop"
        );
        List<TipoMovimento> tiposMovimento = Arrays.stream(TipoMovimento.values())
                .filter(tm -> tm != TipoMovimento.ENTRADA)
                .toList();

        model.addAttribute("tiposMovimento", tiposMovimento);
        model.addAttribute("patioSelecionado", patio);
        model.addAttribute("modelos", modelos);
        model.addAttribute("leitores", leitores);
        model.addAttribute("motos", motos);
        model.addAttribute("user", user);

        return "leitor-moto";
    }


    @PostMapping("/form-moto")
    public String create(@Valid Moto moto, RedirectAttributes redirect, BindingResult result ){
        if(result.hasErrors()) return "form-moto";
        motoService.save(moto);
        redirect.addFlashAttribute("message", messageHelper.get("moto.create.success"));
        return "redirect:/moto";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect ){
        List<Movimento> movimentos = movimentoRepository.findByMotoIdMoto(id);
        if (!movimentos.isEmpty()) {
            movimentoRepository.deleteAll(movimentos);
        }
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

    @DeleteMapping("/leitor-moto/{id}")
    public String deleteMotoLeitor(@PathVariable Long id, RedirectAttributes redirect ){
        List<Movimento> movimentos = movimentoRepository.findByMotoIdMoto(id);
        if (!movimentos.isEmpty()) {
            movimentoRepository.deleteAll(movimentos);
        }

        Moto moto = motoService.getMoto(id);
        Leitor leitor = moto.getLeitor();

        motoService.deleteById(id);
        redirect.addFlashAttribute("message", messageHelper.get("moto.delete.success"));
        return "redirect:/moto/leitor-moto/"  + leitor.getIdLeitor();
    }

    @GetMapping("/leitor-moto/{id}/edit")
    public String editMotoLeitor(@PathVariable Long id, Model model, @AuthenticationPrincipal OAuth2User user) {
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
        return "form-moto-leitor";
    }

    @PostMapping("/leitor-moto/{id}")
    public String updateMotoLeitor(@PathVariable Long id, @Valid Moto moto, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {moto.setIdMoto(id); return "form-moto-leitor";}
        Moto antigaMoto = motoService.getMoto(id);
        Leitor leitor = antigaMoto.getLeitor();
        moto.setIdMoto(id);
        moto.setLeitor(leitor);
        motoService.save(moto);

        redirect.addFlashAttribute("message", messageHelper.get("moto.update.success"));
        return "redirect:/moto/leitor-moto/" + leitor.getIdLeitor();

    }
}
