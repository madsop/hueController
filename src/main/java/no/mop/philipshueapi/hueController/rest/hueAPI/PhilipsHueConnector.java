package no.mop.philipshueapi.hueController.rest.hueAPI;

import no.mop.philipshueapi.hueController.rest.LightState;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class PhilipsHueConnector {

    @Inject
    @SuppressWarnings("unused")
    private HueURL hueURL;

    public int getAllLights() throws IOException {
        String responseText = getResponseText("lights");
        return Integer.valueOf(responseText);
    }

    public String switchStateOfLight(int lightIndex, LightState newLightState) throws IOException {
        String path = "light/" + lightIndex + "/brightness/" + newLightState.getBrightness();
        return getResponseText(path);
    }

    private String getResponseText(String path) throws IOException {
        String responsetext = executeHTTPGet(path);
        System.out.println("Responsetext: " + responsetext);
        return responsetext;
    }

    private String executeHTTPGet(String path) throws IOException {
        HttpUriRequest request = new HttpGet( hueURL.getFullURL() + path);
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        String responsetext = IOUtils.toString(httpResponse.getEntity().getContent());
        httpResponse.close();
        return responsetext;
    }
}
