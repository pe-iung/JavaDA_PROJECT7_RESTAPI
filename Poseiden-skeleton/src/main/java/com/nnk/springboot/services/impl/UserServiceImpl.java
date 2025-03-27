package com.nnk.springboot.services;

import com.nnk.springboot.domain.*;
import com.nnk.springboot.repositories.*;

public class UserServiceImpl extends AbstractCrudService<User> {
    protected UserServiceImpl(UserRepository repository) {
        super(repository);
    }
}
