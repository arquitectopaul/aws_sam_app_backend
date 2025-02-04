package com.inventarios.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonString;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import java.util.Optional;

/**
 * Simple REST Resource that consumes information provided by a JWT token.
 * <p>
 * Note that {@code RequestScoped} is explicitly needed here, since Quarkus changed the default
 * scope for JAX-RS Resources to be {@code ApplicationScoped}.
 * <p>
 * See: https://github.com/quarkusio/quarkus/issues/1710
 */
@Path("/data")
@RequestScoped
public class DataResource {

    private static final JsonString ANOYNMOUS = Json.createValue("anonymous");

    @Inject
    @Claim("raw_token")
    String rawToken;

    @Inject
    @Claim(standard = Claims.sub)
    Optional<JsonString> subject;

    @Inject
    @Claim(standard = Claims.preferred_username)
    Optional<JsonString> currentUsername;

    @GET
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"user"})
    public String userData() {
        return "data for user " + currentUsername.orElse(ANOYNMOUS);
    }

    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"admin"})
    public String adminData() {
        return "data for admin " + currentUsername.orElse(ANOYNMOUS);
    }
}