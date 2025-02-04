package com.inventarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="marca")
public class Marca implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

  /*    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    private Custodio custodio;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Tipo tipo;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Categoria categoria;

    private String descripcomun;
    //private String descripcortacomun;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Custodio getResponsable() {
        return custodio;
    }

    public void setResponsable(Custodio custodio) {
        this.custodio = custodio;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Categoria getGrupo() {
        return categoria;
    }

    public void setGrupo(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescripcomun() {
        return descripcomun;
    }

    public void setDescripcomun(String descripcomun) {
        this.descripcomun = descripcomun;
    }
*/
}
