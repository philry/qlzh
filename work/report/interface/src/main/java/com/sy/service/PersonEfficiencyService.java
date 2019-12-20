package com.sy.service;


import com.sy.entity.PersonEfficiency;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface PersonEfficiencyService {

    PersonEfficiency saveReport(PersonEfficiency personEfficiency);

    Page<PersonEfficiency> initAllData(Integer page, Integer pageSize);

    void   calculateData(String personName,int deptId, Date beginTime,Date endTime) throws Exception;

}
