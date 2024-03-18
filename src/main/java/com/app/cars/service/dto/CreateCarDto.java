package com.app.cars.service.dto;

import com.app.cars.persistence.model.CarEntity;
import com.app.cars.persistence.model.type.Color;

import java.math.BigDecimal;
import java.util.List;

public record CreateCarDto(
        String model,
        BigDecimal price,
        Color color,
        int mileage,
        List<String> components
) {
    public CarEntity toCarEntity() {
        return CarEntity.builder()
                .model(model)
                .price(price)
                .color(color)
                .mileage(mileage)
                .components(components)
                .build();
    }
}

