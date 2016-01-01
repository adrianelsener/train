package ch.adrianelsener.train.pi.dto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class AccelerationDtoTest {
    private Gson gson = new Gson();

    @Test
    public void gson_serialize_deserialize() {
        AccelerationDto testee = new AccelerationDto(17);
        JsonElement jsonElement = gson.toJsonTree(testee);
        String json = gson.toJson(jsonElement);
        AccelerationDto result = gson.fromJson(json, AccelerationDto.class);
        assertThat(result, is(equalTo(testee)));
    }
}
