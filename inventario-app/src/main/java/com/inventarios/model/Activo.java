package com.inventarios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="activo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    private Custodio custodio;
    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Articulo articulo;
    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Tipo tipo;
    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
    private Categoria categoria;
    private String codinventario;//cód. Administración
    private String modelo;
    private String marca;
    private String nroserie;
    //formato default
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaingreso;
    private String fechaingresostr;
    private String moneda;
    private BigDecimal importe;
    @OneToMany(mappedBy = "activo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Especificaciones> especificaciones;
    private String descripcion;
    private Proveedor proveedor;
    //private Long proveedorId;
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

    public String getCodinventario() {
        return codinventario;
    }

    public void setCodinventario(String codinventario) {
        this.codinventario = codinventario;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNroserie() {
        return nroserie;
    }

    public void setNroserie(String nroserie) {
        this.nroserie = nroserie;
    }

    public LocalDate getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(LocalDate fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public String getFechaingresostr() {
        return fechaingresostr;
    }

    public void setFechaingresostr(String fechaingresostr) {
        this.fechaingresostr = fechaingresostr;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public List<Especificaciones> getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(List<Especificaciones> especificaciones) {
        this.especificaciones = especificaciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}