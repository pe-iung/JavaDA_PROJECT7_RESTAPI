package com.nnk.springboot.controllers.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BidListRequest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "account can not be null, empty or blank")
    String account;

    @NotBlank(message = "type can not be null, empty or blank")
    String type;

    @Positive(message = "bidQuantity must be a positive value")
    double bidQuantity;

    public BidListRequest(String account, String type, double bidQuantity) {
        this.account = account;
        this.type=type;
        this.bidQuantity = bidQuantity;
    }
}
