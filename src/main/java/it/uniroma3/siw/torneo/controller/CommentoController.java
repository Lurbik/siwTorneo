package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.model.Commento;
import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.model.Utente;
import it.uniroma3.siw.torneo.service.CommentoService;
import it.uniroma3.siw.torneo.service.PartitaService;
import it.uniroma3.siw.torneo.service.UtenteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/commenti")
public class CommentoController {

    private final CommentoService commentoService;
    private final PartitaService partitaService;
    private final UtenteService utenteService;

    public CommentoController(CommentoService commentoService,
                               PartitaService partitaService,
                               UtenteService utenteService) {
        this.commentoService = commentoService;
        this.partitaService = partitaService;
        this.utenteService = utenteService;
    }

    @PostMapping("/nuovo")
    public String nuovoCommento(@RequestParam String testo,
                                 @RequestParam Long partitaId,
                                 Authentication auth) {
        Utente utente = utenteService.findByUsername(auth.getName());
        Partita partita = partitaService.findById(partitaId);

        Commento commento = new Commento();
        commento.setTesto(testo);
        commento.setCreatore(utente);
        commento.setPartita(partita);
        commentoService.save(commento);

        return "redirect:/partite/" + partitaId;
    }

    @GetMapping("/{id}/modifica")
    public String modificaForm(@PathVariable Long id,
                                Model model,
                                Authentication auth) {
        Commento commento = commentoService.findById(id);

        // Sicurezza: solo il proprietario può modificare
        if (!commento.getCreatore().getUsername().equals(auth.getName())) {
            return "redirect:/";
        }

        model.addAttribute("commento", commento);
        return "commenti/modifica";
    }

    @PostMapping("/{id}/modifica")
    public String modificaCommento(@PathVariable Long id,
                                    @RequestParam String testo,
                                    Authentication auth) {
        Commento commento = commentoService.findById(id);

        // Sicurezza: solo il proprietario può modificare
        if (!commento.getCreatore().getUsername().equals(auth.getName())) {
            return "redirect:/";
        }

        commento.setTesto(testo);
        commentoService.save(commento);

        return "redirect:/partite/" + commento.getPartita().getId();
    }

    @GetMapping("/{id}/elimina")
    public String eliminaCommento(@PathVariable Long id, Authentication auth) {
        Commento commento = commentoService.findById(id);
        Long partitaId = commento.getPartita().getId();

        // Solo il proprietario o admin può eliminare
        boolean isOwner = commento.getCreatore().getUsername().equals(auth.getName());
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isOwner || isAdmin) {
            commentoService.deleteById(id);
        }

        return "redirect:/partite/" + partitaId;
    }
}