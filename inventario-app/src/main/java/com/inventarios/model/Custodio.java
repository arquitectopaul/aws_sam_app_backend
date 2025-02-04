package com.inventarios.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="custodio")
public class Custodio implements Serializable{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String arearesponsable;
  private String nombresyapellidos;
  private String correo;
  private String nombreusuario;

  /*@OneToMany(mappedBy = "custodio", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference*/
  private List<Proveedor> proveedores;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getArearesponsable() {
    return arearesponsable;
  }

  public void setArearesponsable(String arearesponsable) {
    this.arearesponsable = arearesponsable;
  }

  public String getNombresyapellidos() {
    return nombresyapellidos;
  }

  public void setNombresyapellidos(String nombresyapellidos) {
    this.nombresyapellidos = nombresyapellidos;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getNombreusuario() {
    return nombreusuario;
  }

  public void setNombreusuario(String nombreusuario) {
    this.nombreusuario = nombreusuario;
  }

  public List<Proveedor> getProveedores() {
    return proveedores;
  }

  public void setProveedores(List<Proveedor> proveedores) {
    this.proveedores = proveedores;
  }
}