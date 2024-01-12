/**
 * @author Brian Enochson
 * @date 01/01/2024
 */
package com.brianeno.app.web;

import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map.Entry;
import java.util.Set;

@Produces(MediaType.APPLICATION_JSON)
@Path("/status")
public class HealthCheckController {

    private final HealthCheckRegistry registry;

    public HealthCheckController(HealthCheckRegistry registry) {
        this.registry = registry;
    }

    @GET
    @PermitAll
    public Set<Entry<String, Result>> getStatus() {
        return registry.runHealthChecks().entrySet();
    }
}
