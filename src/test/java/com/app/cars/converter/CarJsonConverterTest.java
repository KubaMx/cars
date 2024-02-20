package com.app.cars.converter;

import com.app.cars.persistence.model.CarEntity;
import com.app.cars.persistence.model.type.Color;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarJsonConverterTest {

    @Test
    void testCarJsonConverter() {
        // Arrange
        String jsonFilename = "test.json";
        CarJsonConverter carJsonConverter = new CarJsonConverter(jsonFilename);

        // Create some example cars
        CarEntity car1 = CarEntity.of("TOYOTA", new BigDecimal("25000.00"), Color.BLUE, 50000, List.of("AC", "NAVI"));
        CarEntity car2 = CarEntity.of("HONDA", new BigDecimal("18000.00"), Color.RED, 30000, List.of("AC"));
        List<CarEntity> cars = List.of(car1, car2);

        // Act
        carJsonConverter.to(cars);
        Optional<List<CarEntity>> loadedCarsOptional = carJsonConverter.from();

        // Assert
        assertTrue(loadedCarsOptional.isPresent());
        List<CarEntity> loadedCars = loadedCarsOptional.get();

        assertEquals(2, loadedCars.size());
        assertEquals("TOYOTA", loadedCars.get(0).getModel());
        assertEquals(new BigDecimal("25000.00"), loadedCars.get(0).getPrice());
        assertEquals(Color.BLUE, loadedCars.get(0).getColor());
        assertEquals(50000, loadedCars.get(0).getMileage());
        assertEquals(List.of("AC", "NAVI"), loadedCars.get(0).getComponents());

        assertEquals("HONDA", loadedCars.get(1).getModel());
        assertEquals(new BigDecimal("18000.00"), loadedCars.get(1).getPrice());
        assertEquals(Color.RED, loadedCars.get(1).getColor());
        assertEquals(30000, loadedCars.get(1).getMileage());
        assertEquals(List.of("AC"), loadedCars.get(1).getComponents());
    }
}
