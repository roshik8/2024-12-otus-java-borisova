package ru.otus.dataprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private final String fileName;
    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        ObjectMapper mapper = new ObjectMapper();
        List<Measurement> measurements = new ArrayList<>();
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            JsonNode arrNode = mapper.readTree(inputStream);
            for (JsonNode node : arrNode) {
                measurements.add(new Measurement(node.get("name").textValue(), node.get("value").doubleValue()));
            }
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
        return measurements;
    }
}
