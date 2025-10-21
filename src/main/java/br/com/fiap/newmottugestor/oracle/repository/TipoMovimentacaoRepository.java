package br.com.fiap.newmottugestor.oracle.repository;

import br.com.fiap.newmottugestor.oracle.model.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoMovimentacaoRepository extends JpaRepository<TipoMovimentacao, Long> {
    Optional<TipoMovimentacao> findByNome(String nome);
}