package com.sy.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sy.dao.DeptMapper;
import com.sy.dao.PersonMapper;
import com.sy.entity.Dept;
import com.sy.entity.Person;
import com.sy.service.DeptService;

@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private PersonMapper personMapper;
	
	@Override
	public List<Dept> getDeptList(Dept dept) {
		List<Dept> list = deptMapper.selectDeptList(dept);
		addSonDept(list);
		setDept(list);
		return list;
	}

	@Override
	@Transactional
	public int insertDept(Dept dept) {
		Dept dept2 = new Dept();
		dept2.setName(dept.getName());
		List<Dept> list = deptMapper.selectDeptList(dept2);
		if(list!=null&&list.size()>0) {
			throw new RuntimeException("部门名称已存在");
		}
		dept.setCreateTime(new Timestamp(new Date().getTime()));
		dept.setUpdateTime(dept.getCreateTime());
		if(dept.getPid()==0||dept.getPid()==null) {
			dept.setLevel(1);
		}else {
			Dept fDept = deptMapper.selectDeptById(dept.getPid());
			dept.setLevel(fDept.getLevel()+1);
		}
		return deptMapper.insertDept(dept);
	}

	@Override
	@Transactional
	public int updateDept(Dept dept) {
		Dept dept2 = new Dept();
		dept2.setName(dept.getName());
		List<Dept> list = deptMapper.selectDeptList(dept2);
		boolean flag = true;
		if(list!=null&&list.size()>0) {
			for (Dept dept3 : list) {
				if(dept3.getId()!=dept.getId()) {
					flag = false;
				}
			}
		}
		if(!flag) {
			throw new RuntimeException("部门名称已存在");
		}
		dept.setUpdateTime(new Timestamp(new Date().getTime()));
		if(dept.getPid()==0||dept.getPid()==null) {
			dept.setLevel(1);
		}else {
			Dept fDept = deptMapper.selectDeptById(dept.getPid());
			dept.setLevel(fDept.getLevel()+1);
		}
		return deptMapper.updateDept(dept);
	}

	@Override
	@Transactional
	public int deleteDeptById(Integer id) {
		List<Dept> list = deptMapper.selectDeptListByPid(id);
		if(list==null || list.size()==0) {
			return deptMapper.deleteDeptById(id);
		}else {
			return 0;
		}
	}

	@Override
	public Dept selectDeptById(Integer id) {
		Dept dept = deptMapper.selectDeptById(id);
		setDept(dept);
		return dept;
	}

	private void setDept(List<Dept> list) {
		for (Dept dept : list) {
			setDept(dept);
		}
	}

	private void setDept(Dept dept) {
		Person person = personMapper.selectPersonById(dept.getLeader());
		Person operatorPerson = personMapper.selectPersonById(dept.getOperator());
		if(person!=null)
			dept.setPerson(person);
		if(operatorPerson!=null)
			dept.setOperatorPerson(operatorPerson);
		Dept pDept = deptMapper.selectDeptById(dept.getPid());
		if(pDept!=null) {
			dept.setpDept(pDept);
		}
		/*Dept dept2 = new Dept();
		dept2.setPid(dept.getId());
		List<Dept> sDepts = deptMapper.selectDeptList(dept2);
		if(sDepts!=null) {
			List<Dept> ssDepts = new ArrayList<>();
			for (Dept dept3 : sDepts) {
				dept2.setPid(dept3.getId());
				ssDepts = deptMapper.selectDeptList(dept2);
				if(ssDepts!=null) {
					dept3.setsDepts(ssDepts);
				}
			}
			dept.setsDepts(sDepts);
		}*/
		Dept d = new Dept();
		List<Dept> sDepts = new ArrayList<>();
		d.setPid(dept.getId());
		List<Dept> sDepts2 = deptMapper.selectDeptList(d);
		if(sDepts2!=null&sDepts2.size()>0) {
			dept.setsDepts(sDepts2);
			for (Dept dept2 : sDepts2) {
				d.setPid(dept2.getId());
				List<Dept> sDepts3 = deptMapper.selectDeptList(d);
				if(sDepts3!=null&&sDepts3.size()>0) {
					dept2.setsDepts(sDepts3);
					for(Dept dept3 : sDepts3){
						d.setPid(dept3.getId());
						List<Dept> sDepts4 = deptMapper.selectDeptList(d);
						if(sDepts4!=null){
							dept3.setsDepts(sDepts4);
						}else{
							dept3.setsDepts(sDepts);
						}
					}
				}else{
					dept2.setsDepts(sDepts);
				}
			}
		}else {
			dept.setsDepts(sDepts);
		}
	}

	@Override
	public List<Dept> selectDeptTree() {
		Dept dept = new Dept();
		dept.setPid(0);
		List<Dept> fDepts = deptMapper.selectDeptList(dept);
		addSonDept(fDepts);
		return fDepts;
	}
	
	private void addSonDept(List<Dept> list) {
		Dept d = new Dept();
		List<Dept> sDepts = new ArrayList<>();
		if(sDepts!=null) {
			for (Dept dept : list) {
				d.setPid(dept.getId());
				List<Dept> sDepts2 = deptMapper.selectDeptList(d);
				if(sDepts2!=null&sDepts2.size()>0) {
					dept.setsDepts(sDepts2);
					for (Dept dept2 : sDepts2) {
						d.setPid(dept2.getId());
						List<Dept> sDepts3 = deptMapper.selectDeptList(d);
						if(sDepts3!=null&&sDepts3.size()>0) {
							dept2.setsDepts(sDepts3);
							for(Dept dept3 : sDepts3){
								d.setPid(dept3.getId());
								List<Dept> sDepts4 = deptMapper.selectDeptList(d);
								if(sDepts4!=null){
									dept3.setsDepts(sDepts4);
								}else{
									dept3.setsDepts(sDepts);
								}
							}
						}else{
							dept2.setsDepts(sDepts);
						}
					}
				}else {
					dept.setsDepts(sDepts);
				}
			}
			
		}
	}

	@Override
	public List<Dept> getHigherDept() {
		return deptMapper.selectHigherDept();
	}
}
