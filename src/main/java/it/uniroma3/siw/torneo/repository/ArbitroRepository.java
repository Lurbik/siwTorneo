package it.uniroma3.siw.torneo.repository;

import it.uniroma3.siw.torneo.model.Arbitro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {
    Optional<Arbitro> findByCodiceArbitrale(String codice);
}
