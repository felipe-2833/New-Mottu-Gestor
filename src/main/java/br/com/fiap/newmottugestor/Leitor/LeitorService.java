package br.com.fiap.newmottugestor.Leitor;

import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.patio.Patio;
import br.com.fiap.newmottugestor.patio.PatioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeitorService {
    private final LeitorRepository leitorRepository;
    private final PatioService patioService;
    private final MessageHelper messageHelper;

    public LeitorService(LeitorRepository leitorRepository,PatioService patioService, MessageHelper messageHelper) {
        this.leitorRepository = leitorRepository;
        this.patioService = patioService;
        this.messageHelper = messageHelper;
    }

    public List<Leitor> getAllLeitor() {
        return leitorRepository.findAll();
    }

    public List<Leitor> getLeitorsByPatio(Long patioId) {
        Patio patio = patioService.getPatio(patioId);
        return leitorRepository.findByPatio(patio);
    }

    public Leitor save(Leitor leitor) {
        return leitorRepository.save(leitor);
    }

    public void deleteById(Long id) {
        leitorRepository.delete(getLeitor(id));
    }

    public void rename(Long id, String novoNome) {
        var ficha = getLeitor(id);
        ficha.setNome(novoNome);
        leitorRepository.save(ficha);
    }

    public Leitor getLeitor(Long id) {
        return leitorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageHelper.get("leitor.notfound"))
        );
    }

    public void updateStatus(Long id, TipoStatus novoStatus) {
        var leitor = getLeitor(id);
        leitor.setStatus(novoStatus);
        leitorRepository.save(leitor);
    }
}
