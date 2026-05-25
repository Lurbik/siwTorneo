package it.uniroma3.siw.torneo.repository;

import it.uniroma3.siw.torneo.model.Torneo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    Optional<Torneo> findByNome(String nome);

    @Query("SELECT DISTINCT t FROM Torneo t LEFT JOIN FETCH t.squadre")
    List<Torneo> findAllWithSquadre();

    @Query("SELECT DISTINCT t FROM Torneo t LEFT JOIN FETCH t.partite")
    List<Torneo> findAllWithPartite();

    List<Torneo> findByNomeContainingIgnoreCase(String nome);

    Page<Torneo> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}