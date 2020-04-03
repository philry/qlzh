package com.sy.service;

import com.sy.entity.Role;
import com.sy.entity.ZRole;

import java.util.List;

public interface IRoleService {

    List<ZRole> getAllRole(int pid) throws Exception;

}
