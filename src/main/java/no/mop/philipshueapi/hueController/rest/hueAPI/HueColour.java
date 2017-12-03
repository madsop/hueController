package no.mop.philipshueapi.hueController.rest.hueAPI;

import lombok.Getter;

public enum HueColour {
    RED(0), YELLOW(12750), GREEN(25500), BLUE(46920), PURPLE(56100);

    @Getter
    private int hue;

    private HueColour(int hue) {
        this.hue = hue;
    }
}
