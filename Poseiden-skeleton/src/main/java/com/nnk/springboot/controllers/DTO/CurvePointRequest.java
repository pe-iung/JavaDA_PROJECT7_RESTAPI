package com.nnk.springboot.controllers.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurvePointRequest {
    @NotNull(message = "curveId cannot be null")
    @Positive(message = "curveID must be a positive integer")
    private Integer curveId;

    @NotNull(message = "term cannot be null")
    @Positive(message = "term must be a positive value")
    private Double term;

    @NotNull(message = "value cannot be null")
    @Positive(message = "value must be a positive value")
    private Double value;
}
