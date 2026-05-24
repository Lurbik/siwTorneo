package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.service.PartitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PartitaController {

    private final PartitaService partitaService;

    public PartitaController(PartitaService partitaService) {
        this.partitaService = partitaService;
    }

    @GetMapping("/partite")
    public String listaPartite(Model model) {
        model.addAttribute("partite", partitaService.findAll());
        return "partite/lista";
    }

    @GetMapping("/partite/{id}")
    public String dettaglioPartita(@PathVariable Long id, Model model) {
        model.addAttribute("partita", partitaService.findById(id));
        return "partite/dettaglio";
    }
}