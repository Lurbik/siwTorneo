package it.uniroma3.siw.torneo.service;

import it.uniroma3.siw.torneo.model.Torneo;
import it.uniroma3.siw.torneo.repository.TorneoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;

    public TorneoService(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    @Transactional(readOnly = true)
    public List<Torneo> findAll() {
        return torneoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Torneo findById(Long id) {
        return torneoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Torneo save(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    @Transactional
    public void deleteById(Long id) {
        torneoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Torneo> findAllWithSquadre() {
        return torneoRepository.findAllWithSquadre();
    }

    @Transactional(readOnly = true)
    public List<Torneo> findAllWithPartite() {
        return torneoRepository.findAllWithPartite();
    }

}