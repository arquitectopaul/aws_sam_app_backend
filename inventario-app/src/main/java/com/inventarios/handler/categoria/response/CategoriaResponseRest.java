package com.inventarios.handler.categoria.response;

import com.inventarios.handler.ResponseRest;

public class CategoriaResponseRest extends ResponseRest {
    private CategoriaResponse categoriaResponse = new CategoriaResponse();
    public CategoriaResponse getCategoriaResponse() {
        return categoriaResponse;
    }
    public void setCategoriaResponse(CategoriaResponse categoriaResponse) {
        this.categoriaResponse = categoriaResponse;
    }
}