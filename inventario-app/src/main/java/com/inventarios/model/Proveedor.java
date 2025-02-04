package com.inventarios.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name="proveedor")
public class Proveedor  implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String razonsocial;
    private String ruc;
    private String direccionfiscal;
    private String contacto;
    private String telefono;
    private String correo;

    @ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    private Custodio custodio;
    /*@OneToMany(mappedBy = "custodio", cascade = CascadeType.ALL, orphanRemoval = true)
    //private Custodio custodio;
    /*@ManyToOne(fetch= FetchType.LAZY)
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})*/

    //@Column(name = "custodioid")
    //private Long custodioid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccionfiscal() {
        return direccionfiscal;
    }

    public void setDireccionfiscal(String direccionfiscal) {
        this.direccionfiscal = direccionfiscal;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Custodio getCustodio() {
        return custodio;
    }

    public void setCustodio(Custodio custodio) {
        this.custodio = custodio;
    }

    /*public Long getCustodioid() {
        return custodioid;
    }

    public void setCustodioid(Long custodioid) {
        this.custodioid = custodioid;
    }*/
}