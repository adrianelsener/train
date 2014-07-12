package ch.adrianelsener.train.denkovi;

/**
* Created by els on 7/11/14.
*/
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
    }, P5 {
        @Override
        String getAddressInternal() {
//            ".1.3.6.1.4.1.19865.1.2.3.xxxx.0"
            return "3";
        }
    };

    abstract String getAddressInternal();

    public String getAddress() {
        return ".1.3.6.1.4.1.19865." + //
                "1.2." + getAddressInternal() + ".33.0";
    }
}
