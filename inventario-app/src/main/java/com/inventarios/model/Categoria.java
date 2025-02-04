package com.inventarios.model;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
  @Table(name="categoria")
  public class Categoria implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombregrupo;
    private String descripgrupo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombregrupo() {
    return nombregrupo;
  }

  public void setNombregrupo(String nombregrupo) {
    this.nombregrupo = nombregrupo;
  }

  public String getDescripgrupo() {
    return descripgrupo;
  }

  public void setDescripgrupo(String descripgrupo) {
    this.descripgrupo = descripgrupo;
  }
}
