package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;

public class CurvePointServiceImpl extends AbstractCrudService<CurvePoint>{
    protected CurvePointServiceImpl(JpaRepository<CurvePoint, Integer> repository) {
        super(repository);
    }
}
