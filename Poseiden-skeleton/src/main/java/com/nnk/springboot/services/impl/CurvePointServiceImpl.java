package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class CurvePointServiceImpl extends AbstractCrudService<CurvePoint> {
    protected CurvePointServiceImpl(CurvePointRepository repository) {
        super(repository);
    }
}
