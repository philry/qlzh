package com.sy.service.impl;

import com.sy.dao.TableDataMapper;
import com.sy.dao.TableInfoMapper;
import com.sy.entity.DataType;
import com.sy.entity.TableInfo;
import com.sy.service.IDataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IDataTypeServiceImpl implements IDataTypeService {


    @Autowired
    private TableInfoMapper tableInfoMapper;

    @Autowired
    private TableDataMapper tableDataMapper;

    @Override
    public List<String> getField() throws Exception {

        TableInfo tableInfo = tableInfoMapper.getInfo();

        if(tableInfo.getDataTableName()==null||"".equals(tableInfo.getDataTableName())){
            throw new Exception("请填写数据表表名");
        }

        List<DataType> list = tableDataMapper.getFiled(tableInfo.getDataTableName());

        if(list.isEmpty()){
            throw new Exception("该表无数据，请检查后再尝试");
        }

        List<String> columns = new ArrayList<>();

        List<String> types = new ArrayList<>();

        types.add("DECIMAL");
        types.add("NUMERIC");
        types.add("FLOAT");
        types.add("TINYINT");
        types.add("SMALLINT");
        types.add("MEDIUMINT");
        types.add("INT");
        types.add("BIGINT");
        types.add("DOUBLE");

        for (DataType dataType : list) {
            if(!"id".toUpperCase().equals(dataType.getName().toUpperCase())){
                if (types.contains(dataType.getType().toUpperCase())&&!columns.contains(dataType.getName())){
                    columns.add(dataType.getName());
                }
            }
        }

        if (columns.isEmpty()){
            throw new Exception("该表无可供判定的数学类型，请添加或者更换数据表");
        }

        return columns;
    }
}
