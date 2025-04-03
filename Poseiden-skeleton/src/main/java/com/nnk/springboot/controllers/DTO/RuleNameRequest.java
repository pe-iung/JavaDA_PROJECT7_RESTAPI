package com.nnk.springboot.controllers.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RuleNameRequest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "name can not be null, empty or blank")
    String name;
    @NotBlank(message = "description can not be null, empty or blank")
    String description;
    @NotBlank(message = "json can not be null, empty or blank")
    String json;
    @NotBlank(message = "template can not be null, empty or blank")
    String template;
    @NotBlank(message = "sqlStr can not be null, empty or blank")
    String sqlStr;
    @NotBlank(message = "sqlPart can not be null, empty or blank")
    String sqlPart;

    public RuleNameRequest(String ruleName, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = ruleName;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlPart = sqlPart;
        this.sqlStr = sqlStr;
    }
}
