package com.sy.dao;

import com.sy.entity.Role;
import com.sy.entity.ZRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZRoleMapper {
    
    List<ZRole> getRole(@Param("dept")String dept, @Param("pid")int pid);

}
