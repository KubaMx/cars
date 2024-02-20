package com.app.cars.validator;

import com.app.cars.persistence.model.Car;

import java.math.BigDecimal;
import java.util.List;

public final class CarValidator implements Validator<Car> {

    @Override
    public void validate(Car car) {
        validateCar(car);
    }

    private void validateCar(Car car) {
        ensureCarNotNull(car);
        validateCarModel(car);
        validateCarColor(car);
        validatePrice(car);
        validateCarMileage(car);
        validateCarComponents(car);
    }

    private void ensureCarNotNull(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car is null");
        }
    }

    private void validateCarModel(Car car) {
        String model = car.getModel();

        if (model == null || !isValidCarModelFormat(model)) {
            throw new IllegalArgumentException("Invalid car model");
        }
    }

    private boolean isValidCarModelFormat(String model) {
        return Validator.validateExpression(model, "([A-Z]+(\\s)*)+");
    }

    private void validateCarColor(Car car) {
        if (car.getColor() == null) {
            throw new IllegalArgumentException("Color is null");
        }
    }

    private void validatePrice(Car car) {
        BigDecimal price = car.getPrice();
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price is null or not correct");
        }
    }

    private void validateCarMileage(Car car) {
        if (car.getMileage() < 0) {
            throw new IllegalArgumentException("Mileage is not correct");
        }
    }

    private void validateCarComponents(Car car) {
        List<String> components = car.getComponents();

        if (components == null || components.isEmpty()) {
            throw new IllegalArgumentException("Components are null or empty");
        }

        if (!components
                .stream()
                .allMatch(this::isValidComponentFormat)) {
            throw new IllegalArgumentException("Components are not correct");
        }
    }

    private boolean isValidComponentFormat(String component) {
        return Validator.validateExpression(component, "([A-Z]+(\\s)*)+");
    }
}
