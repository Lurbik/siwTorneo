package it.uniroma3.siw.torneo.service;

import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.repository.PartitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartitaService {

    private final PartitaRepository partitaRepository;

    public PartitaService(PartitaRepository partitaRepository) {
        this.partitaRepository = partitaRepository;
    }

    @Transactional(readOnly = true)
    public List<Partita> findAll() {
        return partitaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Partita findById(Long id) {
        return partitaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Partita save(Partita partita) {
        return partitaRepository.save(partita);
    }

    @Transactional
    public void deleteById(Long id) {
        partitaRepository.deleteById(id);
    }
}
