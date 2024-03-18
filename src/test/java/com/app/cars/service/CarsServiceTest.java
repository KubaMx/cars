package com.app.cars.service;

import com.app.cars.persistence.model.CarEntity;
import com.app.cars.persistence.model.type.Color;
import com.app.cars.persistence.repository.CarsEntityRepository;
import com.app.cars.service.impl.CarsService1;
import com.app.cars.service.type.SortingOrder;
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
    private CarsEntityRepository carsEntityRepository;

    private CarsService1 carsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<CarEntity> mockCarsList = List.of(
                CarEntity.of("AUDI", new BigDecimal(120000), Color.BLACK, 1200, List.of("ABS", "ESP")),
                CarEntity.of("BMW", new BigDecimal(130000), Color.BLUE, 200, List.of("ABS", "ESP")),
                CarEntity.of("TOYOTA", new BigDecimal(100000), Color.RED, 250, List.of("ABS", "ESP", "AC"))
        );

        when(carsEntityRepository.findAll()).thenReturn(mockCarsList);

        carsService = new CarsService1(carsEntityRepository);
    }

    @Test
    void testSortByPriceAscending() {
        List<CarEntity> sortedCars = carsService.sortBy(Comparator.comparing(CarEntity::getPrice), SortingOrder.ASCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("TOYOTA", sortedCars.get(0).getModel());
        assertEquals("AUDI", sortedCars.get(1).getModel());
        assertEquals("BMW", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByPriceDescending() {
        List<CarEntity> sortedCars = carsService.sortBy(Comparator.comparing(CarEntity::getPrice), SortingOrder.DESCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("BMW", sortedCars.get(0).getModel());
        assertEquals("AUDI", sortedCars.get(1).getModel());
        assertEquals("TOYOTA", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByMileageAscending() {
        List<CarEntity> sortedCars = carsService.sortBy(Comparator.comparing(CarEntity::getMileage), SortingOrder.ASCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("BMW", sortedCars.get(0).getModel());
        assertEquals("TOYOTA", sortedCars.get(1).getModel());
        assertEquals("AUDI", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByMileageDescending() {
        List<CarEntity> sortedCars = carsService.sortBy(Comparator.comparing(CarEntity::getMileage), SortingOrder.DESCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("AUDI", sortedCars.get(0).getModel());
        assertEquals("TOYOTA", sortedCars.get(1).getModel());
        assertEquals("BMW", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByModelAscending() {
        List<CarEntity> sortedCars = carsService.sortBy(Comparator.comparing(CarEntity::getModel), SortingOrder.ASCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("AUDI", sortedCars.get(0).getModel());
        assertEquals("BMW", sortedCars.get(1).getModel());
        assertEquals("TOYOTA", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByModelDescending() {
        List<CarEntity> sortedCars = carsService.sortBy(Comparator.comparing(CarEntity::getModel), SortingOrder.DESCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("TOYOTA", sortedCars.get(0).getModel());
        assertEquals("BMW", sortedCars.get(1).getModel());
        assertEquals("AUDI", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByColorAscending() {
        List<CarEntity> sortedCars = carsService.sortBy(Comparator.comparing(CarEntity::getColor), SortingOrder.ASCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("AUDI", sortedCars.get(0).getModel());
        assertEquals("BMW", sortedCars.get(1).getModel());
        assertEquals("TOYOTA", sortedCars.get(2).getModel());
    }

    @Test
    void testSortByColorDescending() {
        List<CarEntity> sortedCars = carsService.sortBy(Comparator.comparing(CarEntity::getColor), SortingOrder.DESCENDING);

        assertNotNull(sortedCars);
        assertEquals(3, sortedCars.size());
        assertEquals("TOYOTA", sortedCars.get(0).getModel());
        assertEquals("BMW", sortedCars.get(1).getModel());
        assertEquals("AUDI", sortedCars.get(2).getModel());
    }

    @Test
    void testMileageGreaterThan() {
        int mileageThreshold = 1000;

        List<CarEntity> result = carsService.mileageGreaterThan(mileageThreshold);

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
        Map<String, CarEntity> result = carsService.getMostExpensiveCarByModel();

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
        List<CarEntity> result = carsService.maxPriceCar();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BMW", result.getFirst().getModel());
    }



    // TODO nie działa po zmianie z getCars na findAll
  /*  @Test
    void testGetCarsWithSortedComponents() {
        List<CarEntity> result = carsService.getCarsWithSortedComponents();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(List.of("ABS", "ESP"), result.get(0).getComponents());
        assertEquals(List.of("ABS", "ESP"), result.get(1).getComponents());
        assertEquals(List.of("ABS", "AC", "ESP"), result.get(2).getComponents());
    }*/

    @Test
    void testGroupByComponentSortedByCount() {
        Map<String, List<CarEntity>> result = carsService.groupByComponentSortedByCount();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(List.of("AUDI", "BMW", "TOYOTA"), result.get("ABS").stream().map(CarEntity::getModel).toList());
        assertEquals(List.of("AUDI", "BMW", "TOYOTA"), result.get("ESP").stream().map(CarEntity::getModel).toList());
        assertEquals(List.of("TOYOTA"), result.get("AC").stream().map(CarEntity::getModel).toList());
    }

    @Test
    void testGetCarsWithinPriceRangeSortedByModel() {
        BigDecimal from = new BigDecimal(120000);
        BigDecimal to = new BigDecimal(130000);

        List<CarEntity> result = carsService.getCarsWithinPriceRangeSortedByModel(from, to);

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
