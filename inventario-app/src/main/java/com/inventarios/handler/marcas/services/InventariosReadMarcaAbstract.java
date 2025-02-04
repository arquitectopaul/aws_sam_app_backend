package com.inventarios.handler.marcas.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.handler.marcas.response.MarcaResponseRest;
import com.inventarios.model.*;
import com.inventarios.util.GsonFactory;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

public abstract class InventariosReadMarcaAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> MARCA_TABLE = DSL.table("marca");
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
    MarcaResponseRest responseRest = new MarcaResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    try {
      Result<Record> result = read();
      responseRest.getMarcaResponse().setListamarcas(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Marcas listadas");
      String output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
  }

  protected List<Marca> convertResultToList(Result<Record> result) throws DatabaseOperationException {
    List<Marca> listaMarcas = new ArrayList<>();
    for (Record record : result) {
      Marca marca = new Marca();
      marca.setId(record.getValue("id", Long.class));
      marca.setNombre(record.getValue("nombre", String.class));
      marca.setDescripcion(record.getValue("descripcion", String.class));
      listaMarcas.add(marca);
    }
    return listaMarcas;
  }

}