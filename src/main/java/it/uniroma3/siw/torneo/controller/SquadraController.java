package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SquadraController {

    private final SquadraService squadraService;

    public SquadraController(SquadraService squadraService) {
        this.squadraService = squadraService;
    }

    @GetMapping("/squadre/{id}")
    public String dettaglioSquadra(@PathVariable Long id, Model model) {
        model.addAttribute("squadra", squadraService.findById(id));
        return "squadre/dettaglio";
    }

    @GetMapping("/squadre")
    public String listaSquadre(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("squadre", squadraService.findByNome(search));
        } else {
            model.addAttribute("squadre", squadraService.findAll());
        }
        model.addAttribute("search", search);
        return "squadre/lista";
    }

}