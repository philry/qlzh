package com.sy.dao;


import com.sy.entity.Person;
import com.sy.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDao extends JpaRepository<Work,Integer>, JpaSpecificationExecutor {

    Page<Work> getAllByPerson(Person person, Pageable pageable);

}
