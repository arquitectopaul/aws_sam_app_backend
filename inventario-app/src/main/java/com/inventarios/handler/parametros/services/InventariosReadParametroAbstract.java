package com.inventarios.handler.parametros.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.*;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.handler.parametros.response.ParametroResponseRest;
import com.inventarios.model.*;
import com.inventarios.util.GsonFactory;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class InventariosReadParametroAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> PARAMETRO_TABLE = DSL.table("parametros");
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
    ParametroResponseRest responseRest = new ParametroResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getParametroResponse().setListaparametros(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Parametros listados");
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

  protected List<Parametro> convertResultToList(Result<Record> result) throws DatabaseOperationException {
    List<Parametro> listaParametros = new ArrayList<>();
    for (Record record : result) {
      Parametro parametro = new Parametro();
      parametro.setId(record.getValue("id", Long.class));
      parametro.setNombre(record.getValue("nombre", String.class));
      parametro.setDescripcion(record.getValue("descripcion", String.class));
      listaParametros.add(parametro);
    }
    return listaParametros;
  }

}
