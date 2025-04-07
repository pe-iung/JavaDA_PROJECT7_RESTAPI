package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.impl.BidServiceImpl;
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
class BidServiceImplTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidServiceImpl bidService;

    private BidList testBid;

    @BeforeEach
    void setUp() {
        testBid = new BidList("Account Test", "Type Test", 10d);
        testBid.setBidListId(1); // Set ID for testing
    }

    @Test
    void save_ShouldSaveNewBid() {
        // Arrange
        BidList bidToSave = new BidList("Account Test", "Type Test", 10d);
        when(bidListRepository.save(any(BidList.class))).thenReturn(testBid);

        // Act
        BidList savedBid = bidService.save(bidToSave);

        // Assert
        assertNotNull(savedBid);
        assertEquals(testBid.getBidListId(), savedBid.getBidListId());
        assertEquals(testBid.getAccount(), savedBid.getAccount());
        verify(bidListRepository).save(any(BidList.class));
    }

    @Test
    void save_WithNonNullId_ShouldThrowException() {
        // Arrange
        testBid.setBidListId(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bidService.save(testBid));
        verify(bidListRepository, never()).save(any(BidList.class));
    }

    @Test
    void getById_ShouldReturnBid() {
        // Arrange
        when(bidListRepository.findById(1)).thenReturn(Optional.of(testBid));

        // Act
        BidList foundBid = bidService.getById(1);

        // Assert
        assertNotNull(foundBid);
        assertEquals(testBid.getBidListId(), foundBid.getBidListId());
        verify(bidListRepository).findById(1);
    }

    @Test
    void getById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(bidListRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> bidService.getById(999));
        verify(bidListRepository).findById(999);
    }

    @Test
    void update_ShouldUpdateExistingBid() {
        // Arrange
        BidList updatedBid = new BidList("Updated Account", "Updated Type", 20d);
        updatedBid.setBidListId(1);

        when(bidListRepository.findById(1)).thenReturn(Optional.of(testBid));
        when(bidListRepository.save(any(BidList.class))).thenReturn(updatedBid);

        // Act
        bidService.update(updatedBid);

        // Assert
        verify(bidListRepository).findById(1);
        verify(bidListRepository).save(any(BidList.class));
    }

    @Test
    void delete_ShouldDeleteBid() {
        // Arrange
        doNothing().when(bidListRepository).deleteById(1);

        // Act
        bidService.delete(1);

        // Assert
        verify(bidListRepository).deleteById(1);
    }

    @Test
    void findAll_ShouldReturnAllBids() {
        // Arrange
        List<BidList> expectedBids = Arrays.asList(
                testBid,
                new BidList("Account 2", "Type 2", 20d)
        );
        when(bidListRepository.findAll()).thenReturn(expectedBids);

        // Act
        List<BidList> actualBids = bidService.findAll();

        // Assert
        assertNotNull(actualBids);
        assertEquals(expectedBids.size(), actualBids.size());
        verify(bidListRepository).findAll();
    }
}