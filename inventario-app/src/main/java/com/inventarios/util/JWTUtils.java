package com.inventarios.util;

import java.util.Base64;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTUtils {

    public static String getKeyIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> header = mapper.readValue(headerJson, Map.class);
            return (String) header.get("kid");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String token = "your-jwt-token-here";
        String keyId = getKeyIdFromToken(token);
        System.out.println("Key ID: " + keyId);
    }
}
