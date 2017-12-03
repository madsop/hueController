package no.mop.philipshueapi.hueController.rest;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Brightness {

    @Getter
    private final int brightness;

    public Brightness(int brightness) {
        this.brightness = brightness < 0 ? 0 : (brightness > 254 ? 254 : brightness);
        if (brightness < 0 || brightness > 254) {
            log("Input brightness was " + brightness +", adjusted to " + this.brightness);
        }
    }

    private void log(String message) {
        System.out.println(message);
    }
}
