package com.nnk.springboot.controller;

import com.nnk.springboot.controllers.DTO.TradeRequest;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
class TradeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CrudService<Trade> tradeService;

    @Autowired
    private TradeRepository tradeRepository;

    private Trade testTrade;
    private TradeRequest testTradeRequest;

    @BeforeEach
    void setUp() {
        // Clean up test data before each test
        tradeRepository.deleteAll();

        // Create test trade
        testTrade = new Trade(
                "TestAccount",
                "TestType",
                100.0,
                50.0,
                200.0,
                75.0
        );

        testTradeRequest = new TradeRequest(
                "TestAccount",
                "TestType",
                100.0,
                50.0,
                200.0,
                75.0
        );
    }

    // 1. Test home page access
    @Test
    @WithMockUser
    void home_ShouldReturnTradeListPage() throws Exception {
        // Create and save a test trade
        Trade savedTrade = tradeService.save(testTrade);

        mvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"));
    }

    // 2. Test add form display
    @Test
    @WithMockUser
    void addUser_ShouldReturnAddPage() throws Exception {
        mvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    // 3. Test successful validation and save
    @Test
    @WithMockUser
    void validate_Success() throws Exception {
        mvc.perform(post("/trade/validate")
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .param("buyQuantity", "100.0")
                        .param("sellQuantity", "50.0")
                        .param("buyPrice", "200.0")
                        .param("sellPrice", "75.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(flash().attribute("successMessage",
                        "new trade added successfully"));
    }

    // 4. Test validation failure
    @Test
    @WithMockUser
    void validate_WithInvalidData_ShouldReturnToForm() throws Exception {
        mvc.perform(post("/trade/validate")
                        .param("account", "") // Invalid: empty account
                        .param("type", "") // Invalid: empty type
                        .param("buyQuantity", "-100.0") // Invalid: negative quantity
                        .param("sellQuantity", "50.0")
                        .param("buyPrice", "200.0")
                        .param("sellPrice", "75.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeHasErrors("tradeRequest"))
                .andExpect(model().attributeHasFieldErrors("tradeRequest", "account"))
                .andExpect(model().attributeHasFieldErrors("tradeRequest", "type"))
                .andExpect(model().attributeHasFieldErrors("tradeRequest", "buyQuantity"));
    }

    // 5. Test show update form
    @Test
    @WithMockUser
    void showUpdateForm_Success() throws Exception {
        Trade savedTrade = tradeService.save(testTrade);

        mvc.perform(get("/trade/update/{id}", savedTrade.getTradeId()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("tradeRequest"))
                .andExpect(model().attributeExists("tradeId"));
    }

    // 6. Test successful update
    @Test
    @WithMockUser
    void updateTrade_Success() throws Exception {
        Trade savedTrade = tradeService.save(testTrade);

        mvc.perform(post("/trade/update/{id}", savedTrade.getTradeId())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("buyQuantity", "150.0")
                        .param("sellQuantity", "75.0")
                        .param("buyPrice", "250.0")
                        .param("sellPrice", "100.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(flash().attribute("successMessage",
                        "trade with id = " + savedTrade.getTradeId() + " succesfully updated"));
    }

    // 7. Test update with invalid data
    @Test
    @WithMockUser
    void updateTrade_WithInvalidData_ShouldReturnToForm() throws Exception {
        Trade savedTrade = tradeService.save(testTrade);

        mvc.perform(post("/trade/update/{id}", savedTrade.getTradeId())
                        .param("account", "") // Invalid: empty account
                        .param("type", "UpdatedType")
                        .param("buyQuantity", "150.0")
                        .param("sellQuantity", "75.0")
                        .param("buyPrice", "250.0")
                        .param("sellPrice", "100.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeHasErrors("tradeRequest"))
                .andExpect(model().attributeHasFieldErrors("tradeRequest", "account"));
    }

    // 8. Test successful delete
    @Test
    @WithMockUser
    void deleteTrade_Success() throws Exception {
        Trade savedTrade = tradeService.save(testTrade);

        mvc.perform(get("/trade/delete/{id}", savedTrade.getTradeId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(flash().attribute("successMessage",
                        "the trade with id = " + savedTrade.getTradeId() + " has been deleted succesfully"));
    }

    // 9. Test delete with non-existent ID
    @Test
    @WithMockUser
    void deleteTrade_NonExistentId_ShouldHandleError() throws Exception {
        mvc.perform(get("/trade/delete/{id}", 999))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(flash().attribute("errorMessage",
                        "ERROR : the trade with id = 999 has not been deleted"));
    }

    // 10. Test authentication requirement
    @Test
    void accessWithoutAuthentication_ShouldRedirectToLogin() throws Exception {
        mvc.perform(get("/trade/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // 11. Test validation with invalid numbers
    @Test
    @WithMockUser
    void validate_WithInvalidNumbers_ShouldHaveFieldErrors() throws Exception {
        mvc.perform(post("/trade/validate")
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .param("buyQuantity", "-1.0") // Invalid: negative
                        .param("sellQuantity", "-2.0") // Invalid: negative
                        .param("buyPrice", "-3.0") // Invalid: negative
                        .param("sellPrice", "-4.0")) // Invalid: negative
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeHasErrors("tradeRequest"))
                .andExpect(model().attributeHasFieldErrors("tradeRequest", "buyQuantity"))
                .andExpect(model().attributeHasFieldErrors("tradeRequest", "sellQuantity"))
                .andExpect(model().attributeHasFieldErrors("tradeRequest", "buyPrice"))
                .andExpect(model().attributeHasFieldErrors("tradeRequest", "sellPrice"));
    }

    // 12. Test update with non-existent ID
    @Test
    @WithMockUser
    void updateTrade_NonExistentId_ShouldHandleError() throws Exception {
        mvc.perform(post("/trade/update/{id}", 999)
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .param("buyQuantity", "100.0")
                        .param("sellQuantity", "50.0")
                        .param("buyPrice", "200.0")
                        .param("sellPrice", "75.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
