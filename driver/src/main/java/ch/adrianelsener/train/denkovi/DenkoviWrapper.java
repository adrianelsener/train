package ch.adrianelsener.train.denkovi;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.adventnet.snmp.beans.SnmpTarget;
import com.adventnet.snmp.snmp2.SnmpAPI;
import com.adventnet.snmp.snmp2.SnmpException;
import com.adventnet.snmp.snmp2.SnmpOID;
import com.adventnet.snmp.snmp2.SnmpPDU;
import com.adventnet.snmp.snmp2.SnmpSession;
import com.adventnet.snmp.snmp2.SnmpVar;
import com.adventnet.snmp.snmp2.SnmpVarBind;

public class DenkoviWrapper implements Board {

    private SnmpAPI api;
    private SnmpSession session;
    private SnmpPDU pdu;
    private final IpAddress address;

    public DenkoviWrapper(final IpAddress address) {
        this.address = address;
        init();
    }

    public static class IpAddress {
        private final String address;

        private IpAddress(final String address) {
            this.address = address;
        }

        public static IpAddress fromValue(final String address) {
            return new IpAddress(address);
        }

        public String getAddress() {
            return address;
        }
    }

    private void init() {
        api = new SnmpAPI();
        session = new SnmpSession(api);
        // session.setCommunity(Community);
        // session.setRemotePort(Port);
        try {
            session.open();
        } catch (final SnmpException e) {
            e.printStackTrace();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.adrianelsener.train.denkovi.Board#close()
     */
    @Override
    public void close() {
        api.close();
        session.close();
        session = null;
        api = null;
    }

    public enum State {
        On {
            @Override
            public String getValue(final Pin relays, final int oldState) {
                return relays.getOnValue(oldState);
            }
        },
        Off {
            @Override
            public String getValue(final Pin relays, final int oldState) {
                return relays.getOffValue(oldState);
            }
        };
        public abstract String getValue(Pin relays, int oldState);
    }

    public enum Jp {
        P3 {
            @Override
            String getAddressInternal() {
                return "1";
            }
        }, //
        P4 {
            @Override
            String getAddressInternal() {
                return "2";
            }
        };

        abstract String getAddressInternal();

        public String getAddress() {
            return ".1.3.6.1.4.1.19865." + //
                    "1.2." + getAddressInternal() + ".33.0";
        }
    }

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
        ;

        private final Jp jp;
        private final int internalValue;

        private Pin(final Jp jp, final int internalVal) {
            this.jp = jp;
            internalValue = internalVal;
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

        public static Pin forNumber(final String pinNumber) {
            for (final Pin p : values()) {
                final String currentPinNumber = p.name().substring(1);
                if (Objects.equals(pinNumber, currentPinNumber)) {
                    return p;
                }
            }
            throw new PinNotFoundException("Pin with id _%s could not be found", pinNumber);
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
    }

    public static class PinNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public PinNotFoundException(final String string, final Object... objects) {
            super(String.format(string, objects));
        }

    }

    @Override
    public int SNMP_SET(final int Port, final Jp OID, final byte dataType, final String SetValue, final String Community) {
        if (null == api || null == session || null == pdu) {
            init();
        }

        pdu = new SnmpPDU();
        pdu.setRemoteHost(address.getAddress());
        pdu.setAgentAddr(address.getAddress());
        pdu.setCommunity(Community);
        pdu.setCommand(SnmpAPI.SET_REQ_MSG);
        final SnmpOID oid = new SnmpOID(OID.getAddress());
        SnmpVar var = null;
        try {
            var = SnmpVar.createVariable(SetValue, dataType);
        } catch (final SnmpException e) {
            return 0;
        }
        final SnmpVarBind varbind = new SnmpVarBind(oid, var);
        pdu.addVariableBinding(varbind);
        try {
            final SnmpPDU result = session.syncSend(pdu);

            if (result == null) {
                return 0;
            }
        } catch (final SnmpException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }

    @Override
    public String SNMP_GET(final int Port, final Jp OID, final String Community) {
        final SnmpTarget target = new SnmpTarget();
        target.setTargetHost(address.getAddress());
        target.setTargetPort(Port);
        target.setCommunity(Community);
        target.setObjectID(OID.getAddress());
        final String result = target.snmpGet();
        return result;
    }

    @Override
    public void set(final PinState relays) {
        final String snmp_GET = SNMP_GET(161, relays.pin.getJp(), "private");
        final int oldState = Integer.parseInt(snmp_GET);

        SNMP_SET(161, relays.pin.getJp(), relays.pin.getType(), relays.state.getValue(relays.pin, oldState), "private");
    }
}