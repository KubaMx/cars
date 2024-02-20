package com.app.cars.persistence.repository;

import com.app.cars.converter.CarJsonConverter;
import com.app.cars.persistence.model.CarEntity;
import com.app.cars.persistence.model.type.Color;
import com.app.cars.persistence.repository.exception.CarsRepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarsRepositoryImplTest {
    @Mock
    private CarJsonConverter jsonConverterMock;

    private CarsRepositoryImpl carsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carsRepository = new CarsRepositoryImpl(jsonConverterMock);
    }

    @Test
    void testGetCarsSuccess() {

        List<CarEntity> mockCarsList = List.of(
                CarEntity.of("AUDI", new BigDecimal(120000), Color.BLACK, 1200, List.of("ABS", "ESP")),
                CarEntity.of("BMW", new BigDecimal(130000), Color.BLUE, 200, List.of("ABS", "ESP"))
        );

        when(jsonConverterMock.from()).thenReturn(Optional.of(mockCarsList));

        List<CarEntity> result = carsRepository.getCars();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("BMW", result.get(1).getModel());

        verify(jsonConverterMock, times(1)).from();
        verifyNoMoreInteractions(jsonConverterMock);
    }

    @Test
    void testGetCarsFailure() {
        when(jsonConverterMock.from()).thenReturn(Optional.empty());

        assertThrows(CarsRepositoryException.class, () -> carsRepository.getCars());

        verify(jsonConverterMock, times(1)).from();
        verifyNoMoreInteractions(jsonConverterMock);
    }
}
