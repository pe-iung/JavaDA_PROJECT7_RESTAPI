package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rulename")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleName  implements EntityModel<RuleName> {
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

    public RuleName(String ruleName, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = ruleName;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlPart = sqlPart;
        this.sqlStr = sqlStr;
    }

    /**
     * @param update
     * @return
     */
    @Override
    public RuleName update(RuleName update) {
        this.name = update.getName();
        this.description = update.getDescription();
        this.json = update.getJson();
        this.template = update.getTemplate();
        this.sqlStr = update.getSqlStr();
        this.sqlPart = update.getSqlPart();
        return this;
    }
}
