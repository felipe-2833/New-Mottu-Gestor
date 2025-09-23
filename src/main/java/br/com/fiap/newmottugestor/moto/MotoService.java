package br.com.fiap.newmottugestor.moto;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoService {

    private final MotoRepository motoRepository;

    public MotoService(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    public List<Moto> getAllMoto() {
        return motoRepository.findAll();
    }
}
