package com.inventarios.handler.categoria.response;

import com.inventarios.model.Categoria;
import java.util.List;

public class CategoriaResponse {
    private List<Categoria> listacategorias;
    public List<Categoria> getListacategorias() {
        return listacategorias;
    }
    public void setListacategorias(List<Categoria> listacategorias) {
        this.listacategorias = listacategorias;
    }
}
