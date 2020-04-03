package com.sy.service;

import com.sy.entity.Emp;

import java.util.List;

public interface IEmpService {

    List<Emp> getEmpByRoleId(int roleId) throws Exception;

}
