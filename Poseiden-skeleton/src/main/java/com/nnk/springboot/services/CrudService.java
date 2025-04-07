package com.nnk.springboot.services;

import java.util.List;

public interface CrudService<M> {

    M save(M entity);

    M getById(Integer id);

    void update(M entity);

    void delete(Integer id) throws Exception;

    List<M> findAll();
}
