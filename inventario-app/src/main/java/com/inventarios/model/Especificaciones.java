package com.inventarios.model;

import jakarta.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="especificaciones")
public class Especificaciones implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "especificacionid")
    private Long especificacionid;
    private String nombreatributo;
    private String descripcionatributo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEspecificacionid() {
        return especificacionid;
    }

    public void setEspecificacionid(Long especificacionid) {
        this.especificacionid = especificacionid;
    }

    public String getNombreatributo() {
        return nombreatributo;
    }

    public void setNombreatributo(String nombreatributo) {
        this.nombreatributo = nombreatributo;
    }

    public String getDescripcionatributo() {
        return descripcionatributo;
    }

    public void setDescripcionatributo(String descripcionatributo) {
        this.descripcionatributo = descripcionatributo;
    }
}