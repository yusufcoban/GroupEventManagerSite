package com.example;

import org.springframework.data.repository.CrudRepository;


public interface userDao extends CrudRepository<user, Integer> {
}