package no.mop.philipshueapi.hueController.rest;

import no.mop.philipshueapi.hueController.rest.hueAPI.PhilipsHueConnector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    @Mock
    private PhilipsHueConnector connector;

    @InjectMocks
    private Controller controller;

    @Test
    public void forwardsToController() throws IOException {
        doReturn(Integer.valueOf(2)).when(connector).getAllLights();
        controller.switchStateOfLights();
        verify(connector).switchStateOfLight(eq(0), anyInt());
        verify(connector).switchStateOfLight(eq(1), anyInt());
    }

}