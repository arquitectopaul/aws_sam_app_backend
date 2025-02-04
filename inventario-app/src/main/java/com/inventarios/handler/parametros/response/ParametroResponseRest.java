package com.inventarios.handler.parametros.response;

import com.inventarios.handler.ResponseRest;

public class ParametroResponseRest extends ResponseRest {
    private ParametroResponse parametroResponse = new ParametroResponse();

    public ParametroResponse getParametroResponse() {
        return parametroResponse;
    }

    public void setParametroResponse(ParametroResponse parametroResponse) {
        this.parametroResponse = parametroResponse;
    }
}
