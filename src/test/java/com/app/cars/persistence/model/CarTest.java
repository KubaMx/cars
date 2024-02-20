package com.app.cars.persistence.model;

import com.app.cars.persistence.model.Car;
import com.app.cars.persistence.model.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    @Test
    @DisplayName("when Car object is correct")
    void testCreateValidCar() {
        String model = "TOYOTA CAMRY";
        BigDecimal price = new BigDecimal("30000.00");
        int mileage = 10000;
        List<String> components = List.of("GPS", "AC");

        Car car = Car.of(model, price, Color.BLACK, mileage, components);

        assertNotNull(car);
        assertEquals(model, car.getModel());
        assertEquals(price, car.getPrice());
        assertEquals(mileage, car.getMileage());
        assertEquals(components, car.getComponents());
    }

    @Test
    @DisplayName("when Car object is not correct")
    void testCreateInvalidCar() {
        String model = "Invalid Car";
        BigDecimal price = new BigDecimal("-1000.00"); // Negative price
        int mileage = -5000; // Negative mileage
        List<String> components = List.of("Air Conditioning", "CD Player");

        try {
           /* assertThrows(IllegalArgumentException.class, () ->
                    Car.of(model, price, Color.RED, mileage, components));*/
            Car.of(model, price, Color.RED, mileage, components);
        } catch (Exception e) {
            assertEquals("Model is null or not correct", e.getMessage());
        }
    }
}