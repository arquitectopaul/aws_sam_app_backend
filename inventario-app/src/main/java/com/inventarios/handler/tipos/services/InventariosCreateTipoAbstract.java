package com.inventarios.handler.tipos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.model.Tipo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.inventarios.handler.tipos.response.TipoResponseRest;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosCreateTipoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }
  protected abstract void save(Tipo tipo) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    TipoResponseRest responseRest = new TipoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    List<Tipo> list = new ArrayList<>();
    String output ="";
    try {
      String body = input.getBody();
      context.getLogger().log("body " + body);
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Tipo tipo = GsonFactory.createGson().fromJson(body, Tipo.class);
        if (tipo == null) {
          return response
                  .withBody("El cuerpo de la solicitud no contiene datos válidos para un tipo")
                  .withStatusCode(400);
        }
        if (tipo != null) {
          save(tipo);
          list.add(tipo);
          responseRest.getTipoResponse().setListatipos(list);
          responseRest.setMetadata("Respuesta ok", "00", "Tipo guardado");
        }
        output = GsonFactory.createGson().toJson(responseRest);
      }
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }
}