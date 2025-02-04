package com.inventarios.resources;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
/*import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)*/
public class UserResourceLambdaFunction  implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        return null;
    }
/*    @Inject
    SecurityIdentity securityIdentity;
    @Inject
    UserInfo userInfo;
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String path = request.getPath();
        if (path.equals("/users/me")) {
            return handleMeRequest(request, context);
        } else if (path.equals("/users/info")) {
            return handleInfoRequest(request, context);
        } else {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(404)
                    .withBody("Resource not found");
        }
    }
    private APIGatewayProxyResponseEvent handleMeRequest(APIGatewayProxyRequestEvent request, Context context) {
        String username = securityIdentity.getPrincipal().getName();
        Map<String, String> responseBody = Map.of("username", username);
        return buildResponse(200, responseBody);
    }
    private APIGatewayProxyResponseEvent handleInfoRequest(APIGatewayProxyRequestEvent request, Context context) {
        Map<String, String> userInfoMap = Map.of(
                "name", userInfo.getName(),
                "first_name", userInfo.getFirstName(),
                "family_name", userInfo.getFamilyName(),
                "display_name", userInfo.getDisplayName(),
                "preferred_username", userInfo.getPreferredUserName(),
                "sub", userInfo.getSubject(),
                "email", userInfo.getEmail()
        );
        return buildResponse(200, userInfoMap);
    }
    private APIGatewayProxyResponseEvent buildResponse(int statusCode, Object body) {
        try {
            String responseBody = new ObjectMapper().writeValueAsString(body);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withHeaders(Map.of("Content-Type", "application/json"))
                    .withBody(responseBody);
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Error processing response body");
        }
    }
    private String toJson(Map<String, String> map) {
        return map.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(",", "{", "}"));
    }*/
}