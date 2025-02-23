package com.inventarios.handler.tipos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.response.TipoResponseRest;
import com.inventarios.model.Tipo;
import java.sql.SQLException;
import java.util.*;

import com.inventarios.util.GsonFactory;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosUpdateTipoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }
  protected abstract void update(Tipo tipo, Long id) throws DatabaseOperationException;
   @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
     input.setHeaders(headers);
     TipoResponseRest responseRest = new TipoResponseRest();
     APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
     List<Tipo> list = new ArrayList<>();
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

         Tipo tipoFromBody = GsonFactory.createGson().fromJson(body, Tipo.class);

         if (tipoFromBody != null) {
               DSLContext dsl = RDSConexion.getDSL();
               Optional<Tipo> tipoSearch = dsl.select()
                       .from(TIPO_TABLE)
                       .where(DSL.field("id", Long.class).eq(id))
                       .fetchOptionalInto(Tipo.class);
               if (tipoSearch.isPresent()) {
                 Tipo tipo = tipoSearch.get();
                 tipo.setNombretipo(tipo.getNombretipo());
                 tipo.setDescriptipo(tipo.getDescriptipo());
                 dsl.update(TIPO_TABLE)
                         .set(DSL.field("nombretipo"), tipoFromBody .getNombretipo().toUpperCase())
                         .set(DSL.field("descriptipo"), tipoFromBody .getDescriptipo().toUpperCase())
                         .where(DSL.field("id").eq(tipo.getId()))
                         .execute();
                 list.add(tipo);
                 responseRest.getTipoResponse().setListatipos(list);
                 responseRest.setMetadata("Respuesta ok", "00", "Tipo actualizado");
               } else {
                 responseRest.setMetadata("Respuesta nok", "-1", "Tipo no encontrado");
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