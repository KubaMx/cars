package com.app.cars.service;

import com.app.cars.service.dto.CreateCarDto;

public interface CarsService {
    Long registerCar(CreateCarDto createCarDto);
}
