package com.inventarios.handler.activo;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import java.time.LocalDate;
import com.inventarios.handler.activo.services.InventariosReadActivoAbstract;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public class InventariosReadActivo extends InventariosReadActivoAbstract {
  protected Result<Record19<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, String, LocalDate, String, String, String, String>> read() throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select(
                    ACTIVO_ID,
                    ACTIVO_RESPONSABLE_ID,
                    ACTIVO_ARTICULO_ID,
                    ACTIVO_TIPO_ID,
                    ACTIVO_GRUPO_ID,
                    ACTIVO_PROVEEDOR_ID,
                    ESPECIFICACIONES_ID,
                    ESPECIFICACIONES_ESPECIFICOID,
                    ESPECIFICACIONES_NOMBREESPECIFICO,
                    ESPECIFICACIONES_DESCRIPESPECIFICO,
                    ACTIVO_CODINVENTARIO,
                    ACTIVO_MODELO,
                    ACTIVO_MARCA,
                    ACTIVO_NROSERIE,
                    ACTIVO_FECHAINGRESO,
                    ACTIVO_FECHAINGRESOSTR,
                    ACTIVO_MONEDA,
                    ACTIVO_IMPORTE,
                    ACTIVO_DESCRIPCION
            )
            .from(ACTIVO_TABLE)
            .leftJoin(ESPECIFICACIONES_TABLE)
            .on(ACTIVO_ID.eq(ESPECIFICACIONES_ESPECIFICOID))
            .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarCustodio(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(RESPONSABLE_TABLE_COLUMNA)
              .from(RESPONSABLE_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(RESPONSABLE_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarArticulo(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(ARTICULO_TABLE_COLUMNA)
              .from(ARTICULO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(ARTICULO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }

  }

  @Override
  protected String mostrarTipoBien(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(TIPO_TABLE_COLUMNA)
              .from(TIPO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(TIPO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarCategoria(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(GRUPO_TABLE_COLUMNA)
              .from(GRUPO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(GRUPO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarProveedor(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(PROVEEDOR_TABLE_COLUMNA)
              .from(PROVEEDOR_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(PROVEEDOR_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
/*
  @Override
  protected String extractAuthToken(APIGatewayProxyRequestEvent input) {
    Map<String, String> headers = input.getHeaders();
    ////logger.log("headers = "+headers);
    if (headers != null) {
      String authHeader = headers.get("Authorization");
      //logger.log("authHeader = "+authHeader);
      //quitamos los 7 primeros caracteres de authHeader
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        //logger.log("token: "+authHeader.substring(7));
        return authHeader.substring(7);
      }
    }
    return null;
  }
  @Override
  protected AuthorizationInfo validateAuthToken(String authToken) {
    if (authToken != null) {
      try {
        String jwkJson = "{\n" +
                "  \"e\": \"AQAB\",\n" +
                "  \"kty\": \"RSA\",\n" +
                "  \"n\": \"rVgMC69SjrQI3tbiF8VUCku5ldif5ixwYENzFWiK5RjqwGD9tVld55MznQLtgZvA_ab4h7DcUeYWHYEhTIr7cYZI79fjAW2VZFbtbLp86UCREbWhgAoXRt1R6DrNWNgAsaRNXssoWtBSAKvub8o_-Pa6cCxLys8B99d7iZFDkfyUwt8NhijY3_6B4PpWnKEdsA1X2_IPPfn5dJs0PW9Yqa-faQeViExkcamzztZS_SeWJOPALOeDbJ9IXWPWPjokxz8jYUsCNBWjE_h8hTWz8bJJ8QA93-KoGMPm5rbIEyrN7qskbkNnrGCA6BCmbdeZgIgzMKYhgNeydkfiWhuqvQ\"\n" +
                "}";

        try {
          PublicKey publicKey = getPublicKeyFromJWK(jwkJson);
          Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);
          JWTVerifier verifier = JWT.require(algorithm)
                  .withIssuer("https://segurfce.popular-safi.com/realms/Inventarios")
                  .build();
          DecodedJWT jwt = verifier.verify(authToken);

          String subject = jwt.getSubject();
          String issuer = jwt.getIssuer();

          Map<String, Claim> claims = jwt.getClaims();
          Claim claim_realm_access = claims.get("realm_access");
          AuthorizationInfo authorizationInfo=new AuthorizationInfo();
          System.out.println("claim_realm_access = " + claim_realm_access.asMap());
          Map<String, Object> realmAccess = claim_realm_access.asMap();
          List<String> roles = null;
          if (realmAccess != null) {
            Object claim_realm_roles =  realmAccess.get("roles");
            System.out.println("roles = " + claim_realm_roles);
            if (claim_realm_roles instanceof List) {
              roles = new ArrayList<>((List<String>) claim_realm_roles);
            } else if (claim_realm_roles instanceof String) {
              roles = Collections.singletonList((String) claim_realm_roles);
            } else {
              //System.out.println("Invalid realm_access roles format.");
            }
            System.out.println("Subject: " + subject);
            System.out.println("Issuer: " + issuer);
            // Mostrar roles si están presentes
            if (roles != null) {
              //System.out.println("Roles: ");
              for (String role : roles) {
                //System.out.println(role);
                // Ejemplo de verificación de rol específico
                if ("user".equals(role)) {
                  System.out.println("El usuario tiene el rol 'user'.");
                  // Aquí puedes agregar lógica adicional relacionada con el rol 'user'
                }
              }
              authorizationInfo.setRoles(roles);
            } else {
              System.out.println("No roles found.");
            }
          }
          authorizationInfo.setUserId(jwt.getClaim("sid").asString());
          authorizationInfo.setEmail(jwt.getClaim("email").asString());
          authorizationInfo.setName(jwt.getClaim("name").asString());
          authorizationInfo.setGivenName(jwt.getClaim("given_name").asString());
          authorizationInfo.setFamilyName(jwt.getClaim("family_name").asString());
          return authorizationInfo;
        } catch (JWTVerificationException exception) {
          System.out.println("Token verification failed: " + exception.getMessage());
        } catch (Exception e) {
          System.out.println("Exception occurred: " + e.getMessage());
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  private static PublicKey getPublicKeyFromJWK(String jwkJson) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> jwk = mapper.readValue(jwkJson, Map.class);
    BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.get("n")));
    BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.get("e")));

    RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
    KeyFactory factory = KeyFactory.getInstance("RSA");
    return factory.generatePublic(spec);
  }

  private static RSAPublicKey getPublicKeyFromJWK1(String jwkJson) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jwkNode = mapper.readTree(jwkJson);
    String n = jwkNode.get("n").asText();
    String e = jwkNode.get("e").asText();

    byte[] nBytes = Base64.getUrlDecoder().decode(n);
    byte[] eBytes = Base64.getUrlDecoder().decode(e);

    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
            new java.math.BigInteger(1, nBytes),
            new java.math.BigInteger(1, eBytes)
    );
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
  }
  @Override
  protected void addAuthorizationHeaders(AuthorizationInfo authInfo, APIGatewayProxyRequestEvent request) {
    if (authInfo != null) {
      System.out.println("authInfo = "+authInfo);
      request.getHeaders().put("X-UserId", authInfo.getUserId());
      request.getHeaders().put("X-Roles", String.join(",", authInfo.getRoles()));
      System.out.println("request.getHeaders() X-UserId = "+request.getHeaders().get("X-UserId"));
      System.out.println("request.getHeaders() X-Roles = "+request.getHeaders().get("X-Roles"));
    } else {
      System.out.println("authInfo is null, cannot add authorization headers");
    }
  }*/
}