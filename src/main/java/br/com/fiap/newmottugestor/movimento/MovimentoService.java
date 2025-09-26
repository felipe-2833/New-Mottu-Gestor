package br.com.fiap.newmottugestor.movimento;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimentoService {

    private final MovimentoRepository movimentoRepository;
    private final MessageHelper messageHelper;

    public MovimentoService(MovimentoRepository movimentoRepository, MessageHelper messageHelper) {
        this.movimentoRepository = movimentoRepository;
        this.messageHelper = messageHelper;
    }

    public List<Movimento> getAllMovimento(){
        return movimentoRepository.findAll();
    }

    public Movimento save(Movimento movimento) {
        return movimentoRepository.save(movimento);
    }

    public void deleteById(Long id) {
        movimentoRepository.delete(getMovimento(id));
    }

    public List<Movimento> buscarComFiltros(Long patioId, Long leitorId, TipoMovimento tipo,
                                            LocalDate data, String modelo) {

        List<Specification<Movimento>> specs = new ArrayList<>();

        if (patioId != null) specs.add(MovimentoSpecification.comPatio(patioId));
        if (leitorId != null) specs.add(MovimentoSpecification.comLeitor(leitorId));
        if (tipo != null) specs.add(MovimentoSpecification.comTipo(tipo));
        if (data != null) specs.add(MovimentoSpecification.comData(data));
        if (modelo != null && !modelo.isBlank()) specs.add(MovimentoSpecification.comModelo(modelo));

        Specification<Movimento> finalSpec = specs.stream()
                .reduce((s1, s2) -> s1.and(s2))
                .orElse((root, query, cb) -> cb.conjunction());

        return movimentoRepository.findAll(
                finalSpec,
                Sort.by(Sort.Direction.DESC, "dataEvento")
                        .and(Sort.by(Sort.Direction.DESC, "idMoviment"))
        );

    }


    public Movimento getMovimento(Long id) {
        return movimentoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageHelper.get("movimento.notfound"))
        );
    }
}
