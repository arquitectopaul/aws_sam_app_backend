package com.inventarios.handler.especificaciones.response;

import com.inventarios.handler.ResponseRest;
public class EspecificacionesResponseRest extends ResponseRest {
    private EspecificacionesResponse especificacionesResponse = new EspecificacionesResponse();
    public EspecificacionesResponse getEspecificacionesResponse() {
        return especificacionesResponse;
    }
    public void setEspecificacionesResponse(EspecificacionesResponse especificacionesResponse) {
        this.especificacionesResponse = especificacionesResponse;
    }
}
