package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl extends AbstractCrudService<Trade> {
    protected TradeServiceImpl(TradeRepository repository) {
        super(repository);
    }
}
