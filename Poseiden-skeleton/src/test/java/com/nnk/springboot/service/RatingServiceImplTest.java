package com.nnk.springboot.service;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.impl.RatingServiceImpl;
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
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Rating testRating;

    @BeforeEach
    void setUp() {
        // Create test data with all required fields
        testRating = new Rating(
                "MoodysRating",
                "SandPRating",
                "FitchRating",
                1
        );
        testRating.setId(1);
    }

    @Test
    void save_ShouldSaveNewRating() {
        // Arrange
        Rating ratingToSave = new Rating(
                "MoodysRating",
                "SandPRating",
                "FitchRating",
                1
        );
        when(ratingRepository.save(any(Rating.class))).thenReturn(testRating);

        // Act
        Rating savedRating = ratingService.save(ratingToSave);

        // Assert
        assertNotNull(savedRating);
        assertEquals(testRating.getId(), savedRating.getId());
        assertEquals(testRating.getMoodysRating(), savedRating.getMoodysRating());
        assertEquals(testRating.getSandPRating(), savedRating.getSandPRating());
        assertEquals(testRating.getFitchRating(), savedRating.getFitchRating());
        assertEquals(testRating.getOrderNumber(), savedRating.getOrderNumber());
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    void save_WithNonNullId_ShouldThrowException() {
        // Arrange - Rating already has ID from setUp()

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ratingService.save(testRating));
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void getById_ShouldReturnRating() {
        // Arrange
        when(ratingRepository.findById(1)).thenReturn(Optional.of(testRating));

        // Act
        Rating foundRating = ratingService.getById(1);

        // Assert
        assertNotNull(foundRating);
        assertEquals(testRating.getId(), foundRating.getId());
        assertEquals(testRating.getMoodysRating(), foundRating.getMoodysRating());
        verify(ratingRepository).findById(1);
    }

    @Test
    void getById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(ratingRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> ratingService.getById(999));
        verify(ratingRepository).findById(999);
    }

    @Test
    void update_ShouldUpdateExistingRating() {
        // Arrange
        Rating updatedRating = new Rating(
                "UpdatedMoodys",
                "UpdatedSandP",
                "UpdatedFitch",
                2
        );
        updatedRating.setId(1);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(testRating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(updatedRating);

        // Act
        ratingService.update(updatedRating);

        // Assert
        verify(ratingRepository).findById(1);
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    void update_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Rating nonExistentRating = new Rating(
                "MoodysRating",
                "SandPRating",
                "FitchRating",
                1
        );
        nonExistentRating.setId(999);
        when(ratingRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> ratingService.update(nonExistentRating));
        verify(ratingRepository).findById(999);
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void delete_ShouldDeleteRating() {
        // Arrange
        doNothing().when(ratingRepository).deleteById(1);

        // Act
        ratingService.delete(1);

        // Assert
        verify(ratingRepository).deleteById(1);
    }

    @Test
    void findAll_ShouldReturnAllRatings() {
        // Arrange
        List<Rating> expectedRatings = Arrays.asList(
                testRating,
                new Rating("Moodys2", "SandP2", "Fitch2", 2)
        );
        when(ratingRepository.findAll()).thenReturn(expectedRatings);

        // Act
        List<Rating> actualRatings = ratingService.findAll();

        // Assert
        assertNotNull(actualRatings);
        assertEquals(expectedRatings.size(), actualRatings.size());
        assertEquals(expectedRatings.get(0).getMoodysRating(),
                actualRatings.get(0).getMoodysRating());
        verify(ratingRepository).findAll();
    }
}

