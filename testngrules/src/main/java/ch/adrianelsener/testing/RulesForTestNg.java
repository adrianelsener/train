package ch.adrianelsener.testing;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class RulesForTestNg {

    private enum MethodToInvoke {
        Starting {
            @Override
            protected boolean matchesName(final String methodName) {
                return "starting".equals(methodName);
            }
        }, Finished {
            @Override
            protected boolean matchesName(final String methodName) {
                return "finished".equals(methodName);
            }
        };
        public boolean isCorrectMethod(final Method method) {
            return matchesName(method.getName());
        }

        protected abstract boolean matchesName(final String methodName);
    }

    @BeforeMethod
    public void before() {
        invoke(MethodToInvoke.Starting);
    }

    @AfterMethod
    public void after() {
        invoke(MethodToInvoke.Finished);
    }

    private void invoke(final MethodToInvoke toInvoke) {
        final Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            final Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if ("org.junit.Rule".equals(annotation.annotationType().getName())) {
                    try {
                        field.setAccessible(true);
                        final Object o = field.get(this);
                        final Method[] methods = o.getClass().getMethods();
                        for (Method method : methods) {
                            if (toInvoke.isCorrectMethod(method)) {
                                method.setAccessible(true);
                                final Class<?> descriptionClass = Class.forName("org.junit.runner.Description");
                                final Object desc = descriptionClass.getDeclaredField("EMPTY").get(descriptionClass);
                                method.invoke(o, desc);
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchFieldException e) {
                        throw new IllegalArgumentException("Could not invoke " + toInvoke,e);
                    }
                }
            }
        }

    }
}
