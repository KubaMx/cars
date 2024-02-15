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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarsServiceTest {
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
    void testSortByPriceAscending() {
        List<Car> sortedCars = carsService.sortBy(Comparator.comparing(Car::getPrice), SortingOrder.ASCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("TOYOTA", sortedCars.get(0).getModel());
        assertEquals("AUDI", sortedCars.get(1).getModel());
        assertEquals("BMW", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByPriceDescending() {
        List<Car> sortedCars = carsService.sortBy(Comparator.comparing(Car::getPrice), SortingOrder.DESCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("BMW", sortedCars.get(0).getModel());
        assertEquals("AUDI", sortedCars.get(1).getModel());
        assertEquals("TOYOTA", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByMileageAscending() {
        List<Car> sortedCars = carsService.sortBy(Comparator.comparing(Car::getMileage), SortingOrder.ASCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("BMW", sortedCars.get(0).getModel());
        assertEquals("TOYOTA", sortedCars.get(1).getModel());
        assertEquals("AUDI", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByMileageDescending() {
        List<Car> sortedCars = carsService.sortBy(Comparator.comparing(Car::getMileage), SortingOrder.DESCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("AUDI", sortedCars.get(0).getModel());
        assertEquals("TOYOTA", sortedCars.get(1).getModel());
        assertEquals("BMW", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByModelAscending() {
        List<Car> sortedCars = carsService.sortBy(Comparator.comparing(Car::getModel), SortingOrder.ASCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("AUDI", sortedCars.get(0).getModel());
        assertEquals("BMW", sortedCars.get(1).getModel());
        assertEquals("TOYOTA", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByModelDescending() {
        List<Car> sortedCars = carsService.sortBy(Comparator.comparing(Car::getModel), SortingOrder.DESCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("TOYOTA", sortedCars.get(0).getModel());
        assertEquals("BMW", sortedCars.get(1).getModel());
        assertEquals("AUDI", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByColorAscending() {
        List<Car> sortedCars = carsService.sortBy(Comparator.comparing(Car::getColor), SortingOrder.ASCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("AUDI", sortedCars.get(0).getModel());
        assertEquals("BMW", sortedCars.get(1).getModel());
        assertEquals("TOYOTA", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByColorDescending() {
        List<Car> sortedCars = carsService.sortBy(Comparator.comparing(Car::getColor), SortingOrder.DESCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("TOYOTA", sortedCars.get(0).getModel());
        assertEquals("BMW", sortedCars.get(1).getModel());
        assertEquals("AUDI", sortedCars.get(2).getModel());
    }

    @Test
    void testMileageGreaterThan() {
        int mileageThreshold = 1000;

        List<Car> result = carsService.mileageGreaterThan(mileageThreshold);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("AUDI", result.getFirst().getModel());
    }

    @Test
    void testGroupByColor() {
        Map<Color, Long> result = carsService.groupByColor();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey(Color.BLACK));
        assertTrue(result.containsKey(Color.BLUE));
        assertTrue(result.containsKey(Color.RED));
        assertEquals(1, result.get(Color.BLACK));
        assertEquals(1, result.get(Color.BLUE));
        assertEquals(1, result.get(Color.RED));
    }

    @Test
    void testGetMostExpensiveCarByModel() {
        Map<String, Car> result = carsService.getMostExpensiveCarByModel();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey("AUDI"));
        assertTrue(result.containsKey("BMW"));
        assertTrue(result.containsKey("TOYOTA"));
        assertEquals(new BigDecimal(120000), result.get("AUDI").getPrice());
        assertEquals(new BigDecimal(130000), result.get("BMW").getPrice());
        assertEquals(new BigDecimal(100000), result.get("TOYOTA").getPrice());
    }

    @Test
    void testStatsPrice() {
        var result = carsService.statsPrice();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(new BigDecimal(100000), result.get("MIN"));
        assertEquals(new BigDecimal(130000), result.get("MAX"));
        assertEquals(new BigDecimal("116666.67"), result.get("AVG"));
    }

    @Test
    void testStatsMileage() {
        var result = carsService.statsMileage();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(200.0, result.get("MIN"));
        assertEquals(1200.0, result.get("MAX"));
        assertEquals(550.0, result.get("AVG"));
    }

    @Test
    void testMaxPriceCar() {
        List<Car> result = carsService.maxPriceCar();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BMW", result.getFirst().getModel());
    }

    @Test
    void testGetCarsWithSortedComponents() {
        List<Car> result = carsService.getCarsWithSortedComponents();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(List.of("ABS", "ESP"), result.get(0).getComponents());
        assertEquals(List.of("ABS", "ESP"), result.get(1).getComponents());
        assertEquals(List.of("ABS", "AC", "ESP"), result.get(2).getComponents());
    }

    @Test
    void testGroupByComponentSortedByCount() {
        Map<String, List<Car>> result = carsService.groupByComponentSortedByCount();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(List.of("AUDI", "BMW", "TOYOTA"), result.get("ABS").stream().map(Car::getModel).toList());
        assertEquals(List.of("AUDI", "BMW", "TOYOTA"), result.get("ESP").stream().map(Car::getModel).toList());
        assertEquals(List.of("TOYOTA"), result.get("AC").stream().map(Car::getModel).toList());
    }

    @Test
    void testGetCarsWithinPriceRangeSortedByModel() {
        BigDecimal from = new BigDecimal(120000);
        BigDecimal to = new BigDecimal(130000);

        List<Car> result = carsService.getCarsWithinPriceRangeSortedByModel(from, to);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("AUDI", result.get(0).getModel());
        assertEquals("BMW", result.get(1).getModel());
    }

    @Test
    void testToString() {
        String expected = "Car(model=AUDI, price=120000, color=BLACK, mileage=1200, components=[ABS, ESP])\n" +
                "Car(model=BMW, price=130000, color=BLUE, mileage=200, components=[ABS, ESP])\n" +
                "Car(model=TOYOTA, price=100000, color=RED, mileage=250, components=[ABS, ESP, AC])";

        String result = carsService.toString();

        assertNotNull(result);
        assertEquals(expected, result);
    }
}
