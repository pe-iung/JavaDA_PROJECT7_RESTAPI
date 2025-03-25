package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.base.AbstractCrudService;

public class TradeServiceImpl extends AbstractCrudService<Trade>{
    protected TradeServiceImpl(TradeRepository repository) {
        super(repository);
    }
}
