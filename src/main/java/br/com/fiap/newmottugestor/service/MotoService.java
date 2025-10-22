package br.com.fiap.newmottugestor.service;

import br.com.fiap.newmottugestor.specification.MotoSpecification;
import br.com.fiap.newmottugestor.oracle.model.Leitor;
import br.com.fiap.newmottugestor.config.MessageHelper;
import br.com.fiap.newmottugestor.oracle.model.Moto;
import br.com.fiap.newmottugestor.oracle.model.Patio;
import br.com.fiap.newmottugestor.oracle.repository.MotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MotoService {

    private final MotoRepository motoRepository;
    private final PatioService patioService;
    private final MessageHelper messageHelper;
    private final LeitorService leitorService;

    public MotoService(MotoRepository motoRepository, MessageHelper messageHelper, PatioService patioService, LeitorService leitorService) {
        this.motoRepository = motoRepository;
        this.messageHelper = messageHelper;
        this.patioService = patioService;
        this.leitorService = leitorService;
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

    public Page<Moto> buscarComFiltros(Long leitorId, LocalDate data, String modelo, String placa,
                                       Pageable pageable) {

        List<Specification<Moto>> specs = new ArrayList<>();
        if (leitorId != null) specs.add(MotoSpecification.comLeitor(leitorId));
        if (data != null) specs.add(MotoSpecification.comData(data));
        if (modelo != null && !modelo.isBlank()) specs.add(MotoSpecification.comModelo(modelo));
        if (placa != null && !placa.isBlank()) specs.add(MotoSpecification.comPlaca(placa));

        Specification<Moto> finalSpec = specs.stream()
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());
        return motoRepository.findAll(finalSpec, pageable);
    }

    public List<Moto> getMotosByLeitor(Long leitorId) {
        Leitor leitor = leitorService.getLeitor(leitorId);
        return motoRepository.findByLeitor(leitor);
    }

    public int obterContagemMotosPorPatio(Long idPatio) {
        if (idPatio == null) {
            return 0;
        }
        Integer contagem = motoRepository.contarMotosPorPatio(idPatio);
        if (contagem == null || contagem < 0) {
            return 0;
        }
         return contagem;
    }


    public Moto getMoto(Long id) {
        return motoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageHelper.get("moto.notfound"))
        );
    }
}
