package com.inventarios.handler.custodio.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.response.CustodioResponseRest;
import com.inventarios.model.Custodio;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosUpdateCustodioAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }

  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("custodio");
  protected abstract void update(Custodio custodio, Long id) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
     input.setHeaders(headers);
     CustodioResponseRest responseRest = new CustodioResponseRest();
     APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
     List<Custodio> list = new ArrayList<>();
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

         Custodio custodioFromBody = GsonFactory.createGson().fromJson(body, Custodio.class);

         if (custodioFromBody != null) {
               DSLContext dsl = RDSConexion.getDSL();
               Optional<Custodio> responsableSearch = dsl.select()
                       .from(RESPONSABLE_TABLE)
                       .where(DSL.field("id", Long.class).eq(id))
                       .fetchOptionalInto(Custodio.class);
               if (responsableSearch.isPresent()) {
                 Custodio custodio = responsableSearch.get();
                 custodio.setArearesponsable(custodio.getArearesponsable());
                 custodio.setNombresyapellidos(custodio.getNombresyapellidos());
                 dsl.update(RESPONSABLE_TABLE)
                         .set(DSL.field("arearesponsable"), custodioFromBody.getArearesponsable().toUpperCase())
                         .set(DSL.field("nombresyapellidos"), custodioFromBody.getNombresyapellidos().toUpperCase())
                         .where(DSL.field("id").eq(custodio.getId()))
                         .execute();
                 list.add(custodio);
                 responseRest.getCustodioResponse().setListacustodios(list);
                 responseRest.setMetadata("Respuesta ok", "00", "Responsable actualizado");
               } else {
                 responseRest.setMetadata("Respuesta nok", "-1", "Responsable no encontrado");
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