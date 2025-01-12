import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    // Test 3: Return the eruption with the highest number of recorded deaths.

    public Volcano mostDeadly() {
        Optional<Volcano> result = volcanos.stream()
                .filter(v -> !v.getDEATHS().isEmpty())
                .max(Comparator.comparingInt(v -> Integer.parseInt(v.getDEATHS())));
        return result.orElse(null);
    }

    // Test 4:Return the percentage of eruptions that caused tsunamis.

    public double causedTsunami() {
        return volcanos.stream()
                .filter(v -> v.getTsu().equals("tsu"))
                .count() * 100 / volcanos.size();
    }

    // Test 5:Return the most common type of volcano in the set.

    public String mostCommonType() {
        return volcanos.stream()
                .collect(Collectors.groupingBy(Volcano::getType, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Test 6: Return the number of eruptions when supplied a country as an
    // argument.

    public Integer eruptionsByCountry(String country) {
        return volcanos.stream()
                .filter(v -> v.getCountry().equals(country))
                .collect(Collectors.toList())
                .size();
    }

    // Test 7: Return the average elevation of all eruptions.

    public double averageElevation() {
        return volcanos.stream().mapToDouble(v -> v.getElevation()).sum() / volcanos.size();
    }

    // Test 8:Return an array of types of volcanoes.
    public String[] volcanoTypes() {
        return volcanos.stream()
                .map(v -> v.getType())
                .distinct()
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    //Test 9:Return the percentage of eruptions that occurred in the Northern Hemisphere.
    
    public double percentNorth(){
        return volcanos.stream()
        .filter(v -> v.getLatitude() > 0)
        .count() *100d / volcanos.size();
    }

    //Test 10: Return the names of eruptions that occurred after 1800, that did NOT cause a tsunami, happened in the Southern Hemisphere, and had a VEI of 5.

    public String[] manyFilters(){
        return volcanos.stream()
        .filter(v -> v.getYear() > 1800)
        .filter(v -> v.getTsu().equals(""))
        .filter(v->v.getLatitude() < 0)
        .filter(v -> v.getVEI() == 5)
        .map(v -> v.getName())
        .collect(Collectors.toList())
        .toArray(new String[0]);
    }

    //Test 11:Return the names of eruptions that occurred at or above an elevation passed in as an argument.

    public String[] elevatedVolcanoes(int n){
        return volcanos.stream()
        .filter(v -> v.getElevation() >= n)
        .map(v -> v.getName())
        .collect(Collectors.toList())
        .toArray(new String[0]);
    }

    //Test 12:Return the agents of death for the ten most deadly eruptions.
    public String[] topAgentsOfDeath(){
        return volcanos.stream()
        .filter(v -> !v.getDEATHS().isEmpty())
        .sorted((i,j)->Integer.parseInt(j.getDEATHS())-Integer.parseInt(i.getDEATHS()))
        .limit(10)
        .filter(v -> !v.getAgent().isEmpty())
        .map(v -> Arrays.asList(v.getAgent().split(",")))
        .flatMap(List::stream)
        .distinct()
        .collect(Collectors.toList())
        .toArray(new String[0]);
    }
}