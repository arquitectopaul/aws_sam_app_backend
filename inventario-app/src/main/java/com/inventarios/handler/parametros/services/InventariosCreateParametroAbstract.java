package com.inventarios.handler.parametros.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.handler.parametros.response.ParametroResponseRest;
import com.inventarios.model.Parametro;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosCreateParametroAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> PARAMETRO_TABLE = DSL.table("parametros");
    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }

    protected abstract void save(String nombre, String descripcion) throws DatabaseOperationException;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        //input.setHeaders(headers);
        LambdaLogger logger = context.getLogger();
        ParametroResponseRest responseRest = new ParametroResponseRest();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
        String output = "";
        String contentTypeHeader = input.getHeaders().get("Content-Type");
        logger.log("Content-Type: " + contentTypeHeader);
        try {
            String body = input.getBody();
            //String body = "{\"nombre\":\"ALTo\",\"descripcion\":\"ALTURa\"}";
            logger.log("##################### BODY PARAMETRO ######################");
            logger.log(body);
            logger.log("#######################################################");
            if (body != null && !body.isEmpty()) {
                Parametro parametro = GsonFactory.createGson().fromJson(body, Parametro.class);

                logger.log("debe llegar aquí 1 ya tenemos Parametro");
                if (parametro == null) {
                    return response
                            .withBody("El cuerpo de la solicitud no contiene datos válidos para un parametro")
                            .withStatusCode(400);
                }
                logger.log("debe llegar aquí 2");

                logger.log("Parametro: ");
                logger.log("Parametro.getId() = " + parametro.getId());
                logger.log("Parametro.getDescripcion() = " + parametro.getDescripcion());

                logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
                if (parametro != null) {
                    save(parametro.getNombre().toUpperCase(), parametro.getDescripcion().toUpperCase());
                    logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
                    responseRest.setMetadata("Respuesta ok", "00", "Grupo guardado");
                }
                output = GsonFactory.createGson().toJson(responseRest);
            }
            return response.withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al insertar Parámetro");
            return response
                    .withBody(new Gson().toJson(responseRest))
                    .withStatusCode(500);
        }
    }

}

