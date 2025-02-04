package com.inventarios.handler.custodio.response;

import com.inventarios.model.Custodio;
import java.util.List;
public class CustodioResponse {
    private List<Custodio> listacustodios;
    public List<Custodio> getListacustodios() {
        return listacustodios;
    }
    public void setListacustodios(List<Custodio> listacustodios) {
        this.listacustodios = listacustodios;
    }
}
