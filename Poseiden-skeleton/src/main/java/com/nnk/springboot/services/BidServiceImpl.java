package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.base.AbstractCrudService;
import org.springframework.stereotype.Service;


@Service
public class BidServiceImpl extends AbstractCrudService<BidList> {

    public BidServiceImpl(BidListRepository repository){
        super(repository);
    }
    
//    public List<BidList> findAll() {
//        BidListRepository repository;
//        return repository.findAll();
//    }

}
