package com.app.cars.service.impl;

import com.app.cars.persistence.repository.CarsEntityRepository;
import com.app.cars.service.CarsService;
import com.app.cars.service.dto.CreateCarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CarsServiceImpl implements CarsService {
    CarsEntityRepository carsEntityRepository;

    @Override
    public Long registerCar(CreateCarDto createCarDto) {
        var carToInsert = createCarDto.toCarEntity();

        var insertedCar = carsEntityRepository.save(carToInsert);

        return insertedCar.getId();
    }
}
