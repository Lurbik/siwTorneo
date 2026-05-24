package it.uniroma3.siw.torneo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Giocatore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 50, message = "Il nome deve essere tra 2 e 50 caratteri")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(min = 2, max = 50, message = "Il cognome deve essere tra 2 e 50 caratteri")
    private String cognome;

    @NotNull(message = "La data di nascita è obbligatoria")
    @Past(message = "La data di nascita deve essere nel passato")
    private LocalDate dataNascita;

    @Enumerated(EnumType.STRING)
    private RuoloGiocatore ruolo;

    @NotNull(message = "L'altezza è obbligatoria")
    @Min(value = 140, message = "L'altezza minima è 140 cm")
    @Max(value = 230, message = "L'altezza massima è 230 cm")
    private Integer altezza;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Squadra squadra;

    private String immagine;

}
