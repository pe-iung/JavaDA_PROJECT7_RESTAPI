package com.nnk.springboot.controllers.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {

    @NotBlank(message = "sandPRating can not be null, empty or blank")
    String sandPRating;

    @NotBlank(message = "moodysRating can not be null, empty or blank")
    String moodysRating;

    @NotBlank(message = "fitchRating can not be null, empty or blank")
    String fitchRating;

    @Positive(message = "orderNumber must be a positive value")
    Integer orderNumber;

}
