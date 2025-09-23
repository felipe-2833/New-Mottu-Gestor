package br.com.fiap.newmottugestor.Leitor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeitorService {
    private final LeitorRepository leitorRepository;

    public LeitorService(LeitorRepository leitorRepository) {
        this.leitorRepository = leitorRepository;
    }

    public List<Leitor> getAllLeitor() {
        return leitorRepository.findAll();
    }
}
