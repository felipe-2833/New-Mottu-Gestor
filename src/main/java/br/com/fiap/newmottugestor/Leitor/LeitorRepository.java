package br.com.fiap.newmottugestor.Leitor;

import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.patio.Patio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeitorRepository extends JpaRepository<Leitor, Long> {

    List<Leitor> findByPatio(Patio patio);

    List<Leitor> findByStatus(TipoStatus status);

}
