package ch.adrianelsener.train.denkovi;

public interface Board {

    void close();

//    int SNMP_SET(int Port, Jp OID, byte dataType, String SetValue, String Community);
//
//    String SNMP_GET(int Port, Jp OID, String Community);

    void set(PinState pinState);

    int read(Pin pin);

    /**
     * If the board is ready for operation it will be return true
     **/
    boolean isReady();
}