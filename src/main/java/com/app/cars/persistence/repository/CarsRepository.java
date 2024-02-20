package com.app.cars.persistence.repository;

import com.app.cars.persistence.model.Car;

import java.util.List;

public sealed interface CarsRepository permits CarsRepositoryImpl {
    List<Car> getCars();
}
