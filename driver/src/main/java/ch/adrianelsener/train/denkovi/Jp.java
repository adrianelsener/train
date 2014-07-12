package ch.adrianelsener.train.denkovi;

/**
 * Created by els on 7/11/14.
 */
public enum Jp {
    P3 {
        @Override
        String getPortPart() {
            return "1";
        }

        @Override
        String getPinPart() {
            return "33";
        }
    }, //
    P4 {
        @Override
        String getPortPart() {
            return "2";
        }

        @Override
        String getPinPart() {
            return "33";
        }
    }, P5 {
        @Override
        String getPortPart() {
//            ".1.3.6.1.4.1.19865.1.2.3.xxxx.0"
//            ".1.3.6.1.4.1.19865.1.2.3.1.0"
//            ".1.3.6.1.4.1.19865.1.2.3.2.0"
            return "3";
        }

        @Override
        String getPinPart() {
            return "{}";
        }
    };

    abstract String getPortPart();

    abstract String getPinPart();

    public String getAddress() {
        return ".1.3.6.1.4.1.19865." + //
                "1.2." + getPortPart() + "." + getPinPart() + ".0";
    }
}
