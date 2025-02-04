package com.inventarios.handler.proveedores.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.*;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.model.Custodio;
import com.inventarios.model.Proveedor;
import com.inventarios.handler.proveedores.response.ProveedorResponseRest;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public abstract class InventariosReadProveedorAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Field<Long> PROVEEDOR_TABLE_ID = field(name("proveedor", "id"), Long.class);
  protected static final Field<String> PROVEEDOR_TABLE_RAZONSOCIAL = field(name("proveedor", "razonsocial"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_RUC = field(name("proveedor", "ruc"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_DIRECCIONFISCAL = field(name("proveedor", "direccionfiscal"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_CONTACTO = field(name("proveedor", "contacto"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_TELEFONO = field(name("proveedor", "telefono"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_CORREO = field(name("proveedor", "correo"), String.class);
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected final static Table<Record> CUSTODIO_TABLE = DSL.table("custodio");
  protected final static Field<Long> CUSTODIO_TABLE_ID = field(name("custodio", "id"), Long.class);
  protected static final Field<Long> PROVEEDOR_CUSTODIO_ID = field(name("proveedor", "custodioid"), Long.class);
  protected static final Field<String> CUSTODIO_AREA_RESPONSABLE = field(name("custodio", "arearesponsable"), String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record9<Long, String, String, String, String, String, String, Long, String>> read() throws DatabaseOperationException;
  protected abstract String mostrarCustodio(Long id) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ProveedorResponseRest responseRest = new ProveedorResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String output ="";
    try {
      Result<Record9<Long, String, String, String, String, String, String, Long, String>> result = read();
      responseRest.getProveedorResponse().setListaproveedores(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Proveedores encontrados");
      output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }

  protected List<Proveedor> convertResultToList(Result<Record9<Long, String, String, String, String, String, String, Long, String>> result)
          throws DatabaseOperationException {
    Map<Long, Proveedor> proveedorMap = new HashMap<>();
    for (Record9<Long, String, String, String, String, String, String, Long, String> record : result) {
      Long proveedorId = record.get(PROVEEDOR_TABLE_ID);
      Proveedor proveedor = proveedorMap.get(proveedorId);
      if (proveedor == null) {
        proveedor = new Proveedor();
        proveedor.setId(proveedorId);
        Custodio custodio = new Custodio();
        custodio.setId(record.get(PROVEEDOR_CUSTODIO_ID));
        custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
        proveedor.setCustodio(custodio);
        proveedor.setRuc(record.get(PROVEEDOR_TABLE_RUC));
        proveedor.setRazonsocial(record.get(PROVEEDOR_TABLE_RAZONSOCIAL));
        proveedor.setDireccionfiscal(record.get(PROVEEDOR_TABLE_DIRECCIONFISCAL));
        proveedor.setContacto(record.get(PROVEEDOR_TABLE_CONTACTO));
        proveedor.setTelefono(record.get(PROVEEDOR_TABLE_TELEFONO));
        proveedor.setCorreo(record.get(PROVEEDOR_TABLE_CORREO));
        proveedorMap.put(proveedorId, proveedor);
      }
    }
    return new ArrayList<>(proveedorMap.values());

  }

}