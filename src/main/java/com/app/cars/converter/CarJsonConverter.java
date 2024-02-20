package com.app.cars.converter;

import com.app.cars.persistence.model.CarEntity;

import java.util.List;

public final class CarJsonConverter extends JsonConverterImpl<List<CarEntity>> {
    public CarJsonConverter(String jsonFilename) { super(jsonFilename); }
}
