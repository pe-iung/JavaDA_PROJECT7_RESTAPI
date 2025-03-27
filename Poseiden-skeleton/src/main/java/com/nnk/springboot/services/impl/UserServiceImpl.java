package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.*;
import com.nnk.springboot.repositories.*;
import com.nnk.springboot.services.AbstractCrudService;

public class UserServiceImpl extends AbstractCrudService<User> {
    protected UserServiceImpl(UserRepository repository) {
        super(repository);
    }
}
