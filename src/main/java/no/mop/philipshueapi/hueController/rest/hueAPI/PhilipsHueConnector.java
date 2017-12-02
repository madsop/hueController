package no.mop.philipshueapi.hueController.rest.hueAPI;

import no.mop.philipshueapi.hueController.rest.LightState;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class PhilipsHueConnector {

    @Inject
    private HttpConnector httpConnector;

    public int getAllLights() throws IOException {
        String responseText = getResponseText("lights");
        return Integer.valueOf(responseText);
    }

    public String switchStateOfLight(int lightIndex, LightState newLightState) throws IOException {
        String path = "light/" + lightIndex + "/brightness/" + newLightState.getBrightness();
        return getResponseText(path);
    }

    private String getResponseText(String path) throws IOException {
        String responsetext = httpConnector.executeHTTPGetOnHue(path);
        System.out.println("Responsetext: " + responsetext);
        return responsetext;
    }
}
