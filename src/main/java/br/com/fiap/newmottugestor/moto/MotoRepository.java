package br.com.fiap.newmottugestor.moto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MotoRepository extends JpaRepository<Moto,Long>, JpaSpecificationExecutor<Moto> {
    List<Moto> findByLeitorPatioIdPatio(Long patioId);
}
