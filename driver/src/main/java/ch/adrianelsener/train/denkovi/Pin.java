package ch.adrianelsener.train.denkovi;

import com.adventnet.snmp.snmp2.SnmpAPI;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public enum Pin {
    _01(Jp.P3, 1), //
    _02(Jp.P3, 2), //
    _03(Jp.P3, 4), //
    _04(Jp.P3, 8), //
    _05(Jp.P3, 16), //
    _06(Jp.P3, 32), //
    _07(Jp.P3, 64), //
    _08(Jp.P3, 128), //
    _09(Jp.P4, 1), //
    _10(Jp.P4, 2), //
    _11(Jp.P4, 4), //
    _12(Jp.P4, 8), //
    _13(Jp.P4, 16), //
    _14(Jp.P4, 32), //
    _15(Jp.P4, 64), //
    _16(Jp.P4, 128), //
    _17(Jp.P5, 1);

    private final Jp jp;
    private final int internalValue;

    private Pin(final Jp jp, final int internalVal) {
        this.jp = jp;
        internalValue = internalVal;
    }

    public static ch.adrianelsener.train.denkovi.Pin forNumber(final String pinNumber) {
        for (final ch.adrianelsener.train.denkovi.Pin p : values()) {
            final String currentPinNumber = p.name().substring(1);
            if (Objects.equals(pinNumber, currentPinNumber)) {
                return p;
            }
        }
        throw new DenkoviWrapper.PinNotFoundException("Pin with id _%s could not be found", pinNumber);
    }

    /**
     * Get the Pin for a lable like P.10 -> Pin._10
     *
     * @param pinWithPDotNumber
     * @return
     */
    public static Pin forPDotNotation(final String pinWithPDotNumber) {
        if (StringUtils.startsWith(pinWithPDotNumber, "P.")) {
            return forNumber(pinWithPDotNumber.substring(2));
        } else {
            throw new IllegalArgumentException("value did not start with 'P.': '" + pinWithPDotNumber + "'");
        }
    }

    Jp getJp() {
        return jp;
    }

    private int getInternalValue() {
        return internalValue;
    }

    public String getOnValue(final int oldState) {
        return Integer.toString(oldState | getInternalValue());
    }

    public String getOffValue(final int oldState) {
        return Integer.toString(oldState & (255 - getInternalValue()));
    }

    byte getType() {
        return SnmpAPI.INTEGER;
    }

    public PinState on() {
        return PinState.Pon(this);
    }

    public PinState off() {
        return PinState.Poff(this);
    }
}