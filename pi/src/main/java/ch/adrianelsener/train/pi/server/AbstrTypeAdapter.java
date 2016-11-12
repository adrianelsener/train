package ch.adrianelsener.train.pi.server;

import com.google.gson.*;

import javax.json.JsonException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class AbstrTypeAdapter implements JsonSerializer, JsonDeserializer {

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getName()));
        JsonElement jsonElement = new Gson().toJsonTree(src, typeOfSrc);
        result.add("properties", jsonElement);
        return result;
    }


    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        try {
            if (Modifier.isAbstract(Class.forName(typeOfT.getTypeName()).getModifiers())) {
                String type = jsonObject.get("type").getAsString();
                JsonElement properties = jsonObject.get("properties");
                return context.deserialize(properties, Class.forName(type));
            } else {
                return new Gson().fromJson(json, typeOfT);
            }
        } catch (ClassNotFoundException e) {
            throw new JsonException("Could not instantiate Class " + typeOfT.getTypeName(), e);
        }
    }
}
