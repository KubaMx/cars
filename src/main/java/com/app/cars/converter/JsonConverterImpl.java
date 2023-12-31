package com.app.cars.converter;

import com.app.cars.converter.exception.JsonConverterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public final class JsonConverterImpl<T> implements JsonConverter<T> {
    private final String jsonFilename;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public JsonConverterImpl(String jsonFilename) {
        this.jsonFilename = jsonFilename;
    }

    @Override
    public void to(T element) {
        try (FileWriter fileWriter = new FileWriter(jsonFilename)) {
            gson.toJson(element, fileWriter);
        } catch (Exception e) {
            throw new JsonConverterException(e.getMessage());
        }
    }

    @Override
    public Optional<T> from() {
        try (FileReader fileReader = new FileReader(jsonFilename)) {
            return Optional.of(gson.fromJson(fileReader, type));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

}
