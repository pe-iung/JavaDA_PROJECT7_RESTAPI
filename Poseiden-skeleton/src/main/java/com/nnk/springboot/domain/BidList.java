package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidList {
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
    @Column
    Double bid;
    @Column
    Double ask;
    @Column
    String benchmark;
    @Column
    Timestamp bidListDate;
    @Column
    String commentary;
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
}
