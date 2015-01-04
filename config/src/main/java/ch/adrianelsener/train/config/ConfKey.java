package ch.adrianelsener.train.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class ConfKey {

    protected final String string;

    ConfKey(final String string) {
        this.string = string;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((string == null) ? 0 : string.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj){
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static ConfKey create(final String string) {
        return new FullConfKey(string);
    }

    public static ConfKey createForBoard(final String boardId) {
        return new BoardConfKey(boardId);
    }

    private static class FullConfKey extends ConfKey {

        FullConfKey(final String key) {
            super(key);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }

    }

    private static class BoardConfKey extends ConfKey {

        BoardConfKey(final String string) {
            super(string);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }

    }

    public static ConfKey create(final ConfKey parent, final String child) {
        return create(parent.string + "." + child);
    }

    public boolean isSubkey(final ConfKey other) {
        return string.startsWith(other.string);
    }

    public ConfKey createSubKey(final String child) {
        return ConfKey.create(this, child);
    }

    public static ConfKey createRoot() {
        return create("");
    }

}
