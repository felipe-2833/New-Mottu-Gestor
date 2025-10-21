package br.com.fiap.newmottugestor.oracle.repository;

import br.com.fiap.newmottugestor.oracle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
