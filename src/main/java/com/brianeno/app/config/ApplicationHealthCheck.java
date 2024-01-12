/**
 * @author Brian Enochson
 * @date 01/01/2024
 */
package com.brianeno.app.config;

import com.brianeno.app.model.ChargeSession;
import com.codahale.metrics.health.HealthCheck;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

public class ApplicationHealthCheck extends HealthCheck {

    private final Client client;

    public ApplicationHealthCheck(Client client) {
        super();
        this.client = client;
    }

    @Override
    protected Result check() throws Exception {

        WebTarget webTarget = client.target("http://localhost:8000/session/status");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        int status = response.getStatus();
        if (status < 300) {
            return Result.healthy();
        }
        return Result.unhealthy("API Failed");
    }
}
