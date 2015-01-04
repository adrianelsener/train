package ch.adrianelsener.train.config;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;

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

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
