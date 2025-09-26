package br.com.fiap.newmottugestor.moto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MotoRepository extends JpaRepository<Moto,Long>, JpaSpecificationExecutor<Moto> {
}
