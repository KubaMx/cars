package com.app.cars.repository;

import com.app.cars.model.Car;

import java.util.List;

public sealed interface CarsRepository permits CarsRepositoryImpl {
    List<Car> getCars();
}
