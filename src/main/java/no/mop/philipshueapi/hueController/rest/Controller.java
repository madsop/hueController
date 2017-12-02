package no.mop.philipshueapi.hueController.rest;

import no.mop.philipshueapi.hueController.rest.hueAPI.PhilipsHueConnector;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static no.mop.philipshueapi.hueController.rest.infrastructure.ExceptionWrapper.wrapExceptions;

@ApplicationScoped
public class Controller {

    @SuppressWarnings("unused")
    @Inject
    private PhilipsHueConnector connector;

    private Set<InputProvider> inputProviders;

    public Controller() {
        inputProviders = new HashSet<>();
    }

    public void registerInputProvider(InputProvider inputProvider) {
        inputProviders.add(inputProvider);
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

        double newBrightness = Math.max(0, proposedLightStates.stream()
                .peek(System.out::println)
                .mapToInt(LightState::getBrightness)
                .peek(System.out::println)
                .average()
                .orElse(0));
        newBrightness = Math.min(254, newBrightness);

        return new LightState((int) newBrightness);
    }

    private Set<LightState> getProposedLightStates(int lightIndex) {
        return inputProviders.stream()
                    .peek(System.out::println)
                    .map(inputProvider -> inputProvider.getNewStateForLight(lightIndex))
                    .peek(x -> System.out.println("Inputprovider suggested " + x + " for light " + lightIndex))
                    .collect(Collectors.toSet());
    }
}
