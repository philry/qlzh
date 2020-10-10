package com.sy.dao;

import com.sy.entity.ProcessType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TableTypeMapper {

    List<ProcessType> getType(@Param("typeName") String typeName, @Param("tableName") String tableName);

}
