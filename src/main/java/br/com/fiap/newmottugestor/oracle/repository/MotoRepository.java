package br.com.fiap.newmottugestor.oracle.repository;

import br.com.fiap.newmottugestor.oracle.model.Leitor;
import br.com.fiap.newmottugestor.oracle.model.Moto;
import br.com.fiap.newmottugestor.oracle.model.Patio;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MotoRepository extends JpaRepository<Moto,Long>, JpaSpecificationExecutor<Moto> {
    List<Moto> findByLeitorPatio(Patio patio);

    List<Moto> findByLeitor(Leitor leitor);

    @Procedure(procedureName = "PCK_GESTAO_FROTA.PRC_ATUALIZAR_SERVICO_MOTO")
    @Modifying
    void atualizarServicoMoto(
            @Param("p_id_moto") Long idMoto,
            @Param("p_nova_descricao") String novaDescricao
    );
    @Procedure(procedureName = "PCK_GESTAO_FROTA.PRC_CONTAR_MOTOS_POR_PATIO", outputParameterName = "p_total_motos")
    Integer contarMotosPorPatio(
            @Param("p_id_patio") Long idPatio
    );
}
