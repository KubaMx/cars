package com.app.cars.service;

import com.app.cars.persistence.model.CarEntity;
import com.app.cars.persistence.model.type.Color;
import com.app.cars.persistence.repository.CarsEntityRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Builder
public class CarsService {

    private final CarsEntityRepository carsRepository;

    // ta metoda ma tak działać! jest to zgodne z treścią zadania
    public List<CarEntity> sortBy(Comparator<CarEntity> compareBy, SortingOrder sortingOrder) {
        return carsRepository.findAll().stream()
                .sorted(sortingOrder == SortingOrder.ASCENDING ? compareBy : compareBy.reversed())
                .toList();
    }

    public List<CarEntity> mileageGreaterThan(int mileage) {
        return carsRepository.findAll().stream()
                .filter(car -> car.getMileage() > mileage)
                .toList();
    }

    public Map<Color, Long> groupByColor() {
        return carsRepository.findAll().stream()
                .collect(Collectors.groupingBy(CarEntity::getColor, Collectors.counting()))
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

    public Map<String, CarEntity> getMostExpensiveCarByModel() {
        return carsRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        CarEntity::getModel,
                        Collectors.maxBy(Comparator.comparing(CarEntity::getPrice))
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
        var cars = carsRepository.findAll();

        BigDecimal minPrice = cars.stream()
                .map(CarEntity::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal maxPrice = cars.stream()
                .map(CarEntity::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal avgPrice = cars.stream()
                .map(CarEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(cars.size()), 2, RoundingMode.HALF_EVEN);

        return Map.of(
                "MIN", minPrice,
                "MAX", maxPrice,
                "AVG", avgPrice
        );
    }

    public Map<String, Double> statsMileage() {
        var mileage = carsRepository.findAll().stream()
                .collect(Collectors.summarizingInt(CarEntity::getMileage));

        return Map.of(
                "MIN", mileage.getMin()*1.0,
                "MAX", mileage.getMax()*1.0,
                "AVG", mileage.getAverage()
        );
    }

    public List<CarEntity> maxPriceCar() {
        var max = carsRepository.findAll().stream()
                .map(CarEntity::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return carsRepository.findAll().stream()
                .filter(x -> x.getPrice().compareTo(max) == 0)
                .toList();
    }

    /*public List<CarEntity> getCarsWithSortedComponents() {
        return carsRepository
                .findAll()
                .stream()
                .map(car -> CarEntity.builder()
                        .id(car.getId())
                        .color(car.getColor())
                        .mileage(car.getMileage())
                        .price(car.getPrice())
                        .model(car.getModel())
                        .components(car.getComponents().stream().sorted().toList())
                        .build())
                .toList();
    }*/
    // TODO nie działa po zmianie z getCars na findAll

    public Map<String, List<CarEntity>> groupByComponentSortedByCount() {
        return carsRepository.findAll().stream()
                .flatMap(car -> car.getComponents().stream().map(component -> new AbstractMap.SimpleEntry<>(component, car)))
                .collect(Collectors.groupingBy(
                        AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, List<CarEntity>>comparingByValue(Comparator.comparingInt(List::size)).reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public List<CarEntity> getCarsWithinPriceRangeSortedByModel(BigDecimal from, BigDecimal to) {
        return carsRepository
                .findAll()
                .stream()
                .filter(c -> c.getPrice().compareTo(from) >= 0 && c.getPrice().compareTo(to) <= 0)
                .sorted(Comparator.comparing(CarEntity::getModel))
                .toList();
    }

    @Override
    public String toString() {
        return carsRepository
                .findAll()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

}
