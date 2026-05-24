package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @GetMapping("/tornei/{id}")
    public String dettaglioTorneo(@PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findById(id));
        return "tornei/dettaglio";
    }

    @GetMapping("/tornei")
    public String listaTornei(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("tornei", torneoService.findByNome(search));
        } else {
            model.addAttribute("tornei", torneoService.findAll());
        }
        model.addAttribute("search", search);
        return "tornei/lista";
    }

}