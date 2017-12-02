package no.mop.philipshueapi.hueController.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.IntStream;

@ApplicationScoped
public class Controller {

    @SuppressWarnings("unused")
    @Inject
    private PhilipsHueConnector connector;

    void switchStateOfLights() {
        IntStream.range(0, getAllLights()).forEach(this::switchStateOfLight);
    }

    private int getAllLights() {
        return wrapExceptions(() -> connector.getAllLights());
    }

    private String switchStateOfLight(int lightIndex) {
        return wrapExceptions(() -> connector.switchStateOfLight(lightIndex, 0));
    }

    private <T> T wrapExceptions(ThrowingSupplier<T> throwingSupplier) {
        return throwingSupplier.get();
    }

}
