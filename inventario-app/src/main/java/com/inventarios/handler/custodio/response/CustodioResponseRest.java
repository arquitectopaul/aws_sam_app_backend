package com.inventarios.handler.custodio.response;

import com.inventarios.handler.ResponseRest;
public class CustodioResponseRest extends ResponseRest {
    private CustodioResponse custodioResponse = new CustodioResponse();
    public CustodioResponse getCustodioResponse() {
        return custodioResponse;
    }
    public void setCustodioResponse(CustodioResponse custodioResponse) {
        this.custodioResponse = custodioResponse;
    }
}