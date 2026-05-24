package it.uniroma3.siw.torneo.service;

import it.uniroma3.siw.torneo.model.Commento;
import it.uniroma3.siw.torneo.repository.CommentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentoService {

    private final CommentoRepository commentoRepository;

    public CommentoService(CommentoRepository commentoRepository) {
        this.commentoRepository = commentoRepository;
    }

    @Transactional(readOnly = true)
    public List<Commento> findAll() {
        return commentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Commento findById(Long id) {
        return commentoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Commento save(Commento commento) {
        return commentoRepository.save(commento);
    }

    @Transactional
    public void deleteById(Long id) {
        commentoRepository.deleteById(id);
    }
}
