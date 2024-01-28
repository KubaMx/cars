package com.app.cars.service;

import com.app.cars.model.Car;
import com.app.cars.model.Color;
import com.app.cars.repository.CarsRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
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

    public void statsPriceMileage() {
        var price = carsRepository
                .getCars()
                .stream()
                .collect(Collectors.summarizingDouble(x -> x.getPrice().doubleValue()));

        System.out.printf("Price -> MAX: %.3f, MIN: %.3f, AVG: %.3f%n", price.getMax(), price.getMin(), price.getAverage());

        var mileage = carsRepository
                .getCars()
                .stream()
                .collect(Collectors.summarizingInt(Car::getMileage));

        System.out.printf("Mileage -> MAX: %d, MIN: %d, AVG: %.3f%n", mileage.getMax(), mileage.getMin(), mileage.getAverage());
    }

    public List<Car> maxPriceCar() {
        var max = carsRepository
                .getCars()
                .stream()
                .map(Car::getPrice)
                .max(BigDecimal::compareTo)
                .orElseThrow();

        return carsRepository
                .getCars()
                .stream()
                .filter(x -> x.getPrice().equals(max))
                .toList();
    }

    // TODO mam wrażenie że można to zrobić prościej, bez uzycia buildera
    public List<Car> getCarsWithSortedComponents() {
        return carsRepository
                .getCars()
                .stream()
                .map(x ->
                        Car
                                .builder()
                                .color(x.getColor())
                                .mileage(x.getMileage())
                                .price(x.getPrice())
                                .model(x.getModel())
                                .components(x.getComponents().stream().sorted().toList())
                                .build())
                .toList();
    }

    public void /*Map<String, List<Car>>*/ groupedByComponent() {

        var components = carsRepository
                .getCars()
                .stream()
                .map(Car::getComponents)
                .flatMap(List::stream)
                .distinct()
                .toList();

        // TODO niby działa ale wygląda tragicznie, brak pomyslu jak to rozegrac streamami
        Map<String, List<Car>> byComponents = new HashMap<>();

        for (String component : components) {
            for (Car car : carsRepository.getCars()) {
                if (car.getComponents().contains(component)) {
                    if (byComponents.containsKey(component)) {
                        byComponents.get(component).add(car);
                    } else {
                        List<Car> cars = new ArrayList<>();
                        cars.add(car);
                        byComponents.put(component, cars);
                    }
                }
            }
        }

        System.out.println(byComponents);
    }

    public List<Car> getCarsWithinPriceRange(BigDecimal from, BigDecimal to) {
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
                .collect(Collectors.joining("\n"));
    }

}
