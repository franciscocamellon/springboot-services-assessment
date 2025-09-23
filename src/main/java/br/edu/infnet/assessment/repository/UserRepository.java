package br.edu.infnet.assessment.repository;

import br.edu.infnet.assessment.entities.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SystemUser, Long> {
    Optional<SystemUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
