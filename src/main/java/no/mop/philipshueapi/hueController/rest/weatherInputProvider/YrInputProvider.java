package no.mop.philipshueapi.hueController.rest.weatherInputProvider;

import no.mop.philipshueapi.hueController.rest.InputProvider;
import no.mop.philipshueapi.hueController.rest.LightState;
import no.mop.philipshueapi.hueController.rest.hueAPI.HttpConnector;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static no.mop.philipshueapi.hueController.rest.infrastructure.ExceptionWrapper.wrapExceptions;

@ApplicationScoped
public class YrInputProvider implements InputProvider {

    @Inject
    @SuppressWarnings("unused")
    private HttpConnector connector;

    @Inject
    private YrCacheSaver yrCacheSaver;

    private Map<String, Tuple<LocalDateTime, String>> cache = new HashMap<>();

    private final String yrURL = "http://www.yr.no/place/Norge/Oslo/Oslo/Oslo/forecast.xml";

    @PostConstruct
    public void readCache() {
        Optional<CacheEntry> cacheEntry = Optional.ofNullable(wrapExceptions(() -> yrCacheSaver.readCache()));
        cacheEntry.ifPresent(entry ->
                cache.put(entry.getPlace(), new Tuple<>(LocalDateTime.parse(entry.getTime()), entry.getTemperature())));
    }

    @Override
    public LightState getNewStateForLight(int lightIndex) {
        String currentLocation = "Oslo";
        if (cache.containsKey(currentLocation)) {
            Tuple<LocalDateTime, String> tuple = cache.get(currentLocation);
            if (isNewlyUpdatedSoUseCache(tuple)) {
                return getLightState(tuple.getSecond());
            }
        }
        String temperature = convert(wrapExceptions(() -> connector.executeHTTPGet(yrURL)));
        wrapExceptions(() -> yrCacheSaver.save(currentLocation, LocalDateTime.now(), temperature));
        cache.put(currentLocation, new Tuple<>(LocalDateTime.now(), temperature));

        return getLightState(temperature);
    }

    private boolean isNewlyUpdatedSoUseCache(Tuple<LocalDateTime, String> tuple) {
        return tuple.getFirst().isAfter(LocalDateTime.now().minus(Duration.ofHours(1)));
    }

    private LightState getLightState(String temperatureStr) {
        Double temperature = Double.valueOf(temperatureStr);
        int newBrightness = (int) Math.round(temperature);

        return new LightState(newBrightness);
    }

    // Yes, this is obscure. But it works. At least atm.
    private String convert(String xml) {
        String[] observations = xml.split("<observations>", 2);
        String[] observation = observations[1].split("<temperature unit=\"celsius\" value=", 2);
        String[] onlyTemperature = observation[1].split("time", 2);
        return onlyTemperature[0].replaceAll("\"", "").trim();
    }

    @Override
    public String toString(){
        return getClass().getSimpleName();
    }

}
