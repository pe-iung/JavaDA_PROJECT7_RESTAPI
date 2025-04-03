package com.nnk.springboot.controllers.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleNameRequest {

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

}
