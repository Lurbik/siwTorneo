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

public class Commento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 500)
    private String testo;

    private LocalDateTime dataCreazione;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Utente creatore;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Partita partita;

    @PrePersist
    private void prePersist(){
        this.dataCreazione = LocalDateTime.now();
    }
}
