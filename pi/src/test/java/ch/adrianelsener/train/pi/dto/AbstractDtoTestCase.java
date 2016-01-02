package ch.adrianelsener.train.pi.dto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

abstract class AbstractDtoTestCase {
    private final Gson gson = new Gson();

    protected abstract Object createTestee();

    @Test
    public void gson_serialize_deserialize() {
        // arrange
        final Object testee = createTestee();
        // act
        final Object result = serializeDeserialize(testee);
        // assert
        assertThat(result, is(equalTo(testee)));
    }

    private Object serializeDeserialize(final Object testee) {
        JsonElement jsonElement = gson.toJsonTree(testee);
        String json = gson.toJson(jsonElement);
        Class<?> testeeClass = testee.getClass();
        return gson.fromJson(json, testeeClass);
    }
}
