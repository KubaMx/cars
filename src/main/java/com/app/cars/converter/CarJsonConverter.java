package com.app.cars.converter;

import com.app.cars.persistence.model.Car;

import java.util.List;

public final class CarJsonConverter extends JsonConverterImpl<List<Car>> {
    public CarJsonConverter(String jsonFilename) { super(jsonFilename); }
}
