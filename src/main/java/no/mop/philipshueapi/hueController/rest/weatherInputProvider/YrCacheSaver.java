package no.mop.philipshueapi.hueController.rest.weatherInputProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Collections;

@ApplicationScoped
public class YrCacheSaver {

    private Path path;

    @PostConstruct
    public void createCacheIfNotExisting() {
        path = Paths.get("yrcache.json");
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CacheEntry readCache() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        if (Files.size(path) == 0) {
            return null;
        }

        return objectMapper.readValue(path.toFile(), CacheEntry.class);
    }

    public boolean save(String currentLocation, LocalDateTime now, String temperature) throws IOException {
        CacheEntry cacheEntries = new CacheEntry(currentLocation, now.toString(), temperature);
        ObjectMapper mapper = new ObjectMapper();
        if (path == null) {
            createCacheIfNotExisting();
        }

        Files.write(path, Collections.singletonList(mapper.writeValueAsString(cacheEntries)), StandardOpenOption.APPEND);
        return true;
    }
}
