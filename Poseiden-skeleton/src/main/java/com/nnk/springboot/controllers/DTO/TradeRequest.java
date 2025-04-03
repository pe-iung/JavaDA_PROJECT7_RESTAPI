package com.nnk.springboot.controllers.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequest {

    @NotBlank(message = "account can not be null, empty or blank")
    String account;

    @NotBlank(message = "type can not be null, empty or blank")
    String type;

    @Positive(message = "buyQuantity must be a positive value")
    Double buyQuantity;

    @Positive(message = "sellQuantity must be a positive value")
    Double sellQuantity;

    @Positive(message = "buyPrice must be a positive value")
    Double buyPrice;

    @Positive(message = "sellPrice must be a positive value")
    Double sellPrice;
}
