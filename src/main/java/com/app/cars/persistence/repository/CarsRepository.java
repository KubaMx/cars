package com.app.cars.persistence.repository;

import com.app.cars.persistence.model.CarEntity;

import java.util.List;

public sealed interface CarsRepository permits CarsRepositoryImpl {
    List<CarEntity> getCars();
}
