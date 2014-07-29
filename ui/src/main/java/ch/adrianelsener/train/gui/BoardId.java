package ch.adrianelsener.train.gui;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;

public abstract class BoardId implements PersistableId {

    public static BoardId createDummy() {
        return new DummyBoardId();
    }

    public static BoardId create(final String string) {
        return new NumberBasedBoardId(string);
    }

    public static BoardId create(final int string) {
        return new NumberBasedBoardId(string);
    }

    @Override
    public abstract String toSerializable();

    public abstract String toUiString();

    public static BoardId create(String first, String second) {
        return new MultiBoardId(first, second);
    }

    private static class DummyBoardId extends BoardId {

        @Override
        public String toSerializable() {
            return "NotAssigned";
        }

        @Override
        public String toUiString() {
            return "Na";
        }

    }

    private static class NumberBasedBoardId extends BoardId {

        private final int id;

        public NumberBasedBoardId(final String id) {
            this(NumberUtils.toInt(id));
        }

        public NumberBasedBoardId(final int id) {
            this.id = id;
        }

        @Override
        public String toSerializable() {
            return String.valueOf(id);
        }

        @Override
        public String toUiString() {
            return String.valueOf(id);
        }

    }

    public static BoardId fromValue(final String idOrNot) {
        if (StringUtils.contains(idOrNot, "/")) {
            return new MultiBoardId(idOrNot);
        } else if (NumberUtils.isNumber(idOrNot)) {
            return create(idOrNot);
        } else {
            return createDummy();
        }

    }

    public static BoardId fromValue(final int i) {
        return create(i);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    private static class MultiBoardId extends BoardId {
        private final String first;
        private final String second;

        public MultiBoardId(String first, String second) {
            super();
            this.first = first;
            this.second = second;
        }

        public MultiBoardId(String idOrNot) {
            final String[] strings = idOrNot.split("/");
            this.first = strings[0];
            this.second = strings[1];
        }

        @Override
        public String toSerializable() {
            return first + "/" + second;
        }

        @Override
        public String toUiString() {
            return first + "/" + second;
        }
    }
}
