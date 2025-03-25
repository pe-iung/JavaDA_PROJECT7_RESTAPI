package com.nnk.springboot.services.base;

public interface CrudService<M> {

    M save(M entity);

    M getById(Integer id);

    void update(M entity);

    void delete(Integer id);

}
