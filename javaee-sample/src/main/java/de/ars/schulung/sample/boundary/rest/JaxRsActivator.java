package de.ars.schulung.sample.boundary.rest;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("api")
@OpenAPIDefinition(info = @Info(
        title = "Todos Management API",
        version = "1.0"
        ),
        servers = {
                @Server(url = "/todos-app/api", description = "localhost")
        })
public class JaxRsActivator extends Application {

}
