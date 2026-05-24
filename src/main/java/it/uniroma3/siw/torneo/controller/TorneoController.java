package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @GetMapping("/tornei")
    public String listaTornei(Model model) {
        model.addAttribute("tornei", torneoService.findAll());
        return "tornei/lista";
    }

    @GetMapping("/tornei/{id}")
    public String dettaglioTorneo(@PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findById(id));
        return "tornei/dettaglio";
    }
}