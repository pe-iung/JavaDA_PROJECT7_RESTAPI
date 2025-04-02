package com.nnk.springboot.controllers.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CurvePointRequest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull(message = "account can not be null, empty or blank")
    int curveId;

    @Positive(message = "term must be a positive value")
    double term;

    @Positive(message = "value must be a positive value")
    double value;

    public CurvePointRequest(int curveId, double term, double value) {
        this.curveId = curveId;
        this.term=term;
        this.value = value;
    }
}
