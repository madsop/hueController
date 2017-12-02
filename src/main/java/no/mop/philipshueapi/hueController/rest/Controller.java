package no.mop.philipshueapi.hueController.rest;

import no.mop.philipshueapi.hueController.rest.hueAPI.PhilipsHueConnector;
import no.mop.philipshueapi.hueController.rest.infrastructure.ThrowingSupplier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class Controller {

    @SuppressWarnings("unused")
    @Inject
    private PhilipsHueConnector connector;

    private Set<InputProvider> inputProviders;

    public Controller() {
        inputProviders = new HashSet<>();
    }

    public boolean registerInputProvider(InputProvider inputProvider) {
        return inputProviders.add(inputProvider);
    }

    public String switchStateOfLights() {
        return IntStream.range(0, getAllLights())
                .mapToObj(this::switchStateOfLight)
                .peek(x -> System.out.println("State of this light: " + x))
                .collect(Collectors.joining("\n"));
    }

    private int getAllLights() {
        return wrapExceptions(connector::getAllLights);
    }

    private String switchStateOfLight(int lightIndex) {
        LightState newStateForLight = getNewStateForLight(lightIndex);
        return wrapExceptions(() -> connector.switchStateOfLight(lightIndex, newStateForLight));
    }

    private LightState getNewStateForLight(int lightIndex) {
        Set<LightState> proposedLightStates = getProposedLightStates(lightIndex);

        double newBrightness = proposedLightStates.stream().mapToInt(LightState::getBrightness).average().orElse(0);

        return new LightState((int) newBrightness);
    }

    private Set<LightState> getProposedLightStates(int lightIndex) {
        return inputProviders.stream()
                    .map(inputProvider -> inputProvider.getNewStateForLight(lightIndex))
                    .collect(Collectors.toSet());
    }

    private <T> T wrapExceptions(ThrowingSupplier<T> throwingSupplier) {
        return throwingSupplier.get();
    }

}
