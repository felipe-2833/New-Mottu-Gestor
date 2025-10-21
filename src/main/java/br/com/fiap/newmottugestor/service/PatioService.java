package br.com.fiap.newmottugestor.service;

import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.oracle.model.Patio;
import br.com.fiap.newmottugestor.oracle.repository.PatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatioService {

    private final PatioRepository patioRepository;
    private final MessageHelper messageHelper;

    public PatioService(PatioRepository patioRepository, MessageHelper messageHelper) {
        this.patioRepository = patioRepository;
        this.messageHelper = messageHelper;
    }

    public List<Patio> getAllPatio() {
        return patioRepository.findAll();
    }

    public Patio save(Patio patio) {
        return patioRepository.save(patio);
    }

    public void deleteById(Long id) {
        patioRepository.delete(getPatio(id));
    }

    public Patio getPatio(Long id) {
        return patioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageHelper.get("patio.notfound"))
        );
    }
}
