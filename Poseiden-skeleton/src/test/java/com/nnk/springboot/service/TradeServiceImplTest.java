package com.nnk.springboot.service;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.impl.TradeServiceImpl;
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
class TradeServiceImplTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeServiceImpl tradeService;

    private Trade testTrade;

    @BeforeEach
    void setUp() {
        // Create test data with all required fields
        testTrade = new Trade(
                "TestAccount",
                "TestType",
                100.0,  // buyQuantity
                50.0,   // sellQuantity
                200.0,  // buyPrice
                150.0   // sellPrice
        );
        testTrade.setTradeId(1);
    }

    @Test
    void save_ShouldSaveNewTrade() {
        // Arrange
        Trade tradeToSave = new Trade(
                "TestAccount",
                "TestType",
                100.0,
                50.0,
                200.0,
                150.0
        );
        when(tradeRepository.save(any(Trade.class))).thenReturn(testTrade);

        // Act
        Trade savedTrade = tradeService.save(tradeToSave);

        // Assert
        assertNotNull(savedTrade);
        assertEquals(testTrade.getTradeId(), savedTrade.getTradeId());
        assertEquals(testTrade.getAccount(), savedTrade.getAccount());
        assertEquals(testTrade.getType(), savedTrade.getType());
        assertEquals(testTrade.getBuyQuantity(), savedTrade.getBuyQuantity());
        assertEquals(testTrade.getSellQuantity(), savedTrade.getSellQuantity());
        assertEquals(testTrade.getBuyPrice(), savedTrade.getBuyPrice());
        assertEquals(testTrade.getSellPrice(), savedTrade.getSellPrice());
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    void save_WithNonNullId_ShouldThrowException() {
        // Arrange - Trade already has ID from setUp()

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> tradeService.save(testTrade));
        verify(tradeRepository, never()).save(any(Trade.class));
    }

    @Test
    void getById_ShouldReturnTrade() {
        // Arrange
        when(tradeRepository.findById(1)).thenReturn(Optional.of(testTrade));

        // Act
        Trade foundTrade = tradeService.getById(1);

        // Assert
        assertNotNull(foundTrade);
        assertEquals(testTrade.getTradeId(), foundTrade.getTradeId());
        assertEquals(testTrade.getAccount(), foundTrade.getAccount());
        verify(tradeRepository).findById(1);
    }

    @Test
    void getById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(tradeRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> tradeService.getById(999));
        verify(tradeRepository).findById(999);
    }

    @Test
    void update_ShouldUpdateExistingTrade() {
        // Arrange
        Trade updatedTrade = new Trade(
                "UpdatedAccount",
                "UpdatedType",
                150.0,
                75.0,
                250.0,
                200.0
        );
        updatedTrade.setTradeId(1);

        when(tradeRepository.findById(1)).thenReturn(Optional.of(testTrade));
        when(tradeRepository.save(any(Trade.class))).thenReturn(updatedTrade);

        // Act
        tradeService.update(updatedTrade);

        // Assert
        verify(tradeRepository).findById(1);
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    void update_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Trade nonExistentTrade = new Trade(
                "TestAccount",
                "TestType",
                100.0,
                50.0,
                200.0,
                150.0
        );
        nonExistentTrade.setTradeId(999);
        when(tradeRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> tradeService.update(nonExistentTrade));
        verify(tradeRepository).findById(999);
        verify(tradeRepository, never()).save(any(Trade.class));
    }

    @Test
    void delete_ShouldDeleteTrade() {
        // Arrange
        doNothing().when(tradeRepository).deleteById(1);

        // Act
        tradeService.delete(1);

        // Assert
        verify(tradeRepository).deleteById(1);
    }

    @Test
    void findAll_ShouldReturnAllTrades() {
        // Arrange
        List<Trade> expectedTrades = Arrays.asList(
                testTrade,
                new Trade("Account2", "Type2", 200.0, 100.0, 300.0, 250.0)
        );
        when(tradeRepository.findAll()).thenReturn(expectedTrades);

        // Act
        List<Trade> actualTrades = tradeService.findAll();

        // Assert
        assertNotNull(actualTrades);
        assertEquals(expectedTrades.size(), actualTrades.size());
        assertEquals(expectedTrades.get(0).getAccount(), actualTrades.get(0).getAccount());
        verify(tradeRepository).findAll();
    }

    @Test
    void update_ShouldUpdateAllFields() {
        // Arrange
        Trade existingTrade = testTrade;
        Trade updatedTrade = new Trade(
                "UpdatedAccount",
                "UpdatedType",
                150.0,
                75.0,
                250.0,
                200.0
        );
        updatedTrade.setTradeId(1);

        when(tradeRepository.findById(1)).thenReturn(Optional.of(existingTrade));
        when(tradeRepository.save(any(Trade.class))).thenReturn(updatedTrade);

        // Act
        tradeService.update(updatedTrade);

        // Assert
        verify(tradeRepository).save(any(Trade.class));
        verify(tradeRepository).findById(1);
    }
}
