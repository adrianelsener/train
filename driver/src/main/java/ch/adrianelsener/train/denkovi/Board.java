package ch.adrianelsener.train.denkovi;

import ch.adrianelsener.train.denkovi.DenkoviWrapper.Jp;

public interface Board {

    void close();

//    int SNMP_SET(int Port, Jp OID, byte dataType, String SetValue, String Community);
//
//    String SNMP_GET(int Port, Jp OID, String Community);

    void set(PinState pinState);


}