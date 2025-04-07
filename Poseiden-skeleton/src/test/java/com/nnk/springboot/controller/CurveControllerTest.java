package com.nnk.springboot.controller;

import com.nnk.springboot.domain.CurvePoint;
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
class CurveControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CrudService<CurvePoint> curvePointService;

    private CurvePoint testCurvePoint;

    @BeforeEach
    void setUp() {
        // Clean up test data before each test
        //curvePointService.deleteAll();

        // Create test curve point
        testCurvePoint = new CurvePoint(1, 10.0, 30.0);
    }

    // 1. Test home page access
    @Test
    @WithMockUser
    void home_ShouldReturnCurvePointListPage() throws Exception {
        // Create and save a test curve point
        CurvePoint savedCurvePoint = curvePointService.save(testCurvePoint);

        mvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"));
    }

    // 2. Test add form display
    @Test
    @WithMockUser
    void addCurveForm_ShouldReturnAddPage() throws Exception {
        mvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    // 3. Test successful validation and save
    @Test
    @WithMockUser
    void validate_Success() throws Exception {
        mvc.perform(post("/curvePoint/validate")
                        .param("curveId", "1")
                        .param("term", "10.0")
                        .param("value", "30.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    // 4. Test validation failure
    @Test
    @WithMockUser
    void validate_WithInvalidData_ShouldReturnToForm() throws Exception {
        mvc.perform(post("/curvePoint/validate")
                        .param("curveId", "-1") // Invalid: negative value
                        .param("term", "10.0")
                        .param("value", "30.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().hasErrors());
    }

    // 5. Test show update form
    @Test
    @WithMockUser
    void showUpdateForm_Success() throws Exception {
        CurvePoint savedCurvePoint = curvePointService.save(testCurvePoint);

        mvc.perform(get("/curvePoint/update/{id}", savedCurvePoint.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePointRequest"))
                .andExpect(model().attributeExists("curvePointId"));
    }

    // 6. Test successful update
    @Test
    @WithMockUser
    void updateCurvePoint_Success() throws Exception {
        CurvePoint savedCurvePoint = curvePointService.save(testCurvePoint);

        mvc.perform(post("/curvePoint/update/{id}", savedCurvePoint.getId())
                        .param("curveId", "2")
                        .param("term", "20.0")
                        .param("value", "40.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    // 7. Test update with invalid data
    @Test
    @WithMockUser
    void updateCurvePoint_WithInvalidData_ShouldReturnToForm() throws Exception {
        CurvePoint savedCurvePoint = curvePointService.save(testCurvePoint);

        mvc.perform(post("/curvePoint/update/{id}", savedCurvePoint.getId())
                        .param("curveId", "-1") // Invalid: negative value
                        .param("term", "20.0")
                        .param("value", "40.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().hasErrors());
    }

    // 8. Test successful delete
    @Test
    @WithMockUser
    void deleteCurvePoint_Success() throws Exception {
        CurvePoint savedCurvePoint = curvePointService.save(testCurvePoint);

        mvc.perform(get("/curvePoint/delete/{id}", savedCurvePoint.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    // 9. Test delete with non-existent ID
    @Test
    @WithMockUser
    void deleteCurvePoint_NonExistentId_ShouldHandleError() throws Exception {
        mvc.perform(get("/curvePoint/delete/{id}", 99999))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    // 10. Test authentication requirement
    @Test
    void accessWithoutAuthentication_ShouldRedirectToLogin() throws Exception {
        mvc.perform(get("/curvePoint/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // 11. Test flash messages for successful operations
    @Test
    @WithMockUser
    void operations_ShouldSetAppropriateFlashMessages() throws Exception {
        // Test add success message
        mvc.perform(post("/curvePoint/validate")
                        .param("curveId", "1")
                        .param("term", "10.0")
                        .param("value", "30.0"))
                .andExpect(flash().attribute("successMessage",
                        "The new curvePoint has been added succesfully !"));

        // Save a curve point for delete testing
        CurvePoint savedCurvePoint = curvePointService.save(testCurvePoint);

        // Test delete success message
        mvc.perform(get("/curvePoint/delete/{id}", savedCurvePoint.getId()))
                .andExpect(flash().attribute("successMessage",
                        "curvePoint.id " + savedCurvePoint.getId() + " has been deleted successfully"));
    }
}
