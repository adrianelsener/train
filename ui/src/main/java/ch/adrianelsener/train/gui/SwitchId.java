package ch.adrianelsener.train.gui;

import ch.adrianelsener.train.driver.WeicheMitState;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;


public abstract class SwitchId implements PersistableId {

    public abstract WeicheMitState mapToWeicheMitState(final boolean isOn);

    private static class NumberbasedSwitchId extends SwitchId {

        private final int id;

        private NumberbasedSwitchId(final int id) {
            this.id = id;
        }

        @Override
        public WeicheMitState mapToWeicheMitState(final boolean isOn) {
            final StringBuilder sb = new StringBuilder("_");
            if (id < 10) {
                sb.append("0");
            }
            sb.append(id);
            if (isOn) {
                sb.append("L");
            } else {
                sb.append("R");
            }
            return WeicheMitState.valueOf(sb.toString());
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }

        @Override
        public String toSerializable() {
            return String.valueOf(id);
        }

        @Override
        public String toUiString() {
            return StringUtils.leftPad(String.valueOf(id), 2, "0");
        }
    }

    private static class DummySwitchId extends SwitchId {
        @Override
        public WeicheMitState mapToWeicheMitState(final boolean isOn) {
            throw new IllegalStateException("mapToWeicheMitState kann mit DummySwitchId nicht durchgefuehrt werden");
        }

        @Override
        public String toSerializable() {
            return "NotAssigned";
        }

        @Override
        public String toUiString() {
            return "Na";
        }

    }

    public static SwitchId create(final int id) {
        return new NumberbasedSwitchId(id);
    }

    public static SwitchId createDummy() {
        return new DummySwitchId();
    }

    public static SwitchId fromValue(final String idOrNot) {
        if (NumberUtils.isNumber(idOrNot)) {
            return create(NumberUtils.toInt(idOrNot));
        } else {
            return createDummy();
        }
    }

    @Override
    public abstract String toSerializable();

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public abstract String toUiString();
}
