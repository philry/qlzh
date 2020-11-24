package com.sy.dao;

import com.sy.entity.WorkType;

import java.util.List;

public interface WorkTypeMapper {
    List<WorkType> selectWorkTypeList(WorkType workType);

    WorkType selectWorkTypeById(Integer id);

    int insertWorkType(WorkType workType);

    void updateWorkType(WorkType workType);

    void deleteWorkTypeById(Integer id);
}
