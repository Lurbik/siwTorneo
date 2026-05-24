package it.uniroma3.siw.torneo.repository;

import it.uniroma3.siw.torneo.model.Squadra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SquadraRepository extends JpaRepository<Squadra, Long> {
    Optional<Squadra> findByNome(String nome);

    List<Squadra> findByNomeContainingIgnoreCase(String nome);
}
