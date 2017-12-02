package no.mop.philipshueapi.hueController.rest;

public interface InputProvider {
    LightState getNewStateForLight(int lightIndex);
}
