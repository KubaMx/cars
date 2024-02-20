package com.app.cars.validator;

import com.app.cars.persistence.model.CarEntity;

import java.math.BigDecimal;
import java.util.List;

public final class CarValidator implements Validator<CarEntity> {

    @Override
    public void validate(CarEntity car) {
        validateCar(car);
    }

    private void validateCar(CarEntity car) {
        ensureCarNotNull(car);
        validateCarModel(car);
        validateCarColor(car);
        validatePrice(car);
        validateCarMileage(car);
        validateCarComponents(car);
    }

    private void ensureCarNotNull(CarEntity car) {
        if (car == null) {
            throw new IllegalArgumentException("Car is null");
        }
    }

    private void validateCarModel(CarEntity car) {
        String model = car.getModel();

        if (model == null || !isValidCarModelFormat(model)) {
            throw new IllegalArgumentException("Invalid car model");
        }
    }

    private boolean isValidCarModelFormat(String model) {
        return Validator.validateExpression(model, "([A-Z]+(\\s)*)+");
    }

    private void validateCarColor(CarEntity car) {
        if (car.getColor() == null) {
            throw new IllegalArgumentException("Color is null");
        }
    }

    private void validatePrice(CarEntity car) {
        BigDecimal price = car.getPrice();
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price is null or not correct");
        }
    }

    private void validateCarMileage(CarEntity car) {
        if (car.getMileage() < 0) {
            throw new IllegalArgumentException("Mileage is not correct");
        }
    }

    private void validateCarComponents(CarEntity car) {
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
