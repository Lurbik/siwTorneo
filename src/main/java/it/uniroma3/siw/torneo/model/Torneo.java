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
public class Torneo {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotBlank(message = "Il nome è obbligatorio")
   @Size(min = 2, max = 100, message = "Il nome deve essere tra 2 e 100 caratteri")
   private String nome;

   @NotNull(message = "L'anno è obbligatorio")
   @Min(value = 1900, message = "L'anno deve essere almeno 1900")
   private Integer anno;

   @Size(max = 1000, message = "La descrizione non può superare 1000 caratteri")
   private String descrizione;

   @ManyToMany
   @JoinTable(name = "torneo_squadra", joinColumns = @JoinColumn(name = "torneo_id"), inverseJoinColumns = @JoinColumn(name = "squadra_id"))
   private List<Squadra> squadre = new ArrayList<>();

   @OneToMany(mappedBy = "torneo")
   private List<Partita> partite = new ArrayList<>();
}
