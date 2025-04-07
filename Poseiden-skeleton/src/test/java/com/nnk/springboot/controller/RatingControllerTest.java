package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.DTO.RatingRequest;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.CrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CrudService<Rating> ratingService;

    @Autowired
    private RatingRepository ratingRepository;

    private Rating testRating;
    private RatingRequest testRatingRequest;

    @BeforeEach
    void setUp() {
        // Clean up test data before each test
        ratingRepository.deleteAll();

        // Create test rating
        testRating = new Rating(
                "MoodysTest",
                "SandPTest",
                "FitchTest",
                1
        );

        testRatingRequest = new RatingRequest(
                "MoodysTest",
                "SandPTest",
                "FitchTest",
                1
        );
    }

    // 1. Test home page access
    @Test
    @WithMockUser
    void home_ShouldReturnRatingListPage() throws Exception {
        // Create and save a test rating
        Rating savedRating = ratingService.save(testRating);

        mvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"));
    }

    // 2. Test add form display
    @Test
    @WithMockUser
    void addRatingForm_ShouldReturnAddPage() throws Exception {
        mvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    // 3. Test successful validation and save
    @Test
    @WithMockUser
    void validate_Success() throws Exception {
        mvc.perform(post("/rating/validate")
                        .param("moodysRating", "MoodysTest")
                        .param("sandPRating", "SandPTest")
                        .param("fitchRating", "FitchTest")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attribute("successMessage",
                        "the new rating has been saved succesfully"));
    }

    // 4. Test validation failure
    @Test
    @WithMockUser
    void validate_WithInvalidData_ShouldReturnToForm() throws Exception {
        mvc.perform(post("/rating/validate")
                        .param("moodysRating", "")
                        .param("sandPRating", "SandPTest")
                        .param("fitchRating", "FitchTest")
                        .param("orderNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().hasErrors());
    }

    // 5. Test show update form
    @Test
    @WithMockUser
    void showUpdateForm_Success() throws Exception {
        Rating savedRating = ratingService.save(testRating);

        mvc.perform(get("/rating/update/{id}", savedRating.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/rating/update"))
                .andExpect(model().attributeExists("ratingRequest"))
                .andExpect(model().attributeExists("ratingId"));
    }

    // 6. Test show update form with invalid ID
    @Test
    @WithMockUser
    void showUpdateForm_WithInvalidId_ShouldRedirectWithError() throws Exception {
        mvc.perform(get("/rating/update/{id}", 999))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("rating/update"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    // 7. Test successful update
    @Test
    @WithMockUser
    void updateRating_Success() throws Exception {
        Rating savedRating = ratingService.save(testRating);

        mvc.perform(post("/rating/update/{id}", savedRating.getId())
                        .param("moodysRating", "UpdatedMoodys")
                        .param("sandPRating", "UpdatedSandP")
                        .param("fitchRating", "UpdatedFitch")
                        .param("orderNumber", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attribute("successMessage",
                        "Rating updated succesfully for id = " + savedRating.getId()));
    }

    // 8. Test update with invalid data
    @Test
    @WithMockUser
    void updateRating_WithInvalidData_ShouldReturnToForm() throws Exception {
        Rating savedRating = ratingService.save(testRating);

        mvc.perform(post("/rating/update/{id}", savedRating.getId())
                        .param("moodysRating", "") // Invalid: empty rating
                        .param("sandPRating", "UpdatedSandP")
                        .param("fitchRating", "UpdatedFitch")
                        .param("orderNumber", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));
    }

    // 9. Test successful delete
    @Test
    @WithMockUser
    void deleteRating_Success() throws Exception {
        Rating savedRating = ratingService.save(testRating);

        mvc.perform(get("/rating/delete/{id}", savedRating.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attribute("successMessage",
                        "the rating with id = " + savedRating.getId() + " has been deleted succesfully"));
    }

    // 10. Test delete with non-existent ID
    @Test
    @WithMockUser
    void deleteRating_NonExistentId_ShouldHandleError() throws Exception {
        mvc.perform(get("/rating/delete/{id}", 999))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attribute("errorMessage",
                        "ERROR : the rating with id = 999 has not been deleted"));
    }

    // 11. Test authentication requirement
    @Test
    void accessWithoutAuthentication_ShouldRedirectToLogin() throws Exception {
        mvc.perform(get("/rating/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // 12. Test error handling during save
    @Test
    @WithMockUser
    void validate_WithServiceException_ShouldHandleError() throws Exception {

        mvc.perform(post("/rating/validate")
                        .param("moodysRating", "")
                        .param("sandPRating", "InvalidRating")
                        .param("fitchRating", "InvalidRating")
                        .param("orderNumber", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeHasErrors("ratingRequest"))
                .andExpect(model().attributeHasFieldErrors("ratingRequest", "moodysRating"))
                .andExpect(model().attributeHasFieldErrors("ratingRequest", "moodysRating"));
    }
}
