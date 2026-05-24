package it.uniroma3.siw.torneo.repository;

import it.uniroma3.siw.torneo.model.Giocatore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiocatoreRepository extends JpaRepository<Giocatore, Long> {

}
