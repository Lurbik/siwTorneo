package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SquadraController {

    private final SquadraService squadraService;

    public SquadraController(SquadraService squadraService) {
        this.squadraService = squadraService;
    }

    @GetMapping("/squadre")
    public String listaSquadre(Model model) {
        model.addAttribute("squadre", squadraService.findAll());
        return "squadre/lista";
    }

    @GetMapping("/squadre/{id}")
    public String dettaglioSquadra(@PathVariable Long id, Model model) {
        model.addAttribute("squadra", squadraService.findById(id));
        return "squadre/dettaglio";
    }
}