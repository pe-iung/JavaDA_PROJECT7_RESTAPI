package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurvePoint implements EntityModel<CurvePoint> {
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

    public CurvePoint(int curveId, double term, double value) {
        this.curveId=curveId;
        this.term=term;
        this.value=value;
    }

    /**
     * @param update
     * @return
     */
    @Override
    public CurvePoint update(CurvePoint update) {
        this.curveId = update.getCurveId();
        return this;
    }
}
