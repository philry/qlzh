package com.sy.dao;

import com.sy.entity.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmpMapper {

    List<Emp> getEmp(@Param("emp") String emp, @Param("roleId") int roleId);

}
