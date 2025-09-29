package br.com.fiap.newmottugestor.movimento;
import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.Leitor.LeitorService;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.moto.Moto;
import br.com.fiap.newmottugestor.patio.Patio;
import br.com.fiap.newmottugestor.patio.PatioService;
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
    private final PatioService patioService;
    private final MessageHelper messageHelper;
    private final LeitorService leitorService;

    public MovimentoService(MovimentoRepository movimentoRepository, MessageHelper messageHelper, PatioService patioService, LeitorService leitorService) {
        this.movimentoRepository = movimentoRepository;
        this.messageHelper = messageHelper;
        this.patioService = patioService;
        this.leitorService = leitorService;
    }

    public Movimento save(Movimento movimento) {
        return movimentoRepository.save(movimento);
    }

    public List<Movimento> buscarComFiltros(Long patioId, Long leitorId, TipoMovimento tipo,
                                            LocalDate data, String modelo, String placa) {

        List<Specification<Movimento>> specs = new ArrayList<>();

        if (patioId != null) specs.add(MovimentoSpecification.comPatio(patioId));
        if (leitorId != null) specs.add(MovimentoSpecification.comLeitor(leitorId));
        if (tipo != null) specs.add(MovimentoSpecification.comTipo(tipo));
        if (data != null) specs.add(MovimentoSpecification.comData(data));
        if (modelo != null && !modelo.isBlank()) specs.add(MovimentoSpecification.comModelo(modelo));
        if (placa != null && !placa.isBlank()) specs.add(MovimentoSpecification.comPlaca(placa));

        Specification<Movimento> finalSpec = specs.stream()
                .reduce((s1, s2) -> s1.and(s2))
                .orElse((root, query, cb) -> cb.conjunction());

        return movimentoRepository.findAll(
                finalSpec,
                Sort.by(Sort.Direction.DESC, "dataEvento")
                        .and(Sort.by(Sort.Direction.DESC, "idMoviment"))
        );

    }

    public List<Movimento> getUltimosMovimentosPorPatio(Long patioId) {
        return movimentoRepository.findTop5ByPatioIdPatioOrderByDataEventoDescIdMovimentDesc(patioId);
    }

    public List<Movimento> getMovimentosByPatio(Long patioId) {
        Patio patio = patioService.getPatio(patioId);
        return movimentoRepository.findByPatio(patio);
    }

    public List<Movimento> getMovimentosByLeitor(Long leitorId) {
        Leitor leitor = leitorService.getLeitor(leitorId);
        return movimentoRepository.findByLeitor(leitor);
    }


    public Movimento getMovimento(Long id) {
        return movimentoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageHelper.get("movimento.notfound"))
        );
    }
}
