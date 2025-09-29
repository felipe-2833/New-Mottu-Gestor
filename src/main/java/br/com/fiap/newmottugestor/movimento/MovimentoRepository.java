package br.com.fiap.newmottugestor.movimento;

import br.com.fiap.newmottugestor.moto.Moto;
import br.com.fiap.newmottugestor.patio.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MovimentoRepository extends JpaRepository<Movimento,Long>, JpaSpecificationExecutor<Movimento> {
    List<Movimento> findByMotoIdMoto(Long motoId);

    List<Movimento> findByPatio(Patio patio);

    List<Movimento> findTop5ByPatioIdPatioOrderByDataEventoDescIdMovimentDesc(Long patioId);
}
