package com.inventarios.handler.especificaciones.response;

import com.inventarios.model.Especificaciones;
import java.util.List;
public class EspecificacionesResponse {
    private List<Especificaciones> listaespecificaciones;

    public List<Especificaciones> getListaespecificaciones() {
        return listaespecificaciones;
    }

    public void setListaespecificaciones(List<Especificaciones> listaespecificaciones) {
        this.listaespecificaciones = listaespecificaciones;
    }
}
