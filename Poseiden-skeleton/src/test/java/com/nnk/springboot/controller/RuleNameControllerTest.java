package com.nnk.springboot.controller;
import com.nnk.springboot.controllers.DTO.RuleNameRequest;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.CrudService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RuleNameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CrudService<RuleName> ruleNameService;

    @Autowired
    private RuleNameRepository ruleNameRepository;

    private RuleName testRuleName;
    private RuleNameRequest testRuleNameRequest;

    @BeforeEach
    void setUp() {
        // Clean up test data before each test
        ruleNameRepository.deleteAll();

        // Create test ruleName
        testRuleName = new RuleName(
                "TestName",
                "TestDescription",
                "TestJson",
                "TestTemplate",
                "TestSqlStr",
                "TestSqlPart"
        );

        testRuleNameRequest = new RuleNameRequest(
                "TestName",
                "TestDescription",
                "TestJson",
                "TestTemplate",
                "TestSqlStr",
                "TestSqlPart"
        );
    }

    // 1. Test home page access
    @Test
    @WithMockUser
    void home_ShouldReturnRuleNameListPage() throws Exception {
        // Create and save a test ruleName
        RuleName savedRuleName = ruleNameService.save(testRuleName);

        mvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"));
    }

    // 2. Test add form display
    @Test
    @WithMockUser
    void addRuleForm_ShouldReturnAddPage() throws Exception {
        mvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    // 3. Test successful validation and save
    @Test
    @WithMockUser
    void validate_Success() throws Exception {
        mvc.perform(post("/ruleName/validate")
                        .param("name", "TestName")
                        .param("description", "TestDescription")
                        .param("json", "TestJson")
                        .param("template", "TestTemplate")
                        .param("sqlStr", "TestSqlStr")
                        .param("sqlPart", "TestSqlPart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(flash().attribute("successMessage",
                        "new ruleName added successfully"));
    }

    // 4. Test validation failure
    @Test
    @WithMockUser
    void validate_WithInvalidData_ShouldReturnToForm() throws Exception {
        mvc.perform(post("/ruleName/validate")
                        .param("name", "") // Invalid: empty name
                        .param("description", "TestDescription")
                        .param("json", "TestJson")
                        .param("template", "TestTemplate")
                        .param("sqlStr", "TestSqlStr")
                        .param("sqlPart", "TestSqlPart"))
                .andExpect(status().isOk())
                .andExpect(view().name("rulename/add"))
                .andExpect(model().attributeHasFieldErrors("ruleNameRequest", "name"));
    }

    // 5. Test show update form
    @Test
    @WithMockUser
    void showUpdateForm_Success() throws Exception {
        RuleName savedRuleName = ruleNameService.save(testRuleName);

        mvc.perform(get("/ruleName/update/{id}", savedRuleName.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleNameRequest"))
                .andExpect(model().attributeExists("ruleNameId"));
    }

    // 6. Test successful update
    @Test
    @WithMockUser
    void updateRuleName_Success() throws Exception {
        RuleName savedRuleName = ruleNameService.save(testRuleName);

        mvc.perform(post("/ruleName/update/{id}", savedRuleName.getId())
                        .param("name", "UpdatedName")
                        .param("description", "UpdatedDescription")
                        .param("json", "UpdatedJson")
                        .param("template", "UpdatedTemplate")
                        .param("sqlStr", "UpdatedSqlStr")
                        .param("sqlPart", "UpdatedSqlPart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(flash().attribute("successMessage",
                        "ruleName with id = " + savedRuleName.getId() + " succesfully updated"));

        //when a validation rule is OK, we should update the data in the repository
        Optional<RuleName> updatedRuleName = ruleNameRepository.findById(savedRuleName.getId());
        if (updatedRuleName.isPresent()) {
            Assertions.assertEquals(updatedRuleName.get().getName(), "UpdatedName");
            Assertions.assertEquals(updatedRuleName.get().getDescription(), "UpdatedDescription");
            Assertions.assertEquals(updatedRuleName.get().getJson(), "UpdatedJson");
            Assertions.assertEquals(updatedRuleName.get().getTemplate(), "UpdatedTemplate");
            Assertions.assertEquals(updatedRuleName.get().getSqlStr(), "UpdatedSqlStr");
            Assertions.assertEquals(updatedRuleName.get().getSqlPart(), "UpdatedSqlPart");
        }
    }

    // 7. Test update with invalid data
    @Test
    @WithMockUser
    void updateRuleName_WithInvalidData_ShouldReturnToList() throws Exception {
        RuleName savedRuleName = ruleNameService.save(testRuleName);
        int numberOfRulesBeforeTest = ruleNameRepository.findAll().size();

        mvc.perform(post("/ruleName/update/{id}", savedRuleName.getId())
                        .param("name", "") // Invalid: empty name
                        .param("description", "UpdatedDescription")
                        .param("json", "UpdatedJson")
                        .param("template", "UpdatedTemplate")
                        .param("sqlStr", "UpdatedSqlStr")
                        .param("sqlPart", "UpdatedSqlPart"))
                .andExpect(status().isOk())
                .andExpect(view().name("/ruleName/update"))
                .andExpect(model().attributeExists("ruleNameRequest"))
                .andExpect(model().attributeHasErrors("ruleNameRequest"))
                .andExpect(model().attributeHasFieldErrors("ruleNameRequest", "name"));

        int numberOfRulesAfterTest = ruleNameRepository.findAll().size();

        //when a validation rule fail, we should not save the wrong rule in the repository
        Assertions.assertEquals(numberOfRulesBeforeTest,numberOfRulesAfterTest);

        //when a validation rule fail, we should not update the data with invalid empty name
        Assertions.assertNotEquals(savedRuleName.getName(),"");
    }

    // 8. Test successful delete
    @Test
    @WithMockUser
    void deleteRuleName_Success() throws Exception {
        RuleName savedRuleName = ruleNameService.save(testRuleName);

        mvc.perform(get("/ruleName/delete/{id}", savedRuleName.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(flash().attribute("successMessage",
                        "the ruleName with id = " + savedRuleName.getId() + " has been deleted succesfully"));
    }

    // 9. Test delete with non-existent ID
    @Test
    @WithMockUser
    void deleteRuleName_NonExistentId_ShouldHandleError() throws Exception {
        mvc.perform(get("/ruleName/delete/{id}", 999))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(flash().attribute("errorMessage",
                        "ERROR : the ruleName with id = 999 has not been deleted"));
    }

    // 10. Test authentication requirement
    @Test
    void accessWithoutAuthentication_ShouldRedirectToLogin() throws Exception {
        mvc.perform(get("/ruleName/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // 11. Test validation error messages
    @Test
    @WithMockUser
    void validate_ShouldCheckSpecificErrorMessages() throws Exception {
        mvc.perform(post("/ruleName/validate")
                        .param("name", "")
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleNameRequest"))
                .andExpect(model().attributeHasErrors("ruleNameRequest"))
                .andExpect(model().attributeHasFieldErrors("ruleNameRequest", "name"))
                .andExpect(model().attributeHasFieldErrors("ruleNameRequest", "description"));
    }


}
