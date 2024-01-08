package com.app.cars.repository;

import com.app.cars.converter.JsonConverter;
import com.app.cars.model.Car;
import com.app.cars.repository.exception.CarsRepositoryException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class CarsRepositoryImpl implements CarsRepository {

    private final JsonConverter<List<Car>> carJsonConverter;

    @Override
    public List<Car> getCars() {
        return carJsonConverter
                .from()
                .orElseThrow(() -> new CarsRepositoryException("Cannot parse Json data"));
    }
}
