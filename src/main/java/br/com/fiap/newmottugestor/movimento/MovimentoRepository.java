package br.com.fiap.newmottugestor.movimento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovimentoRepository extends JpaRepository<Movimento,Long>, JpaSpecificationExecutor<Movimento> {
}
