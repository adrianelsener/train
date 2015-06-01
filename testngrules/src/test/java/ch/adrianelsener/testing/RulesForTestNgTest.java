package ch.adrianelsener.testing;

import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.testng.TestNG;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class RulesForTestNgTest {
    @Test
    public void junitRulesShouldBeExecuted() {
        TestNG testNg = new TestNG();
        Mockito.reset(TestWithJUnitRule.getTestWatcherMock());
        testNg.setTestClasses(new Class[]{TestWithJUnitRule.class});
        // Act
        testNg.run();
        //
        final MyWrappingTestWatcher testWatcherMock = TestWithJUnitRule.getTestWatcherMock();
        final InOrder inOrder = inOrder(testWatcherMock);
        inOrder.verify(testWatcherMock).starting(any(Description.class));
        inOrder.verify(testWatcherMock).finished(any(Description.class));
    }

    public static class MyWrappingTestWatcher extends TestWatcher {
        @Override
        public void starting(final Description description) {
            super.starting(description);
        }

        @Override
        public void finished(final Description description) {
            super.finished(description);
        }
    }

    public static class TestWithJUnitRule extends RulesForTestNg{
        private static final MyWrappingTestWatcher accessToRule = mock(MyWrappingTestWatcher.class);
        @Rule
        public TestWatcher rule = accessToRule;

        public static MyWrappingTestWatcher getTestWatcherMock() {
            return accessToRule;
        }

        @Test
        public void executedByTest() {
            // Noop
        }
    }
}
