package it.uniroma3.siw.torneo.config;

import it.uniroma3.siw.torneo.model.*;
import it.uniroma3.siw.torneo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TorneoRepository torneoRepository;
    private final PartitaRepository partitaRepository;
    private final ArbitroRepository arbitroRepository;
    private final SquadraRepository squadraRepository;

    public DataInitializer(TorneoRepository torneoRepository,
                           PartitaRepository partitaRepository,
                           ArbitroRepository arbitroRepository,
                           SquadraRepository squadraRepository) {
        this.torneoRepository = torneoRepository;
        this.partitaRepository = partitaRepository;
        this.arbitroRepository = arbitroRepository;
        this.squadraRepository = squadraRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        // Esegui solo se non ci sono già partite
        if (partitaRepository.count() > 0) return;

        // Carica arbitri e squadre già esistenti nel DB
        Arbitro zilean = arbitroRepository.findByCodiceArbitrale("ARB001").orElse(null);
        Arbitro bard    = arbitroRepository.findByCodiceArbitrale("ARB002").orElse(null);
        Arbitro soraka  = arbitroRepository.findByCodiceArbitrale("ARB003").orElse(null);

        Squadra noxus    = squadraRepository.findByNome("Noxus FC").orElse(null);
        Squadra demacia  = squadraRepository.findByNome("Demacia United").orElse(null);
        Squadra freljord = squadraRepository.findByNome("Freljord Wolves").orElse(null);
        Squadra piltover = squadraRepository.findByNome("Piltover City FC").orElse(null);
        Squadra ionia    = squadraRepository.findByNome("Ionia Harmony").orElse(null);
        Squadra ombra    = squadraRepository.findByNome("Isole Ombra FC").orElse(null);
        Squadra bandle   = squadraRepository.findByNome("Bandle City").orElse(null);

        Torneo cup      = torneoRepository.findByNome("Runeterra Cup 2024/2025").orElse(null);
        Torneo hextech  = torneoRepository.findByNome("Hextech Champions League 2025/2026").orElse(null);

        if (zilean == null || noxus == null || cup == null) {
            System.out.println("⚠ DataInitializer: dati base mancanti, skip.");
            return;
        }

        // ===== RUNETERRA CUP 2024/2025 — tutte COMPLETATE =====
        List<Partita> partiteCup = List.of(
            crea(noxus,    demacia,  "2024-09-14T20:00", "Noxus Prime",              3, 1, zilean, cup),
            crea(freljord, ionia,    "2024-09-15T18:00", "Ghiacciaio Eterno",        2, 2, bard,   cup),
            crea(piltover, bandle,   "2024-09-21T21:00", "Palazzo delle Meraviglie", 4, 0, soraka, cup),
            crea(ombra,    noxus,    "2024-10-05T20:00", "Porto delle Ombre",        1, 2, zilean, cup),
            crea(demacia,  freljord, "2024-10-12T18:00", "Gran Plaza",               0, 1, bard,   cup),
            crea(ionia,    piltover, "2024-11-03T20:00", "Giardini dell'Alba",       2, 3, soraka, cup),
            crea(bandle,   ombra,    "2024-11-17T18:00", "Grande Foresta",           1, 1, zilean, cup),
            crea(noxus,    freljord, "2024-12-08T20:00", "Noxus Prime",              2, 0, bard,   cup),
            crea(demacia,  ionia,    "2025-01-12T18:00", "Gran Plaza",               1, 2, soraka, cup),
            crea(piltover, ombra,    "2025-01-19T20:00", "Palazzo delle Meraviglie", 3, 2, zilean, cup),
            crea(freljord, bandle,   "2025-02-02T18:00", "Ghiacciaio Eterno",        5, 0, bard,   cup),
            crea(ionia,    noxus,    "2025-02-16T20:00", "Giardini dell'Alba",       1, 3, soraka, cup),
            crea(ombra,    demacia,  "2025-03-02T18:00", "Porto delle Ombre",        2, 0, zilean, cup),
            crea(bandle,   piltover, "2025-03-16T20:00", "Grande Foresta",           0, 2, bard,   cup),
            crea(noxus,    ombra,    "2025-04-06T18:00", "Noxus Prime",              4, 1, soraka, cup)
        );
        partitaRepository.saveAll(partiteCup);

        // ===== HEXTECH CHAMPIONS LEAGUE 2025/2026 — mix COMPLETATE e IN_ATTESA =====
        List<Partita> partiteHex = List.of(
            crea(demacia,  noxus,    "2025-09-07T20:00", "Gran Plaza",               1, 2, bard,   hextech),
            crea(ionia,    freljord, "2025-09-14T18:00", "Giardini dell'Alba",       0, 0, soraka, hextech),
            crea(bandle,   ombra,    "2025-09-21T20:00", "Grande Foresta",           2, 3, zilean, hextech),
            crea(piltover, noxus,    "2025-10-05T18:00", "Palazzo delle Meraviglie", 1, 1, bard,   hextech),
            crea(freljord, demacia,  "2025-10-19T20:00", "Ghiacciaio Eterno",        2, 1, soraka, hextech),
            crea(ombra,    ionia,    "2025-11-02T18:00", "Porto delle Ombre",        3, 0, zilean, hextech),
            crea(noxus,    bandle,   "2025-11-16T20:00", "Noxus Prime",              5, 1, bard,   hextech),
            crea(demacia,  piltover, "2025-12-07T18:00", "Gran Plaza",               2, 2, soraka, hextech),
            crea(ionia,    ombra,    "2026-01-11T20:00", "Giardini dell'Alba",       1, 2, zilean, hextech),
            crea(freljord, noxus,    "2026-02-01T18:00", "Ghiacciaio Eterno",        0, 3, bard,   hextech),
            crea(bandle,   demacia,  "2026-02-15T20:00", "Grande Foresta",           1, 4, soraka, hextech),
            crea(piltover, ionia,    "2026-03-01T18:00", "Palazzo delle Meraviglie", 2, 1, zilean, hextech),
            attesa(noxus,    ombra,    "2026-06-07T20:00", "Noxus Prime",              zilean, hextech),
            attesa(demacia,  ionia,    "2026-06-14T18:00", "Gran Plaza",               bard,   hextech),
            attesa(freljord, piltover, "2026-06-21T20:00", "Ghiacciaio Eterno",        soraka, hextech),
            attesa(bandle,   noxus,    "2026-07-05T18:00", "Grande Foresta",           zilean, hextech),
            attesa(ombra,    freljord, "2026-07-12T20:00", "Porto delle Ombre",        bard,   hextech),
            attesa(ionia,    demacia,  "2026-07-19T18:00", "Giardini dell'Alba",       soraka, hextech)
        );
        partitaRepository.saveAll(partiteHex);

        System.out.println("✅ DataInitializer: partite inserite con successo!");
    }

    private Partita crea(Squadra casa, Squadra ospite, String data, String luogo,
                         int gC, int gO, Arbitro arbitro, Torneo torneo) {
        Partita p = new Partita();
        p.setSquadraCasa(casa);
        p.setSquadraOspite(ospite);
        p.setDataOra(LocalDateTime.parse(data));
        p.setLuogo(luogo);
        p.setGoalHome(gC);
        p.setGoalGuest(gO);
        p.setStato(StatoPartita.COMPLETATA);
        p.setArbitro(arbitro);
        p.setTorneo(torneo);
        return p;
    }

    private Partita attesa(Squadra casa, Squadra ospite, String data, String luogo,
                           Arbitro arbitro, Torneo torneo) {
        Partita p = new Partita();
        p.setSquadraCasa(casa);
        p.setSquadraOspite(ospite);
        p.setDataOra(LocalDateTime.parse(data));
        p.setLuogo(luogo);
        p.setGoalHome(0);
        p.setGoalGuest(0);
        p.setStato(StatoPartita.IN_ATTESA);
        p.setArbitro(arbitro);
        p.setTorneo(torneo);
        return p;
    }
}