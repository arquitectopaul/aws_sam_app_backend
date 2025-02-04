package com.inventarios.handler.categoria.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.response.CategoriaResponseRest;
import com.inventarios.model.Categoria;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;

import com.inventarios.util.GsonFactory;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosUpdateCategoriaAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }
  protected final static Table<Record> GRUPO_TABLE = DSL.table("categoria");
  protected abstract void update(Categoria categoria, Long id) throws DatabaseOperationException;
   @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
     input.setHeaders(headers);
     CategoriaResponseRest responseRest = new CategoriaResponseRest();
     APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
     List<Categoria> list = new ArrayList<>();
     String output = "";
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
     try {
         String body = input.getBody();
         // Convertir el cuerpo de la solicitud a un objeto Grupo
         Categoria categoriaFromBody = GsonFactory.createGson().fromJson(body, Categoria.class);
         // Verificar si el objeto Grupo obtenido del cuerpo de la solicitud es válido
         if (categoriaFromBody != null) {
               DSLContext dsl = RDSConexion.getDSL();
               Optional<Categoria> grupoSearch = dsl.select()
                       .from(GRUPO_TABLE)
                       .where(DSL.field("id", Long.class).eq(id))
                       .fetchOptionalInto(Categoria.class);
               if (grupoSearch.isPresent()) {
                 Categoria categoria = grupoSearch.get();
                 categoria.setNombregrupo(categoria.getNombregrupo());
                 categoria.setDescripgrupo(categoria.getDescripgrupo());
                 dsl.update(GRUPO_TABLE)
                         .set(DSL.field("nombregrupo"), categoriaFromBody.getNombregrupo().toUpperCase())
                         .set(DSL.field("descripgrupo"), categoriaFromBody.getDescripgrupo().toUpperCase())
                         .where(DSL.field("id").eq(categoria.getId()))
                         .execute();
                 list.add(categoria);
                 responseRest.getCategoriaResponse().setListacategorias(list);
                 responseRest.setMetadata("Respuesta ok", "00", "Grupo actualizado");
               } else {
                 responseRest.setMetadata("Respuesta nok", "-1", "Grupo no encontrado");
               }
               output = GsonFactory.createGson().toJson(responseRest);
               return response.withStatusCode(200)
                       .withBody(output);
         } else {
             return response
                     .withBody("Invalid group data in request body")
                     .withStatusCode(400);
         }
     } catch (Exception e) {
       responseRest.setMetadata("Respuesta nok", "-1", "Error al actualizar");
       return response
               .withBody(e.toString())
               .withStatusCode(500);
     }
   }

}