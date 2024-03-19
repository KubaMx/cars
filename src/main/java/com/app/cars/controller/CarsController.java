package com.app.cars.controller;

import com.app.cars.service.CarsService;
import com.app.cars.service.dto.CreateCarDto;
import com.app.cars.service.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarsController {
    private final CarsService carsService;
    @PostMapping
    public ResponseDto<Long> registerCar(@RequestBody CreateCarDto createCarDto) {
        return new ResponseDto<>(carsService.registerCar(createCarDto));
    }

}
