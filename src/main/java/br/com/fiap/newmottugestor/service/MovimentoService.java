package br.com.fiap.newmottugestor.service; // Ou onde seus serviços ficam

import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.mongo.model.MovimentacaoDocument;
import br.com.fiap.newmottugestor.mongo.repository.MovimentacaoDocumentRepository;
import br.com.fiap.newmottugestor.oracle.model.*;
import br.com.fiap.newmottugestor.oracle.repository.*;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class MovimentoService {

    private static final Logger logger = LoggerFactory.getLogger(MovimentoService.class);

    @Autowired
    private MovimentacaoDocumentRepository mongoRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MovimentacaoLogRepository logRepo;

    @Autowired
    private MotoRepository motoRepo;

    @Autowired
    private UserRepository userRepo; // Mude para 'UserRepository' se o nome for esse

    @Autowired
    private PatioRepository patioRepo;

    @Autowired
    private LeitorRepository leitorRepo;

    @Autowired
    private TipoMovimentacaoRepository tipoMovRepo;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Moto registrarNovaMovimentacao(Long idMoto, Long idUsuario, Long idPatio, Long idLeitor, TipoMovimento tipoMov) {

        Moto moto = motoRepo.findById(idMoto).orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        User user = userRepo.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Patio patio = patioRepo.findById(idPatio).orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
        Leitor leitor = leitorRepo.findById(idLeitor).orElseThrow(() -> new RuntimeException("Leitor não encontrado"));

        TipoMovimentacao tipo = tipoMovRepo.findByNome(tipoMov.getCodigo())
                .orElseThrow(() -> new RuntimeException("Tipo de Movimento não encontrado no Oracle DB"));

        MovimentacaoDocument doc = MovimentacaoDocument.builder()
                .dataEvento(LocalDate.now())
                .idMotoOracle(moto.getIdMoto())
                .placaMoto(moto.getPlaca())
                .modeloMoto(moto.getModelo())
                .nomeUsuario(user.getName())
                .emailUsuario(user.getEmail())
                .nomePatio(patio.getNome())
                .nomeLeitor(leitor.getNome())
                .tipoMovimento(tipoMov)
                .build();

        MovimentacaoDocument docSalvo = mongoRepo.save(doc);
        String idMongo = docSalvo.getId();

        MovimentacaoLog log = MovimentacaoLog.builder()
                .idMongoMovimentacao(idMongo)
                .moto(moto)
                .user(user)
                .tipoMovimentacao(tipo)
                .build();

        if (tipoMov == TipoMovimento.MANUTENCAO) {
            motoRepo.atualizarServicoMoto(idMoto, "Manutenção");
            entityManager.flush();
            entityManager.refresh(moto);
        } else if (tipoMov == TipoMovimento.VISTORIA) {
            motoRepo.atualizarServicoMoto(idMoto, "Vistoria");
            entityManager.flush();
            entityManager.refresh(moto);
        }
        else {
            motoRepo.atualizarServicoMoto(idMoto, "Em aguardo...");
            entityManager.flush();
            entityManager.refresh(moto);
        }

        logRepo.save(log);

        System.out.println("MOVIMENTAÇÃO REGISTRADA COM SUCESSO! ID Mongo: " + idMongo);
        return moto;
    }
    public Page<MovimentacaoDocument> buscarComFiltros(Long patioId, Long leitorId, TipoMovimento tipo,
                                                       LocalDate data, String modelo, String placa,
                                                       Pageable pageable) {

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();
        if (patioId != null) {
            patioRepo.findById(patioId).ifPresent(patio ->
                    criteriaList.add(Criteria.where("nomePatio").is(patio.getNome()))
            );
        }
        if (leitorId != null) {
            leitorRepo.findById(leitorId).ifPresent(leitor ->
                    criteriaList.add(Criteria.where("nomeLeitor").is(leitor.getNome()))
            );
        }
        if (tipo != null) {
            criteriaList.add(Criteria.where("tipoMovimento").is(tipo));
        }
        if (data != null) {
            criteriaList.add(Criteria.where("dataEvento").is(data));
        }
        if (modelo != null && !modelo.isBlank()) {
            String escapedModelo = Pattern.quote(modelo);
            String exactMatchPattern = "^" + escapedModelo + "$";
            criteriaList.add(Criteria.where("modeloMoto").regex(exactMatchPattern, "i"));
        }
        if (placa != null && !placa.isBlank()) {
            criteriaList.add(Criteria.where("placaMoto").regex(placa, "i"));
        }
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, MovimentacaoDocument.class);

        query.with(pageable);
        List<MovimentacaoDocument> resultados = mongoTemplate.find(query, MovimentacaoDocument.class);
        return new PageImpl<>(resultados, pageable, total);
    }

    public List<MovimentacaoDocument> getUltimosMovimentosPorPatio(Long patioId) {
        Patio patio = patioRepo.findById(patioId)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado para buscar movimentos"));
        return mongoRepo.findTop5ByNomePatioOrderByDataEventoDescIdDesc(patio.getNome());
    }

    public List<MovimentacaoDocument> getMovimentosByPatio(Long patioId) {
        Patio patio = patioRepo.findById(patioId)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado para buscar movimentos"));
        return mongoRepo.findByNomePatio(patio.getNome());
    }

    public List<MovimentacaoDocument> getMovimentosByLeitor(Long leitorId) {
        Leitor leitor = leitorRepo.findById(leitorId)
                .orElseThrow(() -> new RuntimeException("Leitor não encontrado para buscar movimentos"));
        return mongoRepo.findByNomeLeitor(leitor.getNome());
    }

    public void deleteAll(List<MovimentacaoDocument> movimentacaoDocuments) {
        mongoRepo.deleteAll(movimentacaoDocuments);
    }
}