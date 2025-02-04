package com.inventarios.handler.marcas.response;

import com.inventarios.model.Marca;
import java.util.List;

public class MarcaResponse {
    private List<Marca> listamarcas;

    public List<Marca> getListamarcas() {
        return listamarcas;
    }

    public void setListamarcas(List<Marca> listamarcas) {
        this.listamarcas = listamarcas;
    }
}
