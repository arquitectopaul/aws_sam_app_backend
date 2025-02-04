package com.inventarios.handler.atributos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.handler.atributos.response.AtributosResponseRest;
import com.inventarios.model.Atributos;
import com.inventarios.util.GsonFactory;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class InventariosReadAtributosAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ATRIBUTOS_TABLE = DSL.table("atributos");

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
    //input.setHeaders(headers);
    AtributosResponseRest responseRest = new AtributosResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getAtributosResponse().setListaatributoss(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributoss encontrados");
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

  protected List<Atributos> convertResultToList(Result<Record> result) {
    List<Atributos> listaAtributoss = new ArrayList<>();
    for (Record record : result) {
      Atributos atributos = new Atributos();
      atributos.setId(record.getValue("id", Long.class));
      atributos.setAtributoid(record.getValue("atributoid", Long.class));
      atributos.setNombreatributo(record.getValue("nombreatributo", String.class));
      listaAtributoss.add(atributos);
    }
    return listaAtributoss;
  }

}
