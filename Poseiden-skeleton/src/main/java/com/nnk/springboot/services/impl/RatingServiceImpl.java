package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.AbstractCrudService;

public class RatingServiceImpl extends AbstractCrudService<Rating> {

    protected RatingServiceImpl(RatingRepository repository) {
        super(repository);
    }
}
