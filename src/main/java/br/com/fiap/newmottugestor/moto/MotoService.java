package br.com.fiap.newmottugestor.moto;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.movimento.MovimentoSpecification;
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
public class MotoService {

    private final MotoRepository motoRepository;
    private final PatioService patioService;
    private final MessageHelper messageHelper;

    public MotoService(MotoRepository motoRepository, MessageHelper messageHelper, PatioService patioService) {
        this.motoRepository = motoRepository;
        this.messageHelper = messageHelper;
        this.patioService = patioService;
    }

    public List<Moto> getAllMoto() {
        return motoRepository.findAll(Sort.by(Sort.Direction.DESC, "idMoto"));
    }

    public Moto save(Moto moto) {
        return motoRepository.save(moto);
    }

    public void deleteById(Long id) {
        motoRepository.delete(getMoto(id));
    }

    public List<Moto> getMotosByPatio(Long patioId) {
        Patio patio = patioService.getPatio(patioId);
        return motoRepository.findByLeitorPatio(patio);
    }

    public List<Moto> buscarComFiltros(Long leitorId, LocalDate data, String modelo, String placa) {

        List<Specification<Moto>> specs = new ArrayList<>();

        if (leitorId != null) specs.add(MotoSpecification.comLeitor(leitorId));
        if (data != null) specs.add(MotoSpecification.comData(data));
        if (modelo != null && !modelo.isBlank()) specs.add(MotoSpecification.comModelo(modelo));
        if (placa != null && !placa.isBlank()) specs.add(MotoSpecification.comPlaca(placa));

        Specification<Moto> finalSpec = specs.stream()
                .reduce((s1, s2) -> s1.and(s2))
                .orElse((root, query, cb) -> cb.conjunction());

        return motoRepository.findAll(finalSpec, Sort.by(Sort.Direction.DESC, "idMoto"));

    }


    public Moto getMoto(Long id) {
        return motoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageHelper.get("moto.notfound"))
        );
    }
}
