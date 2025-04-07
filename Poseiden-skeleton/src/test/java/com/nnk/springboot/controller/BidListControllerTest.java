package com.nnk.springboot.controller;

import com.nnk.springboot.domain.BidList;
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
class BidListControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CrudService<BidList> bidService;

    @BeforeEach
    void setUp() {
        // Clean up test data before each test
        //bidService.deleteAll();
    }

    @Test
    @WithMockUser
    void home_ShouldReturnBidListPage() throws Exception {
        mvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"));
    }

    @Test
    @WithMockUser
    void addBidForm_ShouldReturnAddPage() throws Exception {
        mvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser
    void validate_Success() throws Exception {
        mvc.perform(post("/bidList/validate")
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .param("bidQuantity", "100.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    void validate_WithInvalidData_ShouldReturnToForm() throws Exception {
        mvc.perform(post("/bidList/validate")
                        .param("account", "") // Invalid: empty account
                        .param("type", "TestType")
                        .param("bidQuantity", "100.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser
    void showUpdateForm_Success() throws Exception {
        // Create a bid first
        BidList bid = new BidList("TestAccount", "TestType", 100.0);
        BidList savedBid = bidService.save(bid);

        mvc.perform(get("/bidList/update/{id}", savedBid.getBidListId()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidListRequest"))
                .andExpect(model().attributeExists("bidListId"));
    }

    @Test
    @WithMockUser
    void updateBid_Success() throws Exception {
        // Create a bid first
        BidList bid = new BidList("TestAccount", "TestType", 100.0);
        BidList savedBid = bidService.save(bid);

        mvc.perform(post("/bidList/update/{id}", savedBid.getBidListId())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("bidQuantity", "200.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    void updateBid_WithInvalidData_ShouldReturnToForm() throws Exception {
        // Create a bid first
        BidList bid = new BidList("TestAccount", "TestType", 100.0);
        BidList savedBid = bidService.save(bid);

        mvc.perform(post("/bidList/update/{id}", savedBid.getBidListId())
                        .param("account", "") // Invalid: empty account
                        .param("type", "UpdatedType")
                        .param("bidQuantity", "200.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser
    void deleteBid_Success() throws Exception {
        // Create a bid first
        BidList bid = new BidList("TestAccount", "TestType", 100.0);
        BidList savedBid = bidService.save(bid);

        mvc.perform(get("/bidList/delete/{id}", savedBid.getBidListId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    void deleteBid_NonExistentId_ShouldHandleError() throws Exception {
        mvc.perform(get("/bidList/delete/{id}", 999))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    void accessWithoutAuthentication_ShouldRedirectToLogin() throws Exception {
        mvc.perform(get("/bidList/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
