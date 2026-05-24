package it.uniroma3.siw.torneo.service;

import it.uniroma3.siw.torneo.model.Giocatore;
import it.uniroma3.siw.torneo.repository.GiocatoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GiocatoreService {

    private final GiocatoreRepository giocatoreRepository;

    public GiocatoreService(GiocatoreRepository giocatoreRepository) {
        this.giocatoreRepository = giocatoreRepository;
    }

    @Transactional(readOnly = true)
    public List<Giocatore> findAll() {
        return giocatoreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Giocatore findById(Long id) {
        return giocatoreRepository.findById(id).orElse(null);
    }

    @Transactional
    public Giocatore save(Giocatore giocatore) {
        return giocatoreRepository.save(giocatore);
    }

    @Transactional
    public void deleteById(Long id) {
        giocatoreRepository.deleteById(id);
    }
}
