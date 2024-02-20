package com.app.cars.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Car {

    private String model;
    private BigDecimal price;
    private Color color;
    private int mileage;
    private List<String> components;

    public static Car of(String model, BigDecimal price, Color color, int mileage, List<String> components) {
        return new Car(model, price, color, mileage, components);

        //TODO walidacja
    }
}
