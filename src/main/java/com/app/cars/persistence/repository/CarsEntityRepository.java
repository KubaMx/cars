package com.app.cars.persistence.repository;

import com.app.cars.persistence.model.CarEntity;
import com.app.cars.persistence.model.type.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface CarsEntityRepository extends JpaRepository<CarEntity, Long> {
    List<CarEntity> findByModel(String model);
    List<CarEntity> findByColor(Color color);
    List<CarEntity> findByMileageBetween(int from, int to);
    List<CarEntity> findByPriceBetween(BigDecimal from, BigDecimal to);





}
