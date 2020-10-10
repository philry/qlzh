package com.sy.service.impl;

import com.sy.dao.RoleMapper;
import com.sy.dao.TableInfoMapper;
import com.sy.dao.ZRoleMapper;
import com.sy.entity.Role;
import com.sy.entity.TableInfo;
import com.sy.entity.ZRole;
import com.sy.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IRoleServiceImpl implements IRoleService {

    @Autowired
    private ZRoleMapper roleMapper;

    @Autowired
    private TableInfoMapper tableInfoMapper;

    @Override
    public List<ZRole> getAllRole(int pid) throws Exception {

        TableInfo tableInfo = tableInfoMapper.getInfo();

        if(tableInfo.getRoleTableName()==null||"".equals(tableInfo.getRoleTableName())){
            throw new Exception("请填写岗位表表名");
        }

        String roleName = tableInfo.getRoleTableName();

        List<ZRole> roles = roleMapper.getRole(roleName,0);

        List<ZRole> zRoles = new ArrayList<>();

        getAllRole(roleName, roles,zRoles);

        roles.addAll(zRoles);

        return roles;
    }

    private void getAllRole(String roleName, List<ZRole> roles, List<ZRole> zRoles) {
        for (ZRole role : roles) {
            List<ZRole> sonRoles = roleMapper.getRole(roleName,role.getId());
            if (!sonRoles.isEmpty()){
                for (ZRole sonRole : sonRoles) {
                    sonRole.setName(role.getName()+"-"+sonRole.getName());
                    zRoles.add(sonRole);
                }
                getAllRole(roleName,sonRoles,zRoles);
            }
        }
    }
}
