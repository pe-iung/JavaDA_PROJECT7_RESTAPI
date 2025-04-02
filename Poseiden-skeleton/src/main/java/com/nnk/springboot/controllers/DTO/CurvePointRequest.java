package com.nnk.springboot.controllers.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CurvePointRequest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Positive(message = "curveID must be a positive integer")
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
