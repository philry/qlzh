package com.sy.dao;

import com.sy.entity.DataManage;
import com.sy.entity.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataManageMapper {

	int insertDataManage(DataManage data);

}
