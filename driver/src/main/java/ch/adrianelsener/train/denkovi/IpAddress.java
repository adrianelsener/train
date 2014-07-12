package ch.adrianelsener.train.denkovi;

public class IpAddress {
    private final String address;

    IpAddress(final String address) {
        this.address = address;
    }

    public static IpAddress fromValue(final String address) {
        return new IpAddress(address);
    }

    public String getAddress() {
        return address;
    }
}
