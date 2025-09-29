package br.com.fiap.newmottugestor.moto;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.patio.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MotoRepository extends JpaRepository<Moto,Long>, JpaSpecificationExecutor<Moto> {
    List<Moto> findByLeitorPatio(Patio patio);

    List<Moto> findByLeitor(Leitor leitor);
}
