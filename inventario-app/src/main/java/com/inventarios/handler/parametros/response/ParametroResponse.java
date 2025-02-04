package com.inventarios.handler.parametros.response;

import com.inventarios.model.Parametro;
import java.util.List;

public class ParametroResponse {

    private List<Parametro> listaparametros;

    public List<Parametro> getListaparametros() {
        return listaparametros;
    }

    public void setListaparametros(List<Parametro> listaparametros) {
        this.listaparametros = listaparametros;
    }
}
