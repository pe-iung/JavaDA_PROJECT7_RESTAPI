package com.nnk.springboot.controllers.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidListRequest {

    @NotBlank(message = "account can not be null, empty or blank")
    String account;

    @NotBlank(message = "type can not be null, empty or blank")
    String type;

    @Positive(message = "bidQuantity must be a positive value")
    double bidQuantity;

}
