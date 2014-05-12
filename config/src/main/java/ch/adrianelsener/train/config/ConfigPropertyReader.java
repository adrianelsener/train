package ch.adrianelsener.train.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ConfigPropertyReader implements ConfigReader {

    private final Config props = new Config();

    public ConfigPropertyReader(InputStream propertyStream) {
        loadFromStream(propertyStream);
    }

    private void loadFromStream(InputStream propertyStream) {
        final Properties properties = new Properties();
        try {
            properties.load(propertyStream);
        } catch (IOException e) {
            throw new PropertyReadException("Could not read property from Stream", e);
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            props.put(ConfKey.create(entry.getKey().toString()), entry.getValue().toString());
        }
    }

    @Override
    public Config getConfig() {
        return props;
    }
}
