package br.com.fiap.newmottugestor.service; // Ou onde seus serviços ficam

import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.mongo.model.MovimentacaoDocument;
import br.com.fiap.newmottugestor.mongo.repository.MovimentacaoDocumentRepository;
import br.com.fiap.newmottugestor.oracle.model.*;
import br.com.fiap.newmottugestor.oracle.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimentoService {

    @Autowired
    private MovimentacaoDocumentRepository mongoRepo;

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

    @Transactional
    public void registrarNovaMovimentacao(Long idMoto, Long idUsuario, Long idPatio, Long idLeitor, TipoMovimento tipoMov) {

        Moto moto = motoRepo.findById(idMoto).orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        User user = userRepo.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Patio patio = patioRepo.findById(idPatio).orElseThrow(() -> new RuntimeException("Pátio não encontrado"));
        Leitor leitor = leitorRepo.findById(idLeitor).orElseThrow(() -> new RuntimeException("Leitor não encontrado"));

        TipoMovimentacao tipo = tipoMovRepo.findByNome(tipoMov.name())
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

        logRepo.save(log);

        System.out.println("MOVIMENTAÇÃO REGISTRADA COM SUCESSO! ID Mongo: " + idMongo);
    }
    public List<MovimentacaoDocument> buscarComFiltros(Long patioId, Long leitorId, TipoMovimento tipo,
                                                       LocalDate data, String modelo, String placa) {
        if (placa != null && !placa.isBlank()) {
            return mongoRepo.findByPlacaMotoContainingIgnoreCase(placa);
        }

        if (modelo != null && !modelo.isBlank()) {
            return mongoRepo.findByModeloMotoContainingIgnoreCase(modelo);
        }
        return mongoRepo.findAll(Sort.by(Sort.Direction.DESC, "dataEvento"));
    }
}