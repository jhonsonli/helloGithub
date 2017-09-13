package cn.tarena.ht.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tarena.ht.mapper.DeptMapper;
import cn.tarena.ht.pojo.Dept;

@Service
public class DeptServiceImpl implements DeptService {
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Override
	public List<Dept> findAll() {
		
		return deptMapper.findAll();
	}

	@Override
	public void deleteDept(String[] deptIds) {
		
		deptMapper.deletDept(deptIds);
	}

	@Override
	public void updateState(String[] deptIds, int state) {
	
		deptMapper.updateState(deptIds,state);
		
	}

	@Override
	public void saveDept(Dept dept) {
		
		//部门添加时间
		dept.setCreateTime(new Date());
		dept.setUpdateTime(dept.getCreateTime());
		
		deptMapper.saveDept(dept);
		
	}

	@Override
	public Dept findDeptById(String deptId) {
		
		return deptMapper.findDeptById(deptId);
	}

	@Override
	public List<Dept> findParentList(String deptId) {
		
		return deptMapper.findParentList(deptId);
	}

	@Override
	public void updateDept(Dept dept) {
		dept.setUpdateTime(new Date());
		deptMapper.updateDept(dept);
	}

}
