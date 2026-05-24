package it.uniroma3.siw.torneo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Squadra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 100, message = "Il nome deve essere tra 2 e 100 caratteri")
    private String nome;

    @NotNull(message = "L'anno di fondazione è obbligatorio")
    @Min(value = 1, message = "L'anno deve essere positivo")
    private Integer annoFondazione;

    @NotBlank(message = "La città è obbligatoria")
    @Size(min = 2, max = 100, message = "La città deve essere tra 2 e 100 caratteri")
    private String citta;

    @ManyToMany(mappedBy = "squadre")
    private List<Torneo> tornei = new ArrayList<>();

    @OneToMany(mappedBy = "squadra", cascade = CascadeType.REMOVE)
    private List<Giocatore> giocatori = new ArrayList<>();

}