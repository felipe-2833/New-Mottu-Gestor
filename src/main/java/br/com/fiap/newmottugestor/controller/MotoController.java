package br.com.fiap.newmottugestor.controller;

import br.com.fiap.newmottugestor.mongo.repository.MovimentacaoDocumentRepository;
import br.com.fiap.newmottugestor.oracle.model.Leitor;
import br.com.fiap.newmottugestor.oracle.repository.LeitorRepository;
import br.com.fiap.newmottugestor.oracle.repository.MovimentacaoLogRepository;
import br.com.fiap.newmottugestor.service.LeitorService;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.oracle.model.Moto;
import br.com.fiap.newmottugestor.oracle.model.Patio;
import br.com.fiap.newmottugestor.service.MotoService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

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
    private final MovimentacaoDocumentRepository mongoRepo;
    private final MovimentacaoLogRepository logRepo;

    @GetMapping
    public String index(@RequestParam(required = false) Long leitorId,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                        @RequestParam(required = false) String modelo,
                        @RequestParam(required = false) String placa,
                        @PageableDefault(size = 50, sort = "idMoto", direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, @AuthenticationPrincipal OAuth2User user) {

        Page<Moto> motoPage = motoService.buscarComFiltros(leitorId, data, modelo, placa, pageable);
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
        model.addAttribute("motoPage", motoPage);
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
                             @PageableDefault(size = 50, sort = "idMoto", direction = Sort.Direction.DESC) Pageable pageable,
                             Model model, @AuthenticationPrincipal OAuth2User user) {

        Leitor leitor = leitorService.getLeitor(leitorId);
        Patio patio = leitor.getPatio();
        Page<Moto> motoPage = motoService.buscarComFiltros(leitorId, data, modelo, placa, pageable);

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
        model.addAttribute("motoPage", motoPage);
        model.addAttribute("user", user);
        model.addAttribute("leitorId", leitorId);

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
        logRepo.deleteByMoto_IdMoto(id);
        mongoRepo.deleteAllByIdMotoOracle(id);
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
        logRepo.deleteByMoto_IdMoto(id);
        mongoRepo.deleteAllByIdMotoOracle(id);
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
