package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.base.AbstractCrudService;

public class RatingServiceImpl extends AbstractCrudService<Rating> {

    protected RatingServiceImpl(RatingRepository repository) {
        super(repository);
    }
}
