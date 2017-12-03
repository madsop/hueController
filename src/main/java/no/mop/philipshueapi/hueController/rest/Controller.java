package no.mop.philipshueapi.hueController.rest;

import no.mop.philipshueapi.hueController.rest.hueAPI.PhilipsHueConnector;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
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
        return wrapExceptions(() -> connector.switchStateOfLight(lightIndex, getNewStateForLight(lightIndex)));
    }

    private LightState getNewStateForLight(int lightIndex) {
        Set<LightState> proposedLightStates = getProposedLightStates(lightIndex);

        Brightness newBrightness = getNewBrightness(proposedLightStates);
        Color newColour = getNewColour(proposedLightStates);

        return new LightState(newBrightness, newColour);
    }

    private Brightness getNewBrightness(Set<LightState> proposedLightStates) {
        double newBrightness = proposedLightStates.stream()
                .peek(System.out::println)
                .map(LightState::getBrightness)
                .mapToInt(Brightness::getBrightness)
                .peek(System.out::println)
                .average()
                .orElse(0);
        return new Brightness((int) newBrightness);
    }

    private Color getNewColour(Set<LightState> proposedLightStates) {
        double average = proposedLightStates.stream()
                .map(LightState::getHue)
                .filter(Objects::nonNull)
                .peek(x -> System.out.println("New colour: " +x))
                .mapToInt(Color::getRGB)
                .average()
                .orElse(0);
        return new Color((int) average);
    }

    private Set<LightState> getProposedLightStates(int lightIndex) {
        return inputProviders.stream()
                    .peek(System.out::println)
                    .map(inputProvider -> inputProvider.getNewStateForLight(lightIndex))
                    .peek(x -> System.out.println("Inputprovider suggested " + x + " for light " + lightIndex))
                    .collect(Collectors.toSet());
    }
}
