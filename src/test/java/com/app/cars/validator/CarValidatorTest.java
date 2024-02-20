package com.app.cars.validator;

import com.app.cars.persistence.model.Car;
import com.app.cars.persistence.model.Color;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarValidatorTest {

    @Test
    void validate_ValidCar_CarIsCorrect() {
        // Arrange
        Car car = new Car("MODEL", new BigDecimal("10000"), Color.BLUE, 50000, List.of("AC", "ASR"));

        new CarValidator().validate(car);
    }

    @Test
    void validate_NullCar_ThrowsIllegalArgumentException() {
        // Arrange
        Car car = null;

        // Act & Assert
        CarValidator validator = new CarValidator();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(car));
        assertEquals("Car is null", exception.getMessage());
    }

    @Test
    void validate_InvalidModel_ThrowsIllegalArgumentException() {
        // Arrange
        Car car = new Car("model", new BigDecimal("10000"), Color.BLUE, 50000, List.of("AC", "ASR"));

        // Act & Assert
        CarValidator validator = new CarValidator();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(car));
        assertEquals("Invalid car model", exception.getMessage());
    }

    @Test
    void validate_NullColor_ThrowsIllegalArgumentException() {
        // Arrange
        Car car = new Car("MODEL", new BigDecimal("10000"), null, 50000, List.of("AC", "ASR"));

        // Act & Assert
        CarValidator validator = new CarValidator();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(car));
        assertEquals("Color is null", exception.getMessage());
    }

    @Test
    void validate_NegativePrice_ThrowsIllegalArgumentException() {
        // Arrange
        Car car = new Car("MODEL", new BigDecimal("-10000"), Color.BLUE, 50000, List.of("AC", "ASR"));

        // Act & Assert
        CarValidator validator = new CarValidator();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(car));
        assertEquals("Price is null or not correct", exception.getMessage());
    }

    @Test
    void validate_NegativeMileage_ThrowsIllegalArgumentException() {
        // Arrange
        Car car = new Car("MODEL", new BigDecimal("10000"), Color.BLUE, -50000, List.of("AC", "ASR"));

        // Act & Assert
        CarValidator validator = new CarValidator();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(car));
        assertEquals("Mileage is not correct", exception.getMessage());
    }

    @Test
    void validate_NullComponents_ThrowsIllegalArgumentException() {
        // Arrange
        Car car = new Car("MODEL", new BigDecimal("10000"), Color.BLUE, 50000, null);

        // Act & Assert
        CarValidator validator = new CarValidator();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(car));
        assertEquals("Components are null or empty", exception.getMessage());
    }

    @Test
    void validate_EmptyComponents_ThrowsIllegalArgumentException() {
        // Arrange
        Car car = new Car("MODEL", new BigDecimal("10000"), Color.BLUE, 50000, List.of());

        // Act & Assert
        CarValidator validator = new CarValidator();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(car));
        assertEquals("Components are null or empty", exception.getMessage());
    }

    @Test
    void validate_InvalidComponentFormat_ThrowsIllegalArgumentException() {
        // Arrange
        Car car = new Car("MODEL", new BigDecimal("10000"), Color.BLUE, 50000, List.of("Aasd", "ASR"));

        // Act & Assert
        CarValidator validator = new CarValidator();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(car));
        assertEquals("Components are not correct", exception.getMessage());
    }
}