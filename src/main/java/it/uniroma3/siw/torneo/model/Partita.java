package it.uniroma3.siw.torneo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La data e ora sono obbligatorie")
    private LocalDateTime dataOra;

    @NotBlank(message = "Il luogo è obbligatorio")
    private String luogo;

    @Min(value = 0, message = "I goal non possono essere negativi")
    private Integer goalHome;

    @Min(value = 0, message = "I goal non possono essere negativi")
    private Integer goalGuest;

    @Enumerated(EnumType.STRING)
    private StatoPartita stato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "squadra_casa_id")
    private Squadra squadraCasa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "squadra_ospite_id")
    private Squadra squadraOspite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Arbitro arbitro;

    @OneToMany(mappedBy = "partita", cascade = CascadeType.REMOVE)
    private List<Commento> commenti = new ArrayList<>();

}
