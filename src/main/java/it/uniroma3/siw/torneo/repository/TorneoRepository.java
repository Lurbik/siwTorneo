package it.uniroma3.siw.torneo.repository;

import it.uniroma3.siw.torneo.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    Optional<Torneo> findByNome(String nome);


    @Query("SELECT DISTINCT t FROM Torneo t LEFT JOIN FETCH t.squadre")
    List<Torneo> findAllWithSquadre();

    @Query("SELECT DISTINCT t FROM Torneo t LEFT JOIN FETCH t.partite")
    List<Torneo> findAllWithPartite();

}
