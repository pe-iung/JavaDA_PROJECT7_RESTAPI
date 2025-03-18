package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;

@Entity
@Table(name = "rulename")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleName {
    // TODO: Map columns in data table RULENAME with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column
    String name;
    @Column
    String description;
    @Column
    String json;
    @Column
    String template;
    @Column
    String sqlStr;
    @Column
    String sqlPart;
}
