package com.inventarios.handler.marcas.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.handler.marcas.response.MarcaResponseRest;
import com.inventarios.model.Marca;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosUpdateMarcaAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> MARCA_TABLE = DSL.table("marca");
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }

  protected abstract Optional<Marca> marcaSearch(Long id) throws DatabaseOperationException;
  protected abstract void update(Long id, String nombre, String descripcion) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    MarcaResponseRest marcaResponseRest = new MarcaResponseRest();
    List<Marca> listaMarcas = new ArrayList<>();
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
      Marca marcaFromBody = GsonFactory.createGson().fromJson(body, Marca.class);
      if (marcaFromBody != null) {
        if(marcaSearch(id).isPresent()){
          Marca marca = marcaSearch(id).get();
          marca.setNombre(marcaFromBody.getNombre().toUpperCase());
          marca.setDescripcion(marcaFromBody.getDescripcion().toUpperCase());
          update(marca.getId(),
                  marca.getNombre(),
                  marca.getDescripcion());
          listaMarcas.add(marca);
          marcaResponseRest.getMarcaResponse().setListamarcas(listaMarcas);
          marcaResponseRest.setMetadata("Respuesta ok", "00", "Marca actualizada");
        } else {
          marcaResponseRest.setMetadata("Respuesta nok", "-1", "Marca no encontrada");
        }
        String output = GsonFactory.createGson().toJson(marcaResponseRest);
        return response.withStatusCode(200).withBody(output);
      } else {
        return response
                .withBody("Invalid group data in request body")
                .withStatusCode(400);
      }
    } catch (Exception e) {
      marcaResponseRest.setMetadata("Respuesta nok", "-1", "Error al actualizar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
  }
}