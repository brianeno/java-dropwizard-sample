/**
 * @author Brian Enochson
 * @date 01/01/2024
 */
package com.brianeno.app.web;

import com.brianeno.app.auth.User;
import com.brianeno.app.model.ChargeSession;
import com.brianeno.app.repository.ChargeSessionRepository;
import io.dropwizard.auth.Auth;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
public class ChargeSessionController {

    private Validator validator;
    private ChargeSessionRepository repository;

    public ChargeSessionController(Validator validator, ChargeSessionRepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    @GET
    @PermitAll
    public Response getChargeSessions(@Auth User user) {
        return Response.ok(repository.getSessions()).build();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public Response getChargeSessionsById(@PathParam("id") Integer id, @Auth User user) {
        ChargeSession chargeSession = repository.getChargeSession(id);
        if (chargeSession != null) {
            return Response.ok(chargeSession).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @POST
    @RolesAllowed({"ADMIN"})
    public Response createEChargeSessions(ChargeSession chargeSession, @Auth User user) throws URISyntaxException {
        // validation
        Set<ConstraintViolation<ChargeSession>> violations = validator.validate(chargeSession);
        ChargeSession e = repository.getChargeSession(chargeSession.getId());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<ChargeSession> violation : violations) {
                validationMessages.add(
                    violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (e != null) {
            repository.updateChargeSession(chargeSession.getId(), chargeSession);
            return Response.created(new URI("/sessions/" + chargeSession.getId()))
                .build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    @PermitAll
    public Response updateChargeSessionById(@PathParam("id") Integer id, ChargeSession chargeSession, @Auth User user) {
        // validation
        Set<ConstraintViolation<ChargeSession>> violations = validator.validate(chargeSession);
        ChargeSession e = repository.getChargeSession(chargeSession.getId());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<ChargeSession> violation : violations) {
                validationMessages.add(
                    violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (e != null) {
            chargeSession.setId(id);
            repository.updateChargeSession(id, chargeSession);
            return Response.ok(chargeSession).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    public Response removeChsargeSessionById(@PathParam("id") Integer id, @Auth User user) {
        ChargeSession chargeSession = repository.getChargeSession(id);
        if (chargeSession != null) {
            repository.removeChargeSession(id);
            return Response.ok().build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
