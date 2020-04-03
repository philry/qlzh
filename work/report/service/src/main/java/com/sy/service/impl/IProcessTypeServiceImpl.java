package com.sy.service.impl;

import com.sy.dao.TableInfoMapper;
import com.sy.dao.TableTypeMapper;
import com.sy.entity.ProcessType;
import com.sy.entity.TableInfo;
import com.sy.service.IProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IProcessTypeServiceImpl implements IProcessTypeService {

    @Autowired
    private TableInfoMapper tableInfoMapper;

    @Autowired
    private TableTypeMapper tableTypeMapper;


    @Override
    public List<ProcessType> getType() throws Exception {

        TableInfo tableInfo = tableInfoMapper.getInfo();

        if(tableInfo.getTypeTableName()==null||"".equals(tableInfo.getTypeTableName())){
            throw new Exception("请填写类型表表名");
        }

        if(tableInfo.getTypeTableFiled()==null||"".equals(tableInfo.getTypeTableFiled())){
            throw new Exception("请填写类型表字段名");
        }

        List<ProcessType> list = tableTypeMapper.getType(tableInfo.getTypeTableFiled(),tableInfo.getTypeTableName());

        return list;
    }
}
