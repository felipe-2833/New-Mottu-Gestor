package br.com.fiap.newmottugestor.movimento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MovimentoRepository extends JpaRepository<Movimento,Long>, JpaSpecificationExecutor<Movimento> {
    List<Movimento> findByMotoIdMoto(Long motoId);
}
