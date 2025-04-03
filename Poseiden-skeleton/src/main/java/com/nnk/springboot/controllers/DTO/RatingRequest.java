package com.nnk.springboot.controllers.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingRequest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "sandPRating can not be null, empty or blank")
    String sandPRating;

    @NotBlank(message = "moodysRating can not be null, empty or blank")
    String moodysRating;

    @NotBlank(message = "fitchRating can not be null, empty or blank")
    String fitchRating;

    @Positive(message = "orderNumber must be a positive value")
    Integer orderNumber;




    public RatingRequest(String moodysRating, String sandPRating, String fitchRating, int orderNumber) {
        this.moodysRating= moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
