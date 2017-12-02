package no.mop.philipshueapi.hueController.rest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;

@ApplicationScoped
public class PhilipsHueConnector {

    public Integer getAllLights() throws IOException {
        HttpResponse httpResponse = executeHTTPGet("hue/lights");
        String responseText = getResponseText(httpResponse);
        return Integer.valueOf(responseText);
    }

    public String switchStateOfLight(int lightIndex, int brightness) throws IOException {
        String path = "/light/" + lightIndex + "/brightness/" + brightness;
        HttpResponse httpResponse = executeHTTPGet(path);
        return getResponseText(httpResponse);
    }

    private HttpResponse executeHTTPGet(String path) throws IOException {
        HttpUriRequest request = new HttpGet( getHueURL() + path);
        return HttpClientBuilder.create().build().execute( request );
    }

    private String getHueURL() {
        return "http://localhost:8081/";
    }

    private String getResponseText(HttpResponse httpResponse) throws IOException {
        return IOUtils.toString(httpResponse.getEntity().getContent());
    }
}
