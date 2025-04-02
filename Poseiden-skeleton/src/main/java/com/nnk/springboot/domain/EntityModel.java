package com.nnk.springboot.domain;


//public interface EntityModel<M extends EntityModel<M>> {
public interface EntityModel<M> {
    Integer getId();

    M update(M update);

    //void delete(int id);
}
