package br.com.fiap.newmottugestor.oracle.repository;

import br.com.fiap.newmottugestor.mongo.model.MovimentacaoDocument;
import br.com.fiap.newmottugestor.oracle.model.Leitor;
import br.com.fiap.newmottugestor.oracle.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MovimentoRepository extends JpaRepository<MovimentacaoDocument,Long>, JpaSpecificationExecutor<MovimentacaoDocument> {
    List<MovimentacaoDocument> findByMotoIdMoto(Long motoId);

    List<MovimentacaoDocument> findByPatio(Patio patio);

    List<MovimentacaoDocument> findByLeitor(Leitor leitor);

    List<MovimentacaoDocument> findTop5ByPatioIdPatioOrderByDataEventoDescIdMovimentDesc(Long patioId);
}
