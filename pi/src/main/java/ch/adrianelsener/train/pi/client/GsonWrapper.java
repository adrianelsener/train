package ch.adrianelsener.train.pi.client;

import ch.adrianelsener.train.common.io.RuntimeIoException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class GsonWrapper {
    private final Gson gson = new Gson();

    public String toJson(final Object o) {
        return gson.toJson(o);
    }

    public <T> T fromJson(final InputStream inputStream, final Class<T> resultClass) {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(new DataInputStream(inputStream)));
        String line = null;
        try {
            line = inputReader.readLine();
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
        return gson.fromJson(line, resultClass);
    }

    public JsonElement toJsonTree(final Object o) {
        return gson.toJsonTree(o);
    }
}
