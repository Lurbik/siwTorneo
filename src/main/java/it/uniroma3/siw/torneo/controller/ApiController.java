package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.model.*;
import it.uniroma3.siw.torneo.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final TorneoService torneoService;
    private final PartitaService partitaService;
    private final SquadraService squadraService;

    public ApiController(TorneoService torneoService,
                         PartitaService partitaService,
                         SquadraService squadraService) {
        this.torneoService = torneoService;
        this.partitaService = partitaService;
        this.squadraService = squadraService;
    }

    @GetMapping("/tornei")
    public List<Map<String, Object>> getTornei() {
        return torneoService.findAll().stream().map(t -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", t.getId());
            m.put("nome", t.getNome());
            m.put("anno", t.getAnno());
            return m;
        }).toList();
    }

    @GetMapping("/tornei/{id}/classifica")
    public List<Map<String, Object>> getClassifica(@PathVariable Long id) {
        Torneo torneo = torneoService.findById(id);
        if (torneo == null) return List.of();

        Map<Long, Map<String, Object>> classifica = new LinkedHashMap<>();

        // Inizializza tutte le squadre del torneo
        for (Squadra s : torneo.getSquadre()) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("id", s.getId());
            entry.put("nome", s.getNome());
            entry.put("punti", 0);
            entry.put("vinte", 0);
            entry.put("pareggiate", 0);
            entry.put("perse", 0);
            entry.put("golfatti", 0);
            entry.put("golsubiti", 0);
            entry.put("differenzaReti", 0);
            classifica.put(s.getId(), entry);
        }

        // Calcola punti dalle partite completate
        for (Partita p : torneo.getPartite()) {
            if (p.getStato() != StatoPartita.COMPLETATA) continue;

            Map<String, Object> casa   = classifica.get(p.getSquadraCasa().getId());
            Map<String, Object> ospite = classifica.get(p.getSquadraOspite().getId());
            if (casa == null || ospite == null) continue;

            int gC = p.getGoalHome();
            int gO = p.getGoalGuest();

            aggiorna(casa,   gC, gO);
            aggiorna(ospite, gO, gC);
        }

        // Ordina per punti, poi differenza reti, poi gol fatti
        return classifica.values().stream()
            .sorted(Comparator
                .comparingInt((Map<String, Object> e) -> (int) e.get("punti")).reversed()
                .thenComparingInt(e -> -(int) e.get("differenzaReti"))
                .thenComparingInt(e -> -(int) e.get("golfatti")))
            .toList();
    }

    private void aggiorna(Map<String, Object> entry, int golFatti, int golSubiti) {
        entry.put("golfatti",    (int) entry.get("golfatti")    + golFatti);
        entry.put("golsubiti",   (int) entry.get("golsubiti")   + golSubiti);
        entry.put("differenzaReti", (int) entry.get("differenzaReti") + (golFatti - golSubiti));

        if (golFatti > golSubiti) {
            entry.put("punti",  (int) entry.get("punti")  + 3);
            entry.put("vinte",  (int) entry.get("vinte")  + 1);
        } else if (golFatti == golSubiti) {
            entry.put("punti",       (int) entry.get("punti")       + 1);
            entry.put("pareggiate",  (int) entry.get("pareggiate")  + 1);
        } else {
            entry.put("perse", (int) entry.get("perse") + 1);
        }
    }

    @GetMapping("/tornei/{id}/calendario")
    public List<Map<String, Object>> getCalendario(@PathVariable Long id) {
        Torneo torneo = torneoService.findById(id);
        if (torneo == null) return List.of();

        return torneo.getPartite().stream()
            .sorted(Comparator.comparing(Partita::getDataOra))
            .map(p -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", p.getId());
                m.put("dataOra", p.getDataOra().toString());
                m.put("luogo", p.getLuogo());
                m.put("stato", p.getStato().name());
                m.put("squadraCasa",   p.getSquadraCasa().getNome());
                m.put("squadraOspite", p.getSquadraOspite().getNome());
                m.put("goalHome",  p.getGoalHome());
                m.put("goalGuest", p.getGoalGuest());
                return m;
            }).toList();
    }
}