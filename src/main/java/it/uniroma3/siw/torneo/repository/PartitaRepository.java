package it.uniroma3.siw.torneo.repository;

import it.uniroma3.siw.torneo.model.Partita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartitaRepository extends JpaRepository<Partita, Long> {
    Page<Partita> findAll(Pageable pageable);
}
