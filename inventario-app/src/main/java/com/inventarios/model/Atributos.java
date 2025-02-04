package com.inventarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="atributos")
public class Atributos implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atributoid")
    private Long atributoid;
    private String nombreatributo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAtributoid() {
        return atributoid;
    }

    public void setAtributoid(Long atributoid) {
        this.atributoid = atributoid;
    }

    public String getNombreatributo() {
        return nombreatributo;
    }

    public void setNombreatributo(String nombreatributo) {
        this.nombreatributo = nombreatributo;
    }

/*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atributoId") // Nombre de la columna que establece la relación
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Atributo atributo;
    private String nombreatributo;
    private String descripatributo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Atributo getAtributo() {
        return atributo;
    }

    public void setAtributo(Atributo atributo) {
        this.atributo = atributo;
    }

    public String getNombreatributo() {
        return nombreatributo;
    }

    public void setNombreatributo(String nombreatributo) {
        this.nombreatributo = nombreatributo;
    }

    public String getDescripatributo() {
        return descripatributo;
    }

    public void setDescripatributo(String descripatributo) {
        this.descripatributo = descripatributo;
    }*/
}