package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.service.PartitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

@Controller
public class PartitaController {

    private final PartitaService partitaService;

    public PartitaController(PartitaService partitaService) {
        this.partitaService = partitaService;
    }

    @GetMapping("/partite")
    public String listaPartite(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Partita> paginaPartite = partitaService.findPaginated(page, 10);
        model.addAttribute("partite", paginaPartite.getContent());
        model.addAttribute("paginaCorrente", page);
        model.addAttribute("totalePagine", paginaPartite.getTotalPages());
        model.addAttribute("hasPrecedente", paginaPartite.hasPrevious());
        model.addAttribute("hasSuccessiva", paginaPartite.hasNext());
        return "partite/lista";
    }

    @GetMapping("/partite/{id}")
    public String dettaglioPartita(@PathVariable Long id, Model model) {
        model.addAttribute("partita", partitaService.findById(id));
        return "partite/dettaglio";
    }
}