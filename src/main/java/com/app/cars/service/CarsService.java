package com.app.cars.service;

import com.app.cars.model.Car;
import com.app.cars.model.Color;
import com.app.cars.repository.CarsRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Builder
public class CarsService {

    private final CarsRepository carsRepository;

    // ta metoda ma tak działać! jest to zgodne z treścią zadania
    public List<Car> sortBy(Comparator<Car> compareBy, SortingOrder sortingOrder) {
        return carsRepository.getCars().stream()
                .sorted(sortingOrder == SortingOrder.ASCENDING ? compareBy : compareBy.reversed())
                .toList();
    }

    public List<Car> mileageGreaterThan(int mileage) {
        return carsRepository.getCars().stream()
                .filter(car -> car.getMileage() > mileage)
                .toList();
    }

    public Map<Color, Long> groupByColor() {
        return carsRepository.getCars().stream()
                .collect(Collectors.groupingBy(Car::getColor, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> x,
                        LinkedHashMap::new
                ));
        // WAZNE -> zeby mapa mogla przechowac posortowane wartosci musi byc LINKED
    }

    public Map<String, Car> getMostExpensiveCarByModel() {
        return carsRepository.getCars().stream()
                .collect(Collectors.groupingBy(
                        Car::getModel,
                        Collectors.maxBy(Comparator.comparing(Car::getPrice))
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        x -> x.getValue().orElseThrow(),
                        (x, y) -> x,
                        LinkedHashMap::new
                ));
    }

    public Map<String, BigDecimal> statsPrice() {
        var cars = carsRepository.getCars();

        BigDecimal minPrice = cars.stream()
                .map(Car::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal maxPrice = cars.stream()
                .map(Car::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal avgPrice = cars.stream()
                .map(Car::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(cars.size()), 2, RoundingMode.HALF_EVEN);

        return Map.of(
                "MIN", minPrice,
                "MAX", maxPrice,
                "AVG", avgPrice
        );
    }

    public Map<String, Double> statsMileage() {
        var mileage = carsRepository.getCars().stream()
                .collect(Collectors.summarizingInt(Car::getMileage));

        return Map.of(
                "MIN", mileage.getMin()*1.0,
                "MAX", mileage.getMax()*1.0,
                "AVG", mileage.getAverage()
        );
    }

    public List<Car> maxPriceCar() {
        var max = carsRepository.getCars().stream()
                .map(Car::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return carsRepository.getCars().stream()
                .filter(x -> x.getPrice().compareTo(max) == 0)
                .toList();
    }

    public List<Car> getCarsWithSortedComponents() {
        return carsRepository
                .getCars()
                .stream()
                .map(car -> Car.builder()
                        .color(car.getColor())
                        .mileage(car.getMileage())
                        .price(car.getPrice())
                        .model(car.getModel())
                        .components(car.getComponents().stream().sorted().toList())
                        .build())
                .toList();
    }

    public Map<String, List<Car>> groupByComponentSortedByCount() {
        return carsRepository.getCars().stream()
                .flatMap(car -> car.getComponents().stream().map(component -> new AbstractMap.SimpleEntry<>(component, car)))
                .collect(Collectors.groupingBy(
                        AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, List<Car>>comparingByValue(Comparator.comparingInt(List::size)).reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public List<Car> getCarsWithinPriceRangeSortedByModel(BigDecimal from, BigDecimal to) {
        return carsRepository
                .getCars()
                .stream()
                .filter(c -> c.getPrice().compareTo(from) >= 0 && c.getPrice().compareTo(to) <= 0)
                .sorted(Comparator.comparing(Car::getModel))
                .toList();
    }

    @Override
    public String toString() {
        return carsRepository
                .getCars()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

}
