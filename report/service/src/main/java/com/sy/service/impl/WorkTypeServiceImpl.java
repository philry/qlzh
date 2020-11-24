package com.sy.service.impl;

import com.sy.dao.WorkTypeMapper;
import com.sy.entity.WorkType;
import com.sy.service.WorkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkTypeServiceImpl implements WorkTypeService {

    @Autowired
    WorkTypeMapper workTypeMapper;

    @Override
    public List<WorkType> selectWorkTypeList(WorkType workType) {
        return workTypeMapper.selectWorkTypeList(workType);
    }

    @Override
    public WorkType selectWorkTypeById(Integer id) {
        return workTypeMapper.selectWorkTypeById(id);
    }

    @Override
    public int addWorkType(WorkType workType) {
        return workTypeMapper.insertWorkType(workType);
    }

    @Override
    public int changeWorkType(WorkType workType) {
        workTypeMapper.updateWorkType(workType);
        return 1;
    }

    @Override
    public void deleteWorkTypeById(Integer id) {
        workTypeMapper.deleteWorkTypeById(id);
    }
}
