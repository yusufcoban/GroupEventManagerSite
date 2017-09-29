package com.example;

import org.springframework.data.repository.CrudRepository;

//@RepositoryRestResource(collectionResourceRel = "sportarten", path = "sportarten")
public interface sportartenDao extends CrudRepository<sportarten, String> {

}