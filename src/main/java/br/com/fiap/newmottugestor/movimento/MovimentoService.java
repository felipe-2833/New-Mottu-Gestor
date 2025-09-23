package br.com.fiap.newmottugestor.movimento;

import br.com.fiap.newmottugestor.moto.MotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentoService {

    private final MovimentoRepository movimentoRepository;

    public MovimentoService(MovimentoRepository movimentoRepository) {
        this.movimentoRepository = movimentoRepository;
    }

    public List<Movimento> getAllMovimento(){
        return movimentoRepository.findAll();
    }
}
