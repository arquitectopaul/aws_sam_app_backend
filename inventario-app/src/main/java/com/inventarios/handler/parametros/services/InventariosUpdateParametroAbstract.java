package com.inventarios.handler.parametros.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.handler.parametros.response.ParametroResponseRest;
import com.inventarios.model.Parametro;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosUpdateParametroAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> PARAMETRO_TABLE = DSL.table("parametros");

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }

  protected abstract Optional<Parametro> parametroSearch(Long id) throws DatabaseOperationException;
  protected abstract void update(Long id, String nombre, String descripcion) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ParametroResponseRest parametroResponseRest = new ParametroResponseRest();
    List<Parametro> listaParametros = new ArrayList<>();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    Long id = null;
    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      return response
              .withBody("Invalid id in path")
              .withStatusCode(400);
    }
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
        context.getLogger().log("body " + body);
        Parametro parametroFromBody = GsonFactory.createGson().fromJson(body, Parametro.class);
        if (parametroFromBody != null) {
          if(parametroSearch(id).isPresent()){
            Parametro parametro = parametroSearch(id).get();
            parametro.setNombre(parametroFromBody.getNombre().toUpperCase());
            parametro.setDescripcion(parametroFromBody.getDescripcion().toUpperCase());
            update(parametro.getId(),
                    parametro.getNombre(),
                    parametro.getDescripcion());
            listaParametros.add(parametro);
            parametroResponseRest.getParametroResponse().setListaparametros(listaParametros);
            parametroResponseRest.setMetadata("Respuesta ok", "00", "Parámetro actualizado");
          } else {
            parametroResponseRest.setMetadata("Respuesta nok", "-1", "Parámetro no encontrado");
          }
          String output = GsonFactory.createGson().toJson(parametroResponseRest);
          return response.withStatusCode(200).withBody(output);
        } else {
          return response
                  .withBody("Invalid group data in request body")
                  .withStatusCode(400);
        }
      } catch (Exception e) {
        parametroResponseRest.setMetadata("Respuesta nok", "-1", "Error al actualizar");
        return response
                .withBody(e.toString())
                .withStatusCode(500);
      }
    }
  }