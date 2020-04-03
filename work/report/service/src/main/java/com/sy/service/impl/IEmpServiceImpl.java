package com.sy.service.impl;

import com.sy.dao.EmpMapper;
import com.sy.dao.TableInfoMapper;
import com.sy.dao.ZRoleMapper;
import com.sy.entity.Emp;
import com.sy.entity.TableInfo;
import com.sy.entity.ZRole;
import com.sy.service.IEmpService;
import com.sy.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IEmpServiceImpl implements IEmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private TableInfoMapper tableInfoMapper;

    @Autowired
    private ZRoleMapper zRoleMapper;

    @Override
    public List<Emp> getEmpByRoleId(int roleId) throws Exception {

        TableInfo tableInfo = tableInfoMapper.getInfo();

        if(tableInfo.getPeopleTableName()==null||"".equals(tableInfo.getPeopleTableName())){
            throw new Exception("请填写用户表表名");
        }

        List<ZRole> roles = zRoleMapper.getRole(tableInfo.getRoleTableName(),roleId);

        List<Emp> list = empMapper.getEmp(tableInfo.getPeopleTableName(),roleId);

        getAllRoles(roles,list,tableInfo);

        if (list.isEmpty()){
            throw new Exception("当前岗位下没有职工，请选择其他岗位或者添加员工");
        }

        return list;
    }

    private void getAllRoles(List<ZRole> roles,List<Emp> list,TableInfo tableInfo){

        for (ZRole role : roles) {
            List<Emp> empList =  empMapper.getEmp(tableInfo.getPeopleTableName(),role.getId());
            if (!empList.isEmpty()){
                list.addAll(empList);
            }
            List<ZRole> sonRoles = zRoleMapper.getRole(tableInfo.getRoleTableName(),role.getId());
            if(!sonRoles.isEmpty()){
                getAllRoles(sonRoles,list,tableInfo);
            }
        }

    }
}
