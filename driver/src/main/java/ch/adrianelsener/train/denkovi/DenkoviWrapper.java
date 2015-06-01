package ch.adrianelsener.train.denkovi;

import com.adventnet.snmp.beans.SnmpTarget;
import com.adventnet.snmp.snmp2.SnmpAPI;
import com.adventnet.snmp.snmp2.SnmpException;
import com.adventnet.snmp.snmp2.SnmpOID;
import com.adventnet.snmp.snmp2.SnmpPDU;
import com.adventnet.snmp.snmp2.SnmpSession;
import com.adventnet.snmp.snmp2.SnmpVar;
import com.adventnet.snmp.snmp2.SnmpVarBind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DenkoviWrapper implements Board {
    private final static Logger logger = LoggerFactory.getLogger(DenkoviWrapper.class);
    private final IpAddress address;
    private SnmpAPI api;
    private SnmpSession session;
    private SnmpPDU pdu;

    public DenkoviWrapper(final IpAddress address) {
        this.address = address;
        init();
    }

    private void init() {
        api = new SnmpAPI();
        session = new SnmpSession(api);
        // session.setCommunity(Community);
        // session.setRemotePort(Port);l
        try {
            session.open();
        } catch (final SnmpException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        api.close();
        session.close();
        session = null;
        api = null;
    }

    int SNMP_SET(final int Port, final Jp OID, final byte dataType, final String SetValue, final String Community) {
        if (null == api || null == session || null == pdu) {
            init();
        }

        pdu = new SnmpPDU();
        pdu.setRemoteHost(address.getAddress());
        pdu.setAgentAddr(address.getAddress());
        pdu.setCommunity(Community);
        pdu.setCommand(SnmpAPI.SET_REQ_MSG);
        final SnmpOID oid = new SnmpOID(OID.getAddress());
        final SnmpVar var;
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

    String SNMP_GET(final int Port, final Jp OID, final String Community) {
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
        if (null == snmp_GET) {
            logger.error("Got no value as response from targer!!!");
        } else {
            final int oldState = Integer.parseInt(snmp_GET);
            SNMP_SET(161, relays.pin.getJp(), relays.pin.getType(), relays.state.getValue(relays.pin, oldState), "private");
        }
    }

    @Override
    public int read(Pin pin) {
        return 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    public static class PinNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public PinNotFoundException(final String string, final Object... objects) {
            super(String.format(string, objects));
        }

    }
}
