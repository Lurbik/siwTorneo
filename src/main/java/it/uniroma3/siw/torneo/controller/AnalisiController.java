package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.model.Torneo;
import it.uniroma3.siw.torneo.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AnalisiController {

    private final TorneoService torneoService;

    public AnalisiController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @GetMapping("/analisi")
    public String analisi(Model model) {

        // ===== STRATEGIA 1: LAZY (default) =====
        long t1 = System.nanoTime();
        List<Torneo> lazy = torneoService.findAll();
        // Forza il caricamento delle relazioni (altrimenti LAZY non fa le query)
        lazy.forEach(t -> {
            t.getSquadre().size();
            t.getPartite().size();
        });
        long tempoLazy = System.nanoTime() - t1;

        // ===== STRATEGIA 2: JOIN FETCH squadre =====
        long t2 = System.nanoTime();
        List<Torneo> joinFetch = torneoService.findAllWithSquadre();
        joinFetch.forEach(t -> t.getSquadre().size());
        long tempoJoinFetch = System.nanoTime() - t2;

        // ===== STRATEGIA 3: JOIN FETCH squadre + partite =====
        long t3 = System.nanoTime();
List<Torneo> joinFetchPartite = torneoService.findAllWithPartite();
joinFetchPartite.forEach(t -> t.getPartite().size());
long tempoJoinFetchAll = System.nanoTime() - t3;

        // Converti in millisecondi con 3 decimali
        model.addAttribute("tempoLazy",        String.format("%.3f ms", tempoLazy / 1_000_000.0));
        model.addAttribute("tempoJoinFetch",   String.format("%.3f ms", tempoJoinFetch / 1_000_000.0));
        model.addAttribute("tempoJoinFetchAll",String.format("%.3f ms", tempoJoinFetchAll / 1_000_000.0));
        model.addAttribute("numTornei",        lazy.size());
        model.addAttribute("numSquadre",       lazy.stream().mapToInt(t -> t.getSquadre().size()).sum());
        model.addAttribute("numPartite", torneoService.findAll().stream().mapToInt(t -> t.getPartite().size()).sum());

        return "analisi";
    }
}