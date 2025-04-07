package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.impl.CurvePointServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurvePointServiceImplTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointServiceImpl curvePointService;

    private CurvePoint testCurvePoint;

    @BeforeEach
    void setUp() {
        // Create test data with all required fields
        testCurvePoint = new CurvePoint(1, 10.0, 20.0);
        testCurvePoint.setId(1);
    }

    @Test
    void save_ShouldSaveNewCurvePoint() {
        // Arrange
        CurvePoint curvePointToSave = new CurvePoint(1, 10.0, 20.0);
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(testCurvePoint);

        // Act
        CurvePoint savedCurvePoint = curvePointService.save(curvePointToSave);

        // Assert
        assertNotNull(savedCurvePoint);
        assertEquals(testCurvePoint.getId(), savedCurvePoint.getId());
        assertEquals(testCurvePoint.getCurveId(), savedCurvePoint.getCurveId());
        assertEquals(testCurvePoint.getTerm(), savedCurvePoint.getTerm());
        assertEquals(testCurvePoint.getValue(), savedCurvePoint.getValue());
        verify(curvePointRepository).save(any(CurvePoint.class));
    }

    @Test
    void save_WithNonNullId_ShouldThrowException() {
        // Arrange
        testCurvePoint.setId(1); // Setting ID to trigger validation

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> curvePointService.save(testCurvePoint));
        verify(curvePointRepository, never()).save(any(CurvePoint.class));
    }

    @Test
    void getById_ShouldReturnCurvePoint() {
        // Arrange
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(testCurvePoint));

        // Act
        CurvePoint foundCurvePoint = curvePointService.getById(1);

        // Assert
        assertNotNull(foundCurvePoint);
        assertEquals(testCurvePoint.getId(), foundCurvePoint.getId());
        assertEquals(testCurvePoint.getCurveId(), foundCurvePoint.getCurveId());
        verify(curvePointRepository).findById(1);
    }

    @Test
    void getById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(curvePointRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> curvePointService.getById(999));
        verify(curvePointRepository).findById(999);
    }

    @Test
    void update_ShouldUpdateExistingCurvePoint() {
        // Arrange
        CurvePoint updatedCurvePoint = new CurvePoint(2, 30.0, 40.0);
        updatedCurvePoint.setId(1);

        when(curvePointRepository.findById(1)).thenReturn(Optional.of(testCurvePoint));
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(updatedCurvePoint);

        // Act
        curvePointService.update(updatedCurvePoint);

        // Assert
        verify(curvePointRepository).findById(1);
        verify(curvePointRepository).save(any(CurvePoint.class));
    }

    @Test
    void update_WithNonExistentId_ShouldThrowException() {
        // Arrange
        CurvePoint nonExistentCurvePoint = new CurvePoint(1, 10.0, 20.0);
        nonExistentCurvePoint.setId(999);
        when(curvePointRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> curvePointService.update(nonExistentCurvePoint));
        verify(curvePointRepository).findById(999);
        verify(curvePointRepository, never()).save(any(CurvePoint.class));
    }

    @Test
    void delete_ShouldDeleteCurvePoint() {
        // Arrange
        doNothing().when(curvePointRepository).deleteById(1);

        // Act
        curvePointService.delete(1);

        // Assert
        verify(curvePointRepository).deleteById(1);
    }

    @Test
    void findAll_ShouldReturnAllCurvePoints() {
        // Arrange
        List<CurvePoint> expectedCurvePoints = Arrays.asList(
                testCurvePoint,
                new CurvePoint(2, 30.0, 40.0)
        );
        when(curvePointRepository.findAll()).thenReturn(expectedCurvePoints);

        // Act
        List<CurvePoint> actualCurvePoints = curvePointService.findAll();

        // Assert
        assertNotNull(actualCurvePoints);
        assertEquals(expectedCurvePoints.size(), actualCurvePoints.size());
        assertEquals(expectedCurvePoints.get(0).getCurveId(), actualCurvePoints.get(0).getCurveId());
        verify(curvePointRepository).findAll();
    }
}
