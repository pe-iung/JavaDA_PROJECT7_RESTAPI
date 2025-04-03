package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating  implements EntityModel<Rating> {
    // TODO: Map columns in data table RATING with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column
    String moodysRating;
    @Column
    String sandPRating;
    @Column
    String fitchRating;
    @Column
    Integer orderNumber;

    public Rating(String moodysRating, String sandPRating, String fitchRating, int orderNumber) {
        this.moodysRating= moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }

    /**
     * @param update
     * @return
     */
    @Override
    public Rating update(Rating update) {
        this.moodysRating = update.getMoodysRating();
        this.sandPRating = update.getSandPRating();
        this.fitchRating = update.getFitchRating();
        this.orderNumber = update.getOrderNumber();
        return this;
    }
}
