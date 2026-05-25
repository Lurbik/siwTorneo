package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.service.SquadraService;
import org.springframework.data.domain.Page;
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
    public String listaSquadre(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<Squadra> pagina;
        if (search != null && !search.isBlank()) {
            pagina = squadraService.findByNomePaginated(search, page, 8);
        } else {
            pagina = squadraService.findPaginated(page, 8);
        }
        model.addAttribute("squadre", pagina.getContent());
        model.addAttribute("paginaCorrente", page);
        model.addAttribute("totalePagine", pagina.getTotalPages());
        model.addAttribute("hasPrecedente", pagina.hasPrevious());
        model.addAttribute("hasSuccessiva", pagina.hasNext());
        model.addAttribute("search", search);
        return "squadre/lista";
    }
}