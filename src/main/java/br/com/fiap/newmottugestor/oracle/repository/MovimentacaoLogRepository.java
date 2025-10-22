package br.com.fiap.newmottugestor.oracle.repository;

import br.com.fiap.newmottugestor.oracle.model.MovimentacaoLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoLogRepository extends JpaRepository<MovimentacaoLog, Long> {
    @Transactional
    void deleteByMoto_IdMoto(Long motoId);
}