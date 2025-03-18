package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
@AllArgsConstructor
@NoArgsConstructor
public class CurvePoint {
    // TODO: Map columns in data table CURVEPOINT with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column
    Integer curveId;
    @Column
    Timestamp asOfDate;
    @Column
    Double term;
    @Column
    Double value;
    @Column
    Timestamp creationDate;
}
