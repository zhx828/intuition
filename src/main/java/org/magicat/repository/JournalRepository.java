package org.magicat.repository;

import io.swagger.annotations.Api;
import org.magicat.model.Journal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

@Api(tags = "Journal Entities - data on journals")
@RepositoryRestResource(collectionResourceRel = "journals", path = "journals")
public interface JournalRepository extends MongoRepository<Journal, String> {

    
    @Override
    <S extends Journal> List<S> saveAll(Iterable<S> entities);

    
    @Override
    <S extends Journal> S insert(S entity);

    
    @Override
    <S extends Journal> List<S> insert(Iterable<S> entities);

    
    @Override
    <S extends Journal> S save(S entity);

    
    @Override
    void deleteById(String id);

    
    @Override
    void delete(Journal journal);

    
    @Override
    void deleteAll(Iterable<? extends Journal> journals);

    
    @Override
    void deleteAll();


}
