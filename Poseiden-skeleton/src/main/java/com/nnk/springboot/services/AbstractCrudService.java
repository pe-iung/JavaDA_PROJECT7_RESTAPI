package com.nnk.springboot.services;


import com.nnk.springboot.domain.EntityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;

public abstract class AbstractCrudService<M extends EntityModel<M>> implements CrudService<M> {

    private  final JpaRepository<M, Integer> repository;
    
    protected AbstractCrudService(JpaRepository<M, Integer> repository){
        this.repository = repository;
    }
    
    @Override
    public M save(M entity) {
        Assert.isNull(entity.getId(), "Id need to be null");
        return this.repository.save(entity);
    }

    @Override
    public M getById(Integer id) {
        return this.repository.findById(id).orElseThrow();
    }

    @Override
    public void update(M entity) {
        M updatedEntity = getById(entity.getId())
                .update(entity);

        this.repository.save(updatedEntity);

    }

    @Override
    public void delete(Integer id) {
        this.repository.deleteById(id);
    }
}
