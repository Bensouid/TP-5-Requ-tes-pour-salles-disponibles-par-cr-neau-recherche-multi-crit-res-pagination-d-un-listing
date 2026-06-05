package com.example.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "equipements")
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    @ManyToMany(mappedBy = "equipements")
    private Set<Salle> salles = new HashSet<>();

    // 1. Constructeurs
    public Equipement() {
    }

    public Equipement(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // 2. Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Salle> getSalles() {
        return salles;
    }

    public void setSalles(Set<Salle> salles) {
        this.salles = salles;
    }

    // 3. Méthodes de synchronisation (Recommandé pour le ManyToMany)
    public void addSalle(Salle salle) {
        this.salles.add(salle);
        if (!salle.getEquipements().contains(this)) {
            salle.getEquipements().add(this);
        }
    }

    public void removeSalle(Salle salle) {
        this.salles.remove(salle);
        if (salle.getEquipements().contains(this)) {
            salle.getEquipements().remove(this);
        }
    }

    // 4. equals et hashCode basés sur l'ID (crucial pour les entités JPA dans des Sets)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipement that = (Equipement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // 5. toString (Attention à ne pas inclure "salles" pour éviter une boucle infinie de logs)
    @Override
    public String toString() {
        return "Equipement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}