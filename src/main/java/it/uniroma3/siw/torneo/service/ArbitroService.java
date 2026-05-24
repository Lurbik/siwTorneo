package it.uniroma3.siw.torneo.service;

import it.uniroma3.siw.torneo.model.Arbitro;
import it.uniroma3.siw.torneo.repository.ArbitroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;

    public ArbitroService(ArbitroRepository arbitroRepository) {
        this.arbitroRepository = arbitroRepository;
    }

    @Transactional(readOnly = true)
    public List<Arbitro> findAll() {
        return arbitroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Arbitro findById(Long id) {
        return arbitroRepository.findById(id).orElse(null);
    }

    @Transactional
    public Arbitro save(Arbitro arbitro) {
        return arbitroRepository.save(arbitro);
    }

    @Transactional
    public void deleteById(Long id) {
        arbitroRepository.deleteById(id);
    }
}
