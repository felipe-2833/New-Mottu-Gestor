package br.com.fiap.newmottugestor.mongo.repository;

import br.com.fiap.newmottugestor.mongo.model.MovimentacaoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Note que estende MongoRepository, não JpaRepository
@Repository
public interface MovimentacaoDocumentRepository extends MongoRepository<MovimentacaoDocument, String> {
    List<MovimentacaoDocument> findByPlacaMotoContainingIgnoreCase(String placa);
    List<MovimentacaoDocument> findByModeloMotoContainingIgnoreCase(String modelo);
    void deleteAllByIdMotoOracle(Long idMotoOracle);

    List<MovimentacaoDocument> findByNomePatio(String nomePatio);

    List<MovimentacaoDocument> findTop5ByNomePatioOrderByDataEventoDescIdDesc(String nomePatio);

    List<MovimentacaoDocument> findByNomeLeitor(String nomeLeitor);
}