package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.model.RuoloUtente;
import it.uniroma3.siw.torneo.model.Utente;
import it.uniroma3.siw.torneo.service.UtenteService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UtenteService utenteService, PasswordEncoder passwordEncoder) {
        this.utenteService = utenteService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registraForm(Model model) {
        model.addAttribute("utente", new Utente());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registra(@ModelAttribute Utente utente) {
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utente.setRuolo(RuoloUtente.UTENTE);
        utenteService.save(utente);
        return "redirect:/login";
    }
}