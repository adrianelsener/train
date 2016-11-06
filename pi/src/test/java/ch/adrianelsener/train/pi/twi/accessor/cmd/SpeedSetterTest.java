package ch.adrianelsener.train.pi.twi.accessor.cmd;

import ch.adrianelsener.train.pi.dto.properties.TwiDevice;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.hamcrest.core.IsCollectionContaining;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpeedSetterTest {
    @Mock
    private CmdExecutor executor;
    @Captor
    private ArgumentCaptor<ImmutableList<String>> parameterCaptor;

    @BeforeTest
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_speed_set_over_executor() {
        when(executor.toPrefixedHexString(anyInt())).thenAnswer(arg -> String.valueOf(arg.getArguments()[0]));
        final SpeedSetter testee = new SpeedSetter(42);
        final Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(final Binder binder) {
                binder.bind(CmdExecutor.class).toInstance(executor);
            }
        });
        injector.injectMembers(testee);
        TwiDevice twiDevice = mock(TwiDevice.class);
        when(twiDevice.getDeviceBusNr()).thenReturn(12);
        when(twiDevice.getSubDevice()).thenReturn(8);
        testee.onDevice(twiDevice);
        // assert
        verify(executor).executeSetCmd(parameterCaptor.capture());
        assertThat(parameterCaptor.getValue(), IsCollectionContaining.hasItems("42"));
    }
}