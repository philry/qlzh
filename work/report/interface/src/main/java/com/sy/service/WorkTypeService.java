package com.sy.service;

import com.sy.entity.WorkType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface WorkTypeService {

    List<WorkType> selectWorkTypeList(WorkType workType);

    WorkType selectWorkTypeById(Integer id);

    int addWorkType(WorkType workType);

    int changeWorkType(WorkType workType);

    void deleteWorkTypeById(Integer id);
}
