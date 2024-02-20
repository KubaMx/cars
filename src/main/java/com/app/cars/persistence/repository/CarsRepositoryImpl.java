package com.app.cars.persistence.repository;

import com.app.cars.converter.JsonConverter;
import com.app.cars.persistence.model.CarEntity;
import com.app.cars.persistence.repository.exception.CarsRepositoryException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class CarsRepositoryImpl implements CarsRepository {

    private final JsonConverter<List<CarEntity>> carJsonConverter;

    @Override
    public List<CarEntity> getCars() {
        return carJsonConverter
                .from()
                .orElseThrow(() -> new CarsRepositoryException("Cannot parse Json data"));
    }
}
