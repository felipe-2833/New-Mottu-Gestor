package br.com.fiap.newmottugestor.oracle.repository;

import br.com.fiap.newmottugestor.oracle.model.Leitor;
import br.com.fiap.newmottugestor.oracle.model.Moto;
import br.com.fiap.newmottugestor.oracle.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MotoRepository extends JpaRepository<Moto,Long>, JpaSpecificationExecutor<Moto> {
    List<Moto> findByLeitorPatio(Patio patio);

    List<Moto> findByLeitor(Leitor leitor);
}
