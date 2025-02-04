package com.inventarios.handler.custodio.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventarios.model.Custodio;
import com.inventarios.core.DatabaseOperationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.inventarios.handler.custodio.response.CustodioResponseRest;
import com.inventarios.model.Proveedor;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosCreateCustodioAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("custodio");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }

  protected abstract void save(String arearesponsable, String nombresyapellidos) throws DatabaseOperationException;
  //protected abstract void save(Custodio custodio, List<Proveedor> proveedoresList) throws DatabaseOperationException;


  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    CustodioResponseRest responseRest = new CustodioResponseRest();
    LambdaLogger logger = context.getLogger();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);

    List<Proveedor> proveedoresList = new ArrayList<>();

    String body = input.getBody();
    context.getLogger().log("body " + body);
    String output ="";
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(input.getBody());

      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Custodio custodio = GsonFactory.createGson().fromJson(body, Custodio.class);
        if (custodio != null) {
          /*List<Proveedor> proveedores=new ArrayList<>();

          Long defaultCustodiaId = 0L;

          Proveedor defaultProveedor=new Proveedor();
          defaultProveedor.setId(defaultCustodiaId);

          proveedores.add(defaultProveedor);

          custodio.setProveedores(proveedores);

          Long custodioId = jsonNode.get("custodioId").asLong();

          Proveedor proveedor =new Proveedor();
          proveedor.setId(custodioId);

          //custodio.getProveedores()
          JsonNode proveedoresNode = jsonNode.get("proveedores");
          if (proveedoresNode != null && proveedoresNode.isArray()) {
            for (JsonNode atributoNode : proveedoresNode) {
              Proveedor proveedores = new Proveedor();
              proveedores.setCustodioid(custodio.getId());
              proveedores.setRazonsocial(atributoNode.get("razonsocial")!=null?atributoNode.get("razonsocial").asText():"");
              proveedores.setRuc(atributoNode.get("ruc")!=null?atributoNode.get("ruc").asText():"");
              proveedores.setDireccionfiscal(atributoNode.get("direccionfiscal")!=null?atributoNode.get("direccionfiscal").asText():"");
              proveedores.setContacto(atributoNode.get("contacto")!=null?atributoNode.get("contacto").asText():"");
              proveedores.setTelefono(atributoNode.get("telefono")!=null?atributoNode.get("telefono").asText():"");
              proveedores.setCorreo(atributoNode.get("correo")!=null?atributoNode.get("correo").asText():"");
              proveedoresList.add(proveedores);
            }
          }
          custodio.setProveedores(proveedoresList);*/
          logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
          save(custodio.getArearesponsable().toUpperCase(), custodio.getNombresyapellidos().toUpperCase());
          ///save(custodio, proveedoresList);
          logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");

          responseRest.setMetadata("Respuesta ok", "00", "Responsable guardado");
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