package com.sy.service;


import com.sy.entity.PersonEfficiency;
import org.springframework.data.domain.Page;

public interface PersonEfficiencyService {

    PersonEfficiency saveReport(PersonEfficiency personEfficiency);

    Page<PersonEfficiency> initAllData(Integer page, Integer pageSize);

}
