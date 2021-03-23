package de.ars.schulung.sample.boundary.rest;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("api")
@OpenAPIDefinition(info = @Info(
        title = "Todos Management API",
        version = "1.0"
        ))
public class JaxRsActivator extends Application {

}
