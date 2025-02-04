package com.inventarios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="atributo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Atributo  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "custodioId")
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    private Custodio custodio;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "articuloId")
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Articulo articulo;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "tipoId")
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Tipo tipo;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "categoriaId")
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Categoria categoria;
    @OneToMany(mappedBy = "atributo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Atributos> atributos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Custodio getCustodio() {
        return custodio;
    }

    public void setCustodio(Custodio custodio) {
        this.custodio = custodio;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Atributos> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<Atributos> atributos) {
        this.atributos = atributos;
    }
}