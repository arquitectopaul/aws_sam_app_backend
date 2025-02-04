package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.model.Custodio;
import com.inventarios.model.Proveedor;
import java.util.HashMap;
import java.util.Map;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosCreateProveedorAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }
  protected abstract void save(String ruc,
                               String razonsocial,
                               String direccionfiscal,
                               String contacto,
                               String telefono,
                               String correo,
                               long custodioId) throws DatabaseOperationException;
  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ProveedorResponseRest responseRest = new ProveedorResponseRest();
    LambdaLogger logger = context.getLogger();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);

    String body = input.getBody();

    context.getLogger().log("body " + body);
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(input.getBody());
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Proveedor proveedor = GsonFactory.createGson().fromJson(body, Proveedor.class);
        if (proveedor != null) {
          logger.log("proveedor = "+proveedor);
          long custodioId = 0;
          try{
            if (jsonNode.get("custodioId") != null) {
              custodioId = jsonNode.get("custodioId").asLong();
              logger.log("custodioId = "+custodioId);
            }
          } catch (NumberFormatException e) {
            logger.log("Error al convertir custodioId a long: " + e.getMessage());
            responseRest.setMetadata("Respuesta nok", "-1", "Error al convertir custodioId a long");
            String output = GsonFactory.createGson().toJson(responseRest);
            return response.withStatusCode(400).withBody(output);
          }
          Custodio custodio =new Custodio();
          custodio.setId(custodioId);

          proveedor.setCustodio(custodio);

          save(proveedor.getRuc(),
              proveedor.getRazonsocial().toUpperCase(),
              proveedor.getDireccionfiscal().toUpperCase(),
              proveedor.getContacto().toUpperCase(),
              proveedor.getTelefono().toUpperCase(),
              proveedor.getCorreo().toUpperCase(),
              proveedor.getCustodio().getId()
          );
          responseRest.setMetadata("Respuesta ok", "00", "Proveedor guardado");
        }
      }
      String output = GsonFactory.createGson().toJson(responseRest);
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