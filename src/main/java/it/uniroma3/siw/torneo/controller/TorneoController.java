package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.model.Torneo;
import it.uniroma3.siw.torneo.service.TorneoService;
import org.springframework.data.domain.Page;
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
    public String listaTornei(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<Torneo> pagina;
        if (search != null && !search.isBlank()) {
            pagina = torneoService.findByNomePaginated(search, page, 6);
        } else {
            pagina = torneoService.findPaginated(page, 6);
        }
        model.addAttribute("tornei", pagina.getContent());
        model.addAttribute("paginaCorrente", page);
        model.addAttribute("totalePagine", pagina.getTotalPages());
        model.addAttribute("hasPrecedente", pagina.hasPrevious());
        model.addAttribute("hasSuccessiva", pagina.hasNext());
        model.addAttribute("search", search);
        return "tornei/lista";
    }
}