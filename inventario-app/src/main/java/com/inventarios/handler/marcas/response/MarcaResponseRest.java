package com.inventarios.handler.marcas.response;

import com.inventarios.handler.ResponseRest;

public class MarcaResponseRest extends ResponseRest {
    private MarcaResponse marcaResponse = new MarcaResponse();

    public MarcaResponse getMarcaResponse() {
        return marcaResponse;
    }

    public void setMarcaResponse(MarcaResponse marcaResponse) {
        this.marcaResponse = marcaResponse;
    }
}
