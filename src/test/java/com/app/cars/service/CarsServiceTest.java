package com.app.cars.service;

import com.app.cars.model.Car;
import com.app.cars.model.Color;
import com.app.cars.repository.CarsRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CarsServiceTest {
    @Mock
    private CarsRepositoryImpl carsRepositoryMock;

    private CarsService carsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<Car> mockCarsList = List.of(
                Car.of("AUDI", new BigDecimal(120000), Color.BLACK, 1200, List.of("ABS", "ESP")),
                Car.of("BMW", new BigDecimal(130000), Color.BLUE, 200, List.of("ABS", "ESP")),
                Car.of("TOYOTA", new BigDecimal(100000), Color.RED, 250, List.of("ABS", "ESP", "AC"))
        );

        when(carsRepositoryMock.getCars()).thenReturn(mockCarsList);

        carsService = new CarsService(carsRepositoryMock);
    }
    @Test
    void sortBy() {
        List<Car> sortedCars = carsService.sortBy(Comparator.comparing(Car::getPrice), SortingOrder.ASCENDING);
        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("TOYOTA", sortedCars.get(0).getModel());
        assertEquals("AUDI", sortedCars.get(1).getModel());
        assertEquals("BMW", sortedCars.get(2).getModel());
    }

}
