package ch.adrianelsener.train.common.net;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.CombinableMatcher;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.net.InetAddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.mockito.Mockito.mock;

public class NetAddressTest {

    @Test
    public void create_inetAddressAndPort_bothIsSet() throws Exception {
        InetAddress inetAddress = mock(InetAddress.class);
        // act
        NetAddress result = NetAddress.create(inetAddress, 12);
        // assert
        assertThat(result, both(inetAddr(equalTo(inetAddress))).and(port(equalTo(12))));
    }

    @Test
    public void create_stringAddressAndPort_InetAddressAndPortIsSet() throws Exception {
        // act
        final NetAddress result = NetAddress.create("122.3.4.5", 7);
        // assert
        assertThat(result, both(inetAddr(host(equalTo("122.3.4.5")))).and(port(equalTo(7))));
    }

    private Matcher<InetAddress> host(Matcher<String> stringMatcher) {
        return new FeatureMatcher<InetAddress, String>(stringMatcher, "getHost", "getHost") {

            @Override
            protected String featureValueOf(InetAddress address) {
                return address.getHostName();
            }
        };
    }


    private Matcher<? super NetAddress> port(Matcher<Integer> integerMatcher) {
        return new FeatureMatcher<NetAddress, Integer>(integerMatcher, "getPort", "getPort") {
            @Override
            protected Integer featureValueOf(NetAddress netAddress) {
                return netAddress.getPort();
            }
        };
    }

    private Matcher<? super NetAddress> inetAddr(Matcher<InetAddress> inetAddressMatcher) {
        return new FeatureMatcher<NetAddress, InetAddress>(inetAddressMatcher, "getAddress", "getAddress") {
            @Override
            protected InetAddress featureValueOf(NetAddress netAddress) {
                return netAddress.getAddress();
            }
        };
    }
}