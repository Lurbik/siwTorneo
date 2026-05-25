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
public class Arbitro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 50, message = "Il nome deve essere tra 2 e 50 caratteri")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(min = 2, max = 50, message = "Il cognome deve essere tra 2 e 50 caratteri")
    private String cognome;

    @NotBlank(message = "Il codice arbitrale è obbligatorio")
    @Size(min = 4, max = 20, message = "Il codice deve essere tra 4 e 20 caratteri")
    @Column(unique = true)
    private String codiceArbitrale;

    @OneToMany(mappedBy = "arbitro")
    private List<Partita> partite = new ArrayList<>();

    private String immagine;
}
