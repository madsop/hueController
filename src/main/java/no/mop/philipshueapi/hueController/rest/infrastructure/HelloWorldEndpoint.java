package no.mop.philipshueapi.hueController.rest.infrastructure;

import no.mop.philipshueapi.hueController.rest.Controller;
import no.mop.philipshueapi.hueController.rest.clockInputProvider.ClockInputProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/hue")
public class HelloWorldEndpoint {

	@Inject
	@SuppressWarnings("unused")
	private Controller controller;

	@Inject
    @SuppressWarnings("unused")
    private ClockInputProvider clockInputProvider;

	@PostConstruct
	public void HelloWorldEndpoint() {
	    controller.registerInputProvider(clockInputProvider);
    }

	@GET
	@Produces("text/plain")
	@Path("/verify")
	public Response doGet() {
		return Response.ok("Hello from WildFly Swarm!").build();
	}

	@GET
	@Produces("text/plain")
	public Response switchState() {
		return Response.ok(controller.switchStateOfLights()).build();
	}
}