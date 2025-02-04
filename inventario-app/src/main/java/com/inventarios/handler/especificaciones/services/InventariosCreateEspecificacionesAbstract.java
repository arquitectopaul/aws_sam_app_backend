package com.inventarios.handler.especificaciones.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificaciones.response.EspecificacionesResponseRest;
import com.inventarios.model.Activo;
import com.inventarios.model.Especificaciones;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class InventariosCreateEspecificacionesAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> ESPECIFICACIONES_TABLE = DSL.table("especificaciones");
    protected final static Table<Record> ACTIVO_TABLE = DSL.table("activo");
    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }
    protected abstract void save(Especificaciones especificaciones, Long activoID) throws DatabaseOperationException;
    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        input.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        EspecificacionesResponseRest responseRest = new EspecificacionesResponseRest();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        List<Especificaciones> list = new ArrayList<>();
        String output = "";
        String contentTypeHeader = input.getHeaders().get("Content-Type");
        logger.log("Content-Type: " + contentTypeHeader);
        try {


            String body = input.getBody();
            //String body = "{\"modelo\":\"AS\",\"marca\":\"AS\",\"nroserie\":\"1\",\"fechacompra\":\"1\",\"importe\":777,\"categoriaId\":1,\"account\":55555}";
            logger.log("######################################### BODY ################################################");
            logger.log(body);
            logger.log("###############################################################################################");



            Especificaciones especificaciones = GsonFactory.createGson().fromJson(body, Especificaciones.class);

            logger.log("debe llegar aquí 1 ya tenemos especificaciones");
            if (especificaciones == null) {
                return response
                        .withBody("El cuerpo de la solicitud no contiene datos válidos para un especificaciones")
                        .withStatusCode(400);
            }
            logger.log("debe llegar aquí 2");
            // Obtener el ID del grupo del objeto especificaciones
            //Long categoriaId = especificaciones.getGrupo().getId();


            logger.log("especificaciones: ");

            //if (especificaciones != null) {
            logger.log("especificaciones.getId() = " + especificaciones.getId());
            //logger.log("especificaciones.getDescripespecifico() = " + especificaciones.getDescripespecifico());
            logger.log("especificaciones.getNombreespecifico() = " + especificaciones.getNombreatributo());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);

            // Acceder a los datos del cuerpo de la solicitud
            String activoId = jsonNode.get("activoId").asText();

            logger.log("id from path categoriaId --->  " + activoId);
            Long activoID = null;
            try {
                activoID = Long.parseLong(activoId);
                logger.log("ID del activo: " + activoID);
            } catch (NumberFormatException e) {
                return response
                        .withBody("Invalid id in path")
                        .withStatusCode(400);
            }
            /*String bodyparaque = "{\"modelo\":\"+especificaciones.getModelo()+\",\"marca\":\"+especificaciones.getMarca()+" +
                    "\",\"nroserie\":\"+especificaciones.getNroserie()+\",\"fechacompra\":\"+especificaciones.getFechacompra()+" +
                    "\",\"importe\":+especificaciones.getImporte()+,\"categoriaId\":1,\"account\":+especificaciones.getAccount()}";
            logger.log("bodyparaque: ");
            logger.log(bodyparaque);*/
            DSLContext dsl = RDSConexion.getDSL();
            Optional<Activo> grupoSearch = dsl.select()
                    .from(ACTIVO_TABLE)
                    .where(DSL.field("id", Long.class).eq(activoID))
                    .fetchOptionalInto(Activo.class);
            if (!grupoSearch.isPresent()) {
                //Long categoriaId = especificaciones.getGrupo().getId();

                return response
                        .withBody("El grupo especificado no existe")
                        .withStatusCode(404);
            }
            //if (grupoSearch.isPresent()) {
            logger.log("grupoSearch.isPresent()");
        /*    logger.log("especificaciones.getGrupo I  : "+especificaciones.getGrupo());
            especificaciones.setGrupo(grupoSearch.get());
            logger.log("especificaciones.getGrupo II : "+especificaciones.getGrupo());*/
          /*byte[] compressedPicture = null;
          if (especificaciones.getPicture() != null) {
            compressedPicture = Util.compressZLib(especificaciones.getPicture());
          }*/
            logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
            save(especificaciones, activoID);
            logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
            list.add(especificaciones);
            responseRest.getEspecificacionesResponse().setListaespecificaciones(list);
            responseRest.setMetadata("Respuesta ok", "00", "especificaciones guardado");
              /*} else {
                responseRest.setMetadata("Respuesta nok", "-1", "especificaciones no encontrado");
              }*/
            output = GsonFactory.createGson().toJson(responseRest);
            return response.withStatusCode(200)
                    .withBody(output);
            /*} else {
              return response
                      .withBody("Invalid group data in request body, especificaciones is null")
                      .withStatusCode(400);
            }*/
        /*} else {

      }*/
        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al insertar");
            return response
                    .withBody(new Gson().toJson(responseRest))
                    .withStatusCode(500);
        }
    }

}