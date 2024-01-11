/**
 * @author Brian Enochson
 * @date 01/01/2024
 */
package com.brianeno.app.web;

import com.brianeno.app.model.ChargeSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("client-root-path")
public class APIController {

    private Client jerseyClient;

    public APIController(Client jerseyClient) {
        this.jerseyClient = jerseyClient;
    }

    @GET
    @Path("/sessions/")
    public String getChargeSessions() {
        WebTarget webTarget = jerseyClient.target("http://localhost:8080/sessions");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        ArrayList<ChargeSession> chargeSessions = response.readEntity(ArrayList.class);
        return chargeSessions.toString();
    }

    @GET
    @Path("/sessions/{id}")
    public String getChargeSessionById(@PathParam("id") int id) {
        WebTarget webTarget = jerseyClient.target("http://localhost:8080/sessions/" + id);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        ChargeSession chargeSession = response.readEntity(ChargeSession.class);
        return chargeSession.toString();
    }
}
