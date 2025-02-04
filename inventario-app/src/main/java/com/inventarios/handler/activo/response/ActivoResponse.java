package com.inventarios.handler.activo.response;

import com.inventarios.model.Activo;
import java.util.List;
public class ActivoResponse {
    private List<Activo> listaactivos;
    public List<Activo> getListaactivos() {
        return listaactivos;
    }
    public void setListaactivos(List<Activo> listaactivos) {
        this.listaactivos = listaactivos;
    }
}
