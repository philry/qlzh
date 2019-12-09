package com.sy.dao;

import com.sy.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonDao extends JpaRepository<Person,Integer> {

    Person getById(Integer id);
}
