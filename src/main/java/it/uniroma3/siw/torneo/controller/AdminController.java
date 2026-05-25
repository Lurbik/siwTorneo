package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.model.*;
import it.uniroma3.siw.torneo.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TorneoService torneoService;
    private final SquadraService squadraService;
    private final GiocatoreService giocatoreService;
    private final ArbitroService arbitroService;
    private final PartitaService partitaService;
    private final FileService fileService;

    public AdminController(TorneoService torneoService,
            SquadraService squadraService,
            GiocatoreService giocatoreService,
            ArbitroService arbitroService,
            PartitaService partitaService,
            FileService fileService) {
        this.torneoService = torneoService;
        this.squadraService = squadraService;
        this.giocatoreService = giocatoreService;
        this.arbitroService = arbitroService;
        this.partitaService = partitaService;
        this.fileService = fileService;
    }

    @InitBinder("squadra")
    public void initBinderSquadra(WebDataBinder binder) {
        binder.setDisallowedFields("immagine", "maglietta");
    }

    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    // ---- TORNEO ----
    @GetMapping("/tornei/nuovo")
    public String nuovoTorneoForm(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "admin/form-torneo";
    }

    @PostMapping("/tornei/salva")
    public String salvaTorneo(@Valid @ModelAttribute Torneo torneo, BindingResult result) {
        if (result.hasErrors())
            return "admin/form-torneo";
        torneoService.save(torneo);
        return "redirect:/tornei";
    }

    @GetMapping("/tornei/{id}/elimina")
    public String eliminaTorneo(@PathVariable Long id) {
        torneoService.deleteById(id);
        return "redirect:/tornei";
    }

    @GetMapping("/tornei/{id}/modifica")
    public String modificaTorneoForm(@PathVariable Long id, Model model) {
        model.addAttribute("torneo", torneoService.findById(id));
        return "admin/form-torneo";
    }

    @PostMapping("/tornei/{id}/modifica")
    public String modificaTorneo(@PathVariable Long id,
            @Valid @ModelAttribute Torneo torneo, BindingResult result) {
        if (result.hasErrors())
            return "admin/form-torneo";
        torneo.setId(id);
        torneoService.save(torneo);
        return "redirect:/tornei/" + id;
    }

    @GetMapping("/tornei/{id}/squadre")
    public String gestisciSquadre(@PathVariable Long id, Model model) {
        Torneo torneo = torneoService.findById(id);
        model.addAttribute("torneo", torneo);
        model.addAttribute("tutteLeSquadre", squadraService.findAll());
        return "admin/form-torneo-squadre";
    }

    @PostMapping("/tornei/{id}/squadre")
    public String salvaSquadre(@PathVariable Long id,
            @RequestParam(required = false) List<Long> squadreIds) {
        Torneo torneo = torneoService.findById(id);
        List<Squadra> squadre = squadreIds != null
                ? squadreIds.stream().map(squadraService::findById).collect(java.util.stream.Collectors.toList())
                : new java.util.ArrayList<>();
        torneo.setSquadre(squadre);
        torneoService.save(torneo);
        return "redirect:/tornei/" + id;
    }

    // ---- SQUADRA ----
    @GetMapping("/squadre/nuova")
    public String nuovaSquadraForm(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/form-squadra";
    }

    @PostMapping("/squadre/salva")
    public String salvaSquadra(@Valid @ModelAttribute("squadra") Squadra squadra,
            BindingResult result,
            @RequestParam(required = false) MultipartFile immagine,
            @RequestParam(required = false) MultipartFile maglietta) throws IOException {
        if (result.hasErrors())
            return "admin/form-squadra";
        if (immagine != null && !immagine.isEmpty())
            squadra.setImmagine(fileService.salvaFile(immagine));
        if (maglietta != null && !maglietta.isEmpty())
            squadra.setMaglietta(fileService.salvaFile(maglietta));
        squadraService.save(squadra);
        return "redirect:/squadre";
    }

    @GetMapping("/squadre/{id}/elimina")
    public String eliminaSquadra(@PathVariable Long id) {
        squadraService.deleteById(id);
        return "redirect:/squadre";
    }

    @GetMapping("/squadre/{id}/modifica")
    public String modificaSquadraForm(@PathVariable Long id, Model model) {
        model.addAttribute("squadra", squadraService.findById(id));
        return "admin/form-squadra";
    }

    @PostMapping("/squadre/{id}/modifica")
    public String modificaSquadra(@PathVariable Long id,
            @Valid @ModelAttribute("squadra") Squadra squadra,
            BindingResult result,
            @RequestParam(required = false) MultipartFile immagine,
            @RequestParam(required = false) MultipartFile maglietta) throws IOException {
        if (result.hasErrors())
            return "admin/form-squadra";
        Squadra esistente = squadraService.findById(id);
        if (immagine != null && !immagine.isEmpty())
            squadra.setImmagine(fileService.salvaFile(immagine));
        else
            squadra.setImmagine(esistente.getImmagine());
        if (maglietta != null && !maglietta.isEmpty())
            squadra.setMaglietta(fileService.salvaFile(maglietta));
        else
            squadra.setMaglietta(esistente.getMaglietta());
        squadra.setId(id);
        squadraService.save(squadra);
        return "redirect:/squadre/" + id;
    }

    // ---- GIOCATORE ----
    @GetMapping("/giocatori/nuovo")
    public String nuovoGiocatoreForm(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        model.addAttribute("squadre", squadraService.findAll());
        model.addAttribute("ruoli", RuoloGiocatore.values());
        return "admin/form-giocatore";
    }

    @PostMapping("/giocatori/salva")
    public String salvaGiocatore(@Valid @ModelAttribute Giocatore giocatore,
            BindingResult result,
            @RequestParam(required = false) Long squadraId,
            Model model) {
        if (squadraId == null)
            result.rejectValue("squadra", "error.squadra", "Seleziona una squadra");
        if (result.hasErrors()) {
            model.addAttribute("squadre", squadraService.findAll());
            model.addAttribute("ruoli", RuoloGiocatore.values());
            model.addAttribute("squadraIdSelezionata", squadraId);
            return "admin/form-giocatore";
        }
        giocatore.setSquadra(squadraService.findById(squadraId));
        giocatoreService.save(giocatore);
        return "redirect:/squadre";
    }

    @GetMapping("/giocatori/{id}/modifica")
    public String modificaGiocatoreForm(@PathVariable Long id, Model model) {
        model.addAttribute("giocatore", giocatoreService.findById(id));
        model.addAttribute("squadre", squadraService.findAll());
        model.addAttribute("ruoli", RuoloGiocatore.values());
        return "admin/form-giocatore";
    }

    @PostMapping("/giocatori/{id}/modifica")
    public String modificaGiocatore(@PathVariable Long id,
            @Valid @ModelAttribute Giocatore giocatore,
            BindingResult result,
            @RequestParam(required = false) Long squadraId,
            Model model) {
        if (squadraId == null)
            result.rejectValue("squadra", "error.squadra", "Seleziona una squadra");
        if (result.hasErrors()) {
            model.addAttribute("squadre", squadraService.findAll());
            model.addAttribute("ruoli", RuoloGiocatore.values());
            model.addAttribute("squadraIdSelezionata", squadraId);
            return "admin/form-giocatore";
        }
        giocatore.setId(id);
        giocatore.setSquadra(squadraService.findById(squadraId));
        giocatoreService.save(giocatore);
        return "redirect:/squadre/" + giocatore.getSquadra().getId();
    }

    @GetMapping("/giocatori/{id}/elimina")
    public String eliminaGiocatore(@PathVariable Long id) {
        Giocatore g = giocatoreService.findById(id);
        Long squadraId = g.getSquadra().getId();
        giocatoreService.deleteById(id);
        return "redirect:/squadre/" + squadraId;
    }

    // ---- ARBITRO ----
    @GetMapping("/arbitri/nuovo")
    public String nuovoArbitroForm(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        return "admin/form-arbitro";
    }

    @PostMapping("/arbitri/salva")
    public String salvaArbitro(@Valid @ModelAttribute Arbitro arbitro, BindingResult result) {
        if (result.hasErrors())
            return "admin/form-arbitro";
        arbitroService.save(arbitro);
        return "redirect:/";
    }

    @GetMapping("/arbitri/{id}/modifica")
    public String modificaArbitroForm(@PathVariable Long id, Model model) {
        model.addAttribute("arbitro", arbitroService.findById(id));
        return "admin/form-arbitro";
    }

    @PostMapping("/arbitri/{id}/modifica")
    public String modificaArbitro(@PathVariable Long id,
            @Valid @ModelAttribute Arbitro arbitro, BindingResult result) {
        if (result.hasErrors())
            return "admin/form-arbitro";
        arbitro.setId(id);
        arbitroService.save(arbitro);
        return "redirect:/";
    }

    // ---- PARTITA ----
    @GetMapping("/partite/nuova")
    public String nuovaPartitaForm(Model model) {
        model.addAttribute("partita", new Partita());
        model.addAttribute("tornei", torneoService.findAll());
        model.addAttribute("squadre", squadraService.findAll());
        model.addAttribute("arbitri", arbitroService.findAll());
        model.addAttribute("statiPartita", StatoPartita.values());
        return "admin/form-partita";
    }

    @PostMapping("/partite/salva")
    public String salvaPartita(@Valid @ModelAttribute Partita partita,
            BindingResult result,
            @RequestParam(required = false) Long torneoId,
            @RequestParam(required = false) Long squadraCasaId,
            @RequestParam(required = false) Long squadraOspiteId,
            @RequestParam(required = false) Long arbitroId,
            Model model) {
        if (torneoId == null)
            result.rejectValue("torneo", "error.torneo", "Seleziona un torneo");
        if (squadraCasaId == null)
            result.rejectValue("squadraCasa", "error.squadraCasa", "Seleziona la squadra di casa");
        if (squadraOspiteId == null)
            result.rejectValue("squadraOspite", "error.squadraOspite", "Seleziona la squadra ospite");
        if (arbitroId == null)
            result.rejectValue("arbitro", "error.arbitro", "Seleziona un arbitro");
        if (result.hasErrors()) {
            model.addAttribute("tornei", torneoService.findAll());
            model.addAttribute("squadre", squadraService.findAll());
            model.addAttribute("arbitri", arbitroService.findAll());
            model.addAttribute("statiPartita", StatoPartita.values());
            model.addAttribute("torneoIdSelezionato", torneoId);
            model.addAttribute("squadraCasaIdSelezionata", squadraCasaId);
            model.addAttribute("squadraOspiteIdSelezionata", squadraOspiteId);
            model.addAttribute("arbitroIdSelezionato", arbitroId);
            return "admin/form-partita";
        }
        partita.setTorneo(torneoService.findById(torneoId));
        partita.setSquadraCasa(squadraService.findById(squadraCasaId));
        partita.setSquadraOspite(squadraService.findById(squadraOspiteId));
        partita.setArbitro(arbitroService.findById(arbitroId));
        partitaService.save(partita);
        return "redirect:/partite";
    }

    @GetMapping("/partite/{id}/modifica")
    public String modificaPartitaForm(@PathVariable Long id, Model model) {
        model.addAttribute("partita", partitaService.findById(id));
        model.addAttribute("tornei", torneoService.findAll());
        model.addAttribute("squadre", squadraService.findAll());
        model.addAttribute("arbitri", arbitroService.findAll());
        model.addAttribute("statiPartita", StatoPartita.values());
        return "admin/form-partita";
    }

    @PostMapping("/partite/{id}/modifica")
    public String modificaPartita(@PathVariable Long id,
            @Valid @ModelAttribute Partita partita,
            BindingResult result,
            @RequestParam(required = false) Long torneoId,
            @RequestParam(required = false) Long squadraCasaId,
            @RequestParam(required = false) Long squadraOspiteId,
            @RequestParam(required = false) Long arbitroId,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tornei", torneoService.findAll());
            model.addAttribute("squadre", squadraService.findAll());
            model.addAttribute("arbitri", arbitroService.findAll());
            model.addAttribute("statiPartita", StatoPartita.values());
            model.addAttribute("torneoIdSelezionato", torneoId);
            model.addAttribute("squadraCasaIdSelezionata", squadraCasaId);
            model.addAttribute("squadraOspiteIdSelezionata", squadraOspiteId);
            model.addAttribute("arbitroIdSelezionato", arbitroId);
            return "admin/form-partita";
        }
        partita.setId(id);
        partita.setTorneo(torneoService.findById(torneoId));
        partita.setSquadraCasa(squadraService.findById(squadraCasaId));
        partita.setSquadraOspite(squadraService.findById(squadraOspiteId));
        partita.setArbitro(arbitroService.findById(arbitroId));
        partitaService.save(partita);
        return "redirect:/partite/" + id;
    }

    @GetMapping("/partite/{id}/elimina")
    public String eliminaPartita(@PathVariable Long id) {
        partitaService.deleteById(id);
        return "redirect:/partite";
    }
}