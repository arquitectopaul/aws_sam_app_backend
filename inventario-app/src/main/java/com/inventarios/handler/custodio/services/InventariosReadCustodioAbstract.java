package com.inventarios.handler.custodio.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.model.Custodio;
import com.inventarios.handler.custodio.response.CustodioResponseRest;
import com.inventarios.util.GsonFactory;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class InventariosReadCustodioAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> CUSTODIO_TABLE = DSL.table("custodio");
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record> read() throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    CustodioResponseRest responseRest = new CustodioResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getCustodioResponse().setListacustodios(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Responsables encontrados");
      output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }
  protected List<Custodio> convertResultToList(Result<Record> result) {
    List<Custodio> listaCustodios = new ArrayList<>();
    for (Record record : result) {
      Custodio custodio = new Custodio();
      custodio.setId(record.getValue("id", Long.class));
      custodio.setArearesponsable(record.getValue("arearesponsable", String.class));
      custodio.setNombresyapellidos(record.getValue("nombresyapellidos", String.class));
      listaCustodios.add(custodio);
    }
    return listaCustodios;
  }

}