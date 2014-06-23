package sample;

import com.adventnet.snmp.beans.SnmpTarget;
import com.adventnet.snmp.snmp2.SnmpAPI;
import com.adventnet.snmp.snmp2.SnmpException;
import com.adventnet.snmp.snmp2.SnmpOID;
import com.adventnet.snmp.snmp2.SnmpPDU;
import com.adventnet.snmp.snmp2.SnmpSession;
import com.adventnet.snmp.snmp2.SnmpVar;
import com.adventnet.snmp.snmp2.SnmpVarBind;

public class MYSNMP {

    private SnmpAPI api;
    private SnmpSession session;
    private SnmpPDU pdu;

    public MYSNMP() {
        init();
    }

    private void init() {
        api = new SnmpAPI();
        session = new SnmpSession(api);
        // session.setCommunity(Community);
        // session.setRemotePort(Port);
        try {
            session.open();
        } catch (SnmpException e) {
            e.printStackTrace();
        }

    }

    public void close() {
        api.close();
        session.close();
        session = null;
        api = null;
    }

    /**
     * @param Port
     */
    public int SNMP_SET(String ipAddress, int Port, String OID, byte dataType, String SetValue, String Community) {
        if (null == api || null == session || null == pdu) {
            init();
        }

        pdu = new SnmpPDU();
        pdu.setRemoteHost(ipAddress);
        pdu.setAgentAddr(ipAddress);
        pdu.setCommunity(Community);
        pdu.setCommand(SnmpAPI.SET_REQ_MSG);
        SnmpOID oid = new SnmpOID(OID);
        SnmpVar var = null;
        try {
            var = SnmpVar.createVariable(SetValue, dataType);
        } catch (SnmpException e) {
            return 0;
        }
        SnmpVarBind varbind = new SnmpVarBind(oid, var);
        pdu.addVariableBinding(varbind);
        try {
            SnmpPDU result = session.syncSend(pdu);

            if (result == null) {
                return 0;
            }
        } catch (SnmpException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }

    public String SNMP_GET(String IPaddress, int Port, String OID, String Community) {
        SnmpTarget target = new SnmpTarget();
        target.setTargetHost(IPaddress);
        target.setTargetPort(Port);
        target.setCommunity(Community);
        target.setObjectID(OID);
        String result = target.snmpGet();
        return result;
    }

    public static void main(String args[]) {
    }
}
