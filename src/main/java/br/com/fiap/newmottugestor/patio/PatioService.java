package br.com.fiap.newmottugestor.patio;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatioService {

    private final  PatioRepository patioRepository;

    public PatioService(PatioRepository patioRepository) {
        this.patioRepository = patioRepository;
    }

    public List<Patio> getAllPatio() {
        return patioRepository.findAll();
    }

    public Patio save(Patio patio) {
        return patioRepository.save(patio);
    }
}
