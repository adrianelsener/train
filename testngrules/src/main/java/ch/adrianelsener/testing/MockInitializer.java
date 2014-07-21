package ch.adrianelsener.testing;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.MockitoAnnotations;

public abstract class MockInitializer extends TestWatcher {
    public static MockInitializer createMockitoInitializer(Object toInitialize) {
        return new MockitoInitializer(toInitialize);
    }

    private static class MockitoInitializer extends MockInitializer {

        private final Object toInitialize;

        MockitoInitializer(Object toInitialize) {
            this.toInitialize = toInitialize;
        }

        @Override
        protected void starting(Description description) {
            MockitoAnnotations.initMocks(toInitialize);
        }
    }
}
