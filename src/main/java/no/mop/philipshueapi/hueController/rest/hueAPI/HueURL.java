package no.mop.philipshueapi.hueController.rest.hueAPI;

import javax.enterprise.context.Dependent;

@Dependent
public class HueURL {

    private final String host = "localhost";
    private final String port = "8081";

    String getFullURL() {
        return "http://" + host + ":" + port +"/hue/";
    }
}
