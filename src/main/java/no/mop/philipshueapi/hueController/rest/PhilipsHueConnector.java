package no.mop.philipshueapi.hueController.rest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
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

    public Integer getAllLights() throws IOException {
        HttpResponse httpResponse = executeHTTPGet("lights");
        String responseText = getResponseText(httpResponse);
        return Integer.valueOf(responseText);
    }

    public String switchStateOfLight(int lightIndex, LightState newLightState) throws IOException {
        String path = "light/" + lightIndex + "/brightness/" + newLightState.getBrightness();
        HttpResponse httpResponse = executeHTTPGet(path);
        return getResponseText(httpResponse);
    }

    private HttpResponse executeHTTPGet(String path) throws IOException {
        HttpUriRequest request = new HttpGet( getHueURL() + path);
        return HttpClientBuilder.create().build().execute( request );
    }

    private String getHueURL() {
        return "http://" + hueURL.host + ":" + hueURL.port +"/hue/";
    }

    private String getResponseText(HttpResponse httpResponse) throws IOException {
        String responsetext = IOUtils.toString(httpResponse.getEntity().getContent());
        System.out.println("Responsetext: " + responsetext);
        return responsetext;
    }
}
