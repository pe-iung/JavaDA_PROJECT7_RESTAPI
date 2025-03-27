package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

public class CurvePointServiceImpl extends AbstractCrudService<CurvePoint> {
    protected CurvePointServiceImpl(CurvePointRepository repository) {
        super(repository);
    }
}
