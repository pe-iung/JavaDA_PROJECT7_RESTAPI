package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.impl.RuleNameServiceImpl;
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
class RuleNameServiceImplTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameServiceImpl ruleNameService;

    private RuleName testRuleName;

    @BeforeEach
    void setUp() {
        // Create test data with all required fields
        testRuleName = new RuleName(
                "TestName",
                "TestDescription",
                "TestJson",
                "TestTemplate",
                "TestSqlStr",
                "TestSqlPart"
        );
        testRuleName.setId(1);
    }

    @Test
    void save_ShouldSaveNewRuleName() {
        // Arrange
        RuleName ruleNameToSave = new RuleName(
                "TestName",
                "TestDescription",
                "TestJson",
                "TestTemplate",
                "TestSqlStr",
                "TestSqlPart"
        );
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(testRuleName);

        // Act
        RuleName savedRuleName = ruleNameService.save(ruleNameToSave);

        // Assert
        assertNotNull(savedRuleName);
        assertEquals(testRuleName.getId(), savedRuleName.getId());
        assertEquals(testRuleName.getName(), savedRuleName.getName());
        assertEquals(testRuleName.getDescription(), savedRuleName.getDescription());
        assertEquals(testRuleName.getJson(), savedRuleName.getJson());
        assertEquals(testRuleName.getTemplate(), savedRuleName.getTemplate());
        assertEquals(testRuleName.getSqlStr(), savedRuleName.getSqlStr());
        assertEquals(testRuleName.getSqlPart(), savedRuleName.getSqlPart());
        verify(ruleNameRepository).save(any(RuleName.class));
    }

    @Test
    void save_WithNonNullId_ShouldThrowException() {
        // Arrange - RuleName already has ID from setUp()

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ruleNameService.save(testRuleName));
        verify(ruleNameRepository, never()).save(any(RuleName.class));
    }

    @Test
    void getById_ShouldReturnRuleName() {
        // Arrange
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(testRuleName));

        // Act
        RuleName foundRuleName = ruleNameService.getById(1);

        // Assert
        assertNotNull(foundRuleName);
        assertEquals(testRuleName.getId(), foundRuleName.getId());
        assertEquals(testRuleName.getName(), foundRuleName.getName());
        verify(ruleNameRepository).findById(1);
    }

    @Test
    void getById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(ruleNameRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> ruleNameService.getById(999));
        verify(ruleNameRepository).findById(999);
    }

    @Test
    void update_ShouldUpdateExistingRuleName() {
        // Arrange
        RuleName updatedRuleName = new RuleName(
                "UpdatedName",
                "UpdatedDescription",
                "UpdatedJson",
                "UpdatedTemplate",
                "UpdatedSqlStr",
                "UpdatedSqlPart"
        );
        updatedRuleName.setId(1);

        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(testRuleName));
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(updatedRuleName);

        // Act
        ruleNameService.update(updatedRuleName);

        // Assert
        verify(ruleNameRepository).findById(1);
        verify(ruleNameRepository).save(any(RuleName.class));
    }

    @Test
    void update_WithNonExistentId_ShouldThrowException() {
        // Arrange
        RuleName nonExistentRule = new RuleName(
                "TestName",
                "TestDescription",
                "TestJson",
                "TestTemplate",
                "TestSqlStr",
                "TestSqlPart"
        );
        nonExistentRule.setId(999);
        when(ruleNameRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> ruleNameService.update(nonExistentRule));
        verify(ruleNameRepository).findById(999);
        verify(ruleNameRepository, never()).save(any(RuleName.class));
    }

    @Test
    void delete_ShouldDeleteRuleName() {
        // Arrange
        doNothing().when(ruleNameRepository).deleteById(1);

        // Act
        ruleNameService.delete(1);

        // Assert
        verify(ruleNameRepository).deleteById(1);
    }

    @Test
    void findAll_ShouldReturnAllRuleNames() {
        // Arrange
        List<RuleName> expectedRules = Arrays.asList(
                testRuleName,
                new RuleName("Name2", "Desc2", "Json2", "Template2", "SqlStr2", "SqlPart2")
        );
        when(ruleNameRepository.findAll()).thenReturn(expectedRules);

        // Act
        List<RuleName> actualRules = ruleNameService.findAll();

        // Assert
        assertNotNull(actualRules);
        assertEquals(expectedRules.size(), actualRules.size());
        assertEquals(expectedRules.get(0).getName(), actualRules.get(0).getName());
        verify(ruleNameRepository).findAll();
    }

    @Test
    void update_ShouldUpdateAllFields() {
        // Arrange
        RuleName existingRule = testRuleName;
        RuleName updatedRule = new RuleName(
                "UpdatedName",
                "UpdatedDescription",
                "UpdatedJson",
                "UpdatedTemplate",
                "UpdatedSqlStr",
                "UpdatedSqlPart"
        );
        updatedRule.setId(1);

        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(existingRule));
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(updatedRule);

        // Act
        ruleNameService.update(updatedRule);

        // Assert
        verify(ruleNameRepository).save(any(RuleName.class));
        verify(ruleNameRepository).findById(1);
    }
}
