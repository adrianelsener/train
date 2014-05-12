package ch.adrianelsener.train.config;

import java.util.HashMap;

import com.google.common.collect.Maps;

public class Config {

    private final HashMap<ConfKey, String> props = Maps.newHashMap();
    private final ConfKey parentKey;

    public Config() {
        this(ConfKey.createRoot());
    }

    private Config(final ConfKey root) {
        parentKey = root;
    }

    public String get(final ConfKey key) {
        return props.get(key);
    }

    public void put(final ConfKey key, final String val) {
        props.put(key, val);
    }

    public Config getAll(final ConfKey boardKey) {
        final Config subConfig = new Config(boardKey);
        for (final ConfKey key : props.keySet()) {
            if (key.isSubkey(boardKey)) {
                subConfig.put(key, props.get(key));
            }
        }
        return subConfig;
    }

    public String getChild(final String childname) {
        return get(parentKey.createSubKey(childname));
    }

}
