import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VolcanoAnalyzer {
    private List<Volcano> volcanos;

    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try {
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString,
                    typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch (Exception e) {
            throw (e);
        }
    }

    public Integer numbVolcanoes() {
        return volcanos.size();
    }

    // add methods here to meet the requirements in README.md

    // Test 1: Return the volcanoes that erupted in the 1980s.
    public List<Volcano> eruptedInEighties() {
        return volcanos.stream()
                .filter(v -> v.getYear() >= 1980 && v.getYear() <= 1989)
                .collect(Collectors.toList());
    }

    // Test 2: Return an array of the names of volcanoes that had an eruption with a
    // Volcanic Explosivity Index (VEI) of 6 or higher.

    public String[] highVEI() {
        return volcanos.stream()
                .filter(v -> v.getVEI() >= 6)
                .map(v -> v.getName())
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    
}