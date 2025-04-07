package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Table(name = "trade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade  implements EntityModel<Trade> {
    // TODO: Map columns in data table TRADE with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer tradeId;
    @Column
    String account;
    @Column
    String type;
    @Column
    Double buyQuantity;
    @Column
    Double sellQuantity;
    @Column
    Double buyPrice;
    @Column
    Double sellPrice;
    @Column
    String benchmark;
    @Column
    Timestamp tradeDate;
    @Column
    String security;
    @Column
    String status;
    @Column
    String trader;
    @Column
    String book;
    @Column
    String creationName;
    @Column
    Timestamp creationDate;
    @Column
    String revisionName;
    @Column
    Timestamp revisionDate;
    @Column
    String dealName;
    @Column
    String dealType;
    @Column
    String sourceListId;
    @Column
    String side;

    public Trade(String tradeAccount, String type) {
        this.account=tradeAccount;
        this.type = type;
    }

    public Trade(String account,
                 String type,
                 Double buyQuantity,
                 Double buyPrice,
                 Double sellPrice,
                 Double sellQuantity) {
        this.account = account;
        this.type = type;
        this.buyQuantity = buyQuantity;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.sellQuantity = sellQuantity;


    }

    @Override
    public Integer getId() {
        return tradeId;
    }

    /**
     * @param update
     * @return
     */
    @Override
    public Trade update(Trade update) {
        this.account = update.getAccount();
        this.type = update.getType();
        this.buyQuantity = update.getBuyQuantity();
        this.sellQuantity = update.getSellQuantity();
        this.buyPrice = update.getBuyPrice();
        this.sellPrice = update.getSellPrice();

        return this;
    }
}
