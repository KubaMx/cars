package com.app.cars.persistence.model;

import com.app.cars.persistence.model.type.Color;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name="cars")
public class CarEntity extends BaseEntity {
    private String model;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Color color;
    private int mileage;
    @ElementCollection
    private List<String> components;

    public static CarEntity of(String model, BigDecimal price, Color color, int mileage, List<String> components) {
        return new CarEntity(model, price, color, mileage, components);

        //TODO walidacja
    }
}
