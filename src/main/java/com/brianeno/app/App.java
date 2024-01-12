/**
 * @author Brian Enochson
 * @date 01/01/2024
 */
package com.brianeno.app;

import com.brianeno.app.auth.AppAuthorizer;
import com.brianeno.app.auth.AppBasicAuthenticator;
import com.brianeno.app.auth.User;
import com.brianeno.app.config.ApplicationConfiguration;
import com.brianeno.app.config.ApplicationHealthCheck;
import com.brianeno.app.repository.ChargeSessionRepository;
import com.brianeno.app.web.APIController;
import com.brianeno.app.web.ChargeSessionController;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.migrations.MigrationsBundle;
import jakarta.ws.rs.client.Client;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application<ApplicationConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> b) {
        b.addBundle(new MigrationsBundle<>() {
            @Override
            public DataSourceFactory getDataSourceFactory(final ApplicationConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        enableConfigSubstitutionWithEnvVariables(b);
    }

    private void enableConfigSubstitutionWithEnvVariables(final Bootstrap<ApplicationConfiguration> bootstrap) {
        final var envVarSubst = new EnvironmentVariableSubstitutor(true);
        final var provider = new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), envVarSubst);
        bootstrap.setConfigurationSourceProvider(provider);
    }

    @Override
    public void run(ApplicationConfiguration c, Environment e) throws Exception {

        final Jdbi jdbi = new JdbiFactory().build(e, c.getDataSourceFactory(), "database");

        final ChargeSessionRepository chargeSessionRepository = jdbi.onDemand(ChargeSessionRepository.class);
        LOGGER.info("Registering the Jersey Client");
        final Client client = new JerseyClientBuilder(e)
            .using(c.getJerseyClientConfiguration())
            .build(getName());
        e.jersey().register(new APIController(client));

        LOGGER.info("Registering all REST resources");
        e.jersey().register(new ChargeSessionController(e.getValidator(), chargeSessionRepository));

        LOGGER.info("Registering the Health Check");
        e.healthChecks().register("application", new ApplicationHealthCheck(client));
        e.lifecycle().addServerLifecycleListener(new ServerLifecycleListener() {
            @Override
            public void serverStarted(Server server) {
                LOGGER.info("Jetty Server Started on port " + getLocalPort(server));
            }
        });

        configureJsonMapper(e.getObjectMapper());

        //****** Dropwizard security  ***********/
        e.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
            .setAuthenticator(new AppBasicAuthenticator())
            .setAuthorizer(new AppAuthorizer())
            .setRealm("BASIC-AUTH-REALM")
            .buildAuthFilter()));
        e.jersey().register(RolesAllowedDynamicFeature.class);
        e.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    private void configureJsonMapper(final ObjectMapper mapper) {
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}
