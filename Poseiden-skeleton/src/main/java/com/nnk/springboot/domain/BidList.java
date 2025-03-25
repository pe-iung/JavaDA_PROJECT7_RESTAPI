package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidList implements EntityModel<BidList> {
    // TODO: Map columns in data table BIDLIST with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer BidListId;

    @Column
    String account;

    @Column
    String type;

    @Column
    Double bidQuantity;
    @Column
    Double askQuantity;

    //todo : ask franck what this attribute is about
    @Column
    Double bid;

    //todo : ask franck what this attribute is about
    @Column
    Double ask;

    //todo : ask franck what this attribute is about
    @Column
    String benchmark;
    @Column
    Timestamp bidListDate;
    @Column
    String commentary;

    //todo : ask franck what this attribute is about
    @Column
    String security;
    @Column
    String status;
    @Column
    String trader;

    //todo : ask franck what this attribute is about
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

    //todo : ask franck what this attribute is about : seller vs buyer ?
    @Column
    String side;

    public BidList(String account, String type, double bid) {
        this.account = account;
        this.type=type;
        this.bid = bid;
    }

    @Override
    public Integer getId(){
        return BidListId;
    }


    @Override
    public BidList update(BidList update){
        this.account = update.getAccount();


        return this;
    }
}
