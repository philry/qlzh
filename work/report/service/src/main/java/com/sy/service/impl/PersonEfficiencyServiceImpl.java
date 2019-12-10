package com.sy.service.impl;

import com.sy.dao.PersonEfficiencyDao;
import com.sy.entity.PersonEfficiency;
import com.sy.service.PersonEfficiencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonEfficiencyServiceImpl implements PersonEfficiencyService {

    @Autowired
    private PersonEfficiencyDao personEfficiencyDao;

    @Override
    @Transactional
    public PersonEfficiency saveReport(PersonEfficiency personEfficiency) {

        return personEfficiencyDao.save(personEfficiency);
    }

    @Override
    public Page<PersonEfficiency> initAllData(Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page,pageSize);

        return personEfficiencyDao.findAll(pageable);
    }
}
