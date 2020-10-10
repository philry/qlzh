package com.sy.dao;

import com.sy.entity.DataType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TableDataMapper {

    List<DataType> getFiled(String tableName);

    List<DataType> getUncheckedOrder(@Param("dataName") String tableName);

    Double getNumberByField(@Param("field") String field,@Param("dataName") String tableName,@Param("code") String code);

    int updateFlag(@Param("dataName") String tableName,@Param("code") String code);
}
