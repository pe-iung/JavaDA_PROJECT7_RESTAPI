package com.nnk.springboot.services.impl;


import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.AbstractCrudService;

public class RuleNameServiceImpl extends AbstractCrudService<RuleName> {
    protected RuleNameServiceImpl(RuleNameRepository repository) {
        super(repository);
    }
}
