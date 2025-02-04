package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import com.inventarios.model.Custodio;
import com.inventarios.model.Proveedor;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public abstract class InventariosBusquedaProveedorAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected final static Field<Long> PROVEEDOR_ID = field(name("proveedor", "id"), Long.class);
  protected static final Field<String> PROVEEDOR_TABLE_RAZONSOCIAL = field(name("proveedor", "razonsocial"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_RUC = field(name("proveedor", "ruc"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_DIRECCION = field(name("proveedor", "direccionfiscal"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_CONTACTO = field(name("proveedor", "contacto"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_TELEFONO = field(name("proveedor", "telefono"), String.class);
  protected final static Field<String> PROVEEDOR_TABLE_CORREO = field(name("proveedor", "correo"), String.class);
  protected final static Field<Long> PROVEEDOR_CUSTODIO_ID = field(name("proveedor", "custodioid"), Long.class);
  protected final static Table<Record> CUSTODIO_TABLE = DSL.table("custodio");
  protected final static Field<Long> CUSTODIO_ID = field(name("custodio", "id"), Long.class);
  protected static final Field<String> CUSTODIO_TABLE_COLUMNA = field(name("custodio", "arearesponsable"), String.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract String mostrarCustodio(Long id) throws DatabaseOperationException;
  protected abstract Result<Record8<Long, String, String, Long, String, String, String, String>> busquedaPorNombreProveedor(String areaResponsable) throws DatabaseOperationException;
  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    System.out.println("========================BUSCAR PROVEEDOR=========================");
    LambdaLogger logger = context.getLogger();
    ProveedorResponseRest responseRest = new ProveedorResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    // String path = input.getPath();
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString.toUpperCase());
    try {
      Result<Record8<Long, String, String, Long, String, String, String, String>> result = busquedaPorNombreProveedor(idString.toUpperCase());
      logger.log("Tamaño del resultado: " + result.size());
      responseRest.getProveedorResponse().setListaproveedores(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Proveedors encontrados");
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
  protected List<Proveedor> convertResultToList0(Result<Record7<Long, String, String, Long, String, String, String>> result) throws DatabaseOperationException {
    List<Proveedor> listaProveedores = new ArrayList<>();
    for (Record record : result) {
      Proveedor proveedor = new Proveedor();
      proveedor.setRazonsocial(record.getValue(PROVEEDOR_TABLE_RAZONSOCIAL));
      listaProveedores.add(proveedor);
    }
    return listaProveedores;
  }
  protected List<Proveedor> convertResultToList(Result<Record8<Long, String, String, Long, String, String, String, String>> result) throws DatabaseOperationException {
    List<Proveedor> listaProveedores = new ArrayList<>();
    for (Record record : result) {
      Proveedor proveedor = new Proveedor();
      proveedor.setId(record.getValue(PROVEEDOR_ID));
      proveedor.setRazonsocial(record.getValue(PROVEEDOR_TABLE_RAZONSOCIAL));
      proveedor.setRuc(record.getValue(PROVEEDOR_TABLE_RUC));
      Custodio custodio = new Custodio();
      custodio.setId(record.get(PROVEEDOR_CUSTODIO_ID));
      custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
      proveedor.setCustodio(custodio);
      proveedor.setDireccionfiscal(record.getValue(PROVEEDOR_TABLE_DIRECCION));
      proveedor.setTelefono(record.getValue(PROVEEDOR_TABLE_TELEFONO));
      proveedor.setCorreo(record.getValue(PROVEEDOR_TABLE_CORREO));
      proveedor.setContacto(record.getValue(PROVEEDOR_TABLE_CONTACTO));
      listaProveedores.add(proveedor);
    }
    return listaProveedores;
  }
}