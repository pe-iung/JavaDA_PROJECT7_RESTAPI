package com.nnk.springboot.controller;

import com.nnk.springboot.configuration.CustomUserDetailsService;
import com.nnk.springboot.controllers.DTO.UserEditRequest;
import com.nnk.springboot.controllers.DTO.UserRoleEditRequest;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.CrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrudService<User> userService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;
    private User testAdminUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setFullname("Test User");
        testUser.setPassword("password");
        testUser.setRole("USER");

        testAdminUser = new User();
        testAdminUser.setId(2);
        testAdminUser.setUsername("adminUser");
        testAdminUser.setFullname("Admin User");
        testAdminUser.setPassword("password");
        testAdminUser.setRole("ADMIN");
    }

    @Test
    void publicPages_ShouldBeAccessibleWithoutAuth() throws Exception {

        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("userEditRequest"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminUsers_WhenAdmin_ShouldShowUsersList() throws Exception {
        List<User> users = Arrays.asList(testUser, testAdminUser);
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attribute("users", users));
    }

    @Test
    @WithMockUser(roles = "USER")
    void adminUsers_WhenUser_ShouldBeDenied() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/403"));
    }

    @Test
    void validate_NewUser_ShouldRedirectToLogin() throws Exception {
        UserEditRequest request = new UserEditRequest("newUser", "password", "New User");

        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .flashAttr("userEditRequest", request))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).save(any(User.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserRole_Success() throws Exception {
        when(userService.getById(1)).thenReturn(testUser);

        UserRoleEditRequest request = new UserRoleEditRequest(
                "updatedUser",
                "Updated User",
                "ROLE_ADMIN"
        );

        mockMvc.perform(post("/admin/user/update/1")
                        .with(csrf())
                        .flashAttr("userRoleEditRequest", request))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(userService).update(any(User.class));
    }

    @Test
    @WithMockUser(username = "testUser")
    void updateMyself_Success() throws Exception {
        when(userService.getById(1)).thenReturn(testUser);
        when(customUserDetailsService.loadUserByUsername("testUser")).thenReturn(testUser);

        UserEditRequest request = new UserEditRequest(
                "testUser",
                "newPassword",
                "Updated Test User"
        );

        mockMvc.perform(post("/user/myself/validate")
                        .with(csrf())
                        .with(user(testUser))
                        .flashAttr("userEditRequest", request))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(userService).update(any(User.class));
    }

    @Test
    void validate_WithInvalidData_ShouldShowErrors() throws Exception {
        UserEditRequest request = new UserEditRequest("", "", "");

        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .flashAttr("userEditRequest", request))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserRole_WithInvalidData_ShouldShowErrors() throws Exception {
        when(userService.getById(1)).thenReturn(testUser);

        UserRoleEditRequest request = new UserRoleEditRequest("", "", "");

        mockMvc.perform(post("/admin/user/update/1")
                        .with(csrf())
                        .flashAttr("userRoleEditRequest", request))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/updateUserRole"))
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserRole_WithInvalidData_ShouldShowErrors2() throws Exception {
        // Given a test user
        when(userService.getById(1)).thenReturn(testUser);

        // and given an invalid request
        UserRoleEditRequest request = new UserRoleEditRequest();
        request.setUsername("");  // Invalid: blank
        request.setFullname("");  // Invalid: blank
        request.setRole("");      // Invalid: blank

        // when we Perform request with proper model binding
        mockMvc.perform(post("/admin/user/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "")
                        .param("fullname", "")
                        .param("role", ""))
        // then we expect validation errors
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/updateUserRole"))
                .andExpect(model().attributeHasFieldErrors("userRoleEditRequest", "username"))
                .andExpect(model().attributeHasFieldErrors("userRoleEditRequest", "fullname"))
                .andExpect(model().attributeHasFieldErrors("userRoleEditRequest", "role"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserRole_WithException_ShouldShowError() throws Exception {
        when(userService.getById(1)).thenReturn(testUser);
        doThrow(new RuntimeException("Update failed")).when(userService).update(any(User.class));

        UserRoleEditRequest request = new UserRoleEditRequest(
                "updatedUser",
                "Updated User",
                "ROLE_ADMIN"
        );

        mockMvc.perform(post("/admin/user/update/1")
                        .with(csrf())
                        .flashAttr("userRoleEditRequest", request))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
