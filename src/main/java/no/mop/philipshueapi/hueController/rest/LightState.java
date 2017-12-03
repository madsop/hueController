package no.mop.philipshueapi.hueController.rest;

import lombok.Data;

import java.awt.*;

@Data
public class LightState {
    private final Brightness brightness;
    private final Color hue;

    public final int getBrightnessInt() {
        return brightness.getBrightness();
    }
}
