package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
		
	public List<Department> findAll() {		
		return dao.findAll();	
	}
	
	public void SaveOrUpdate(Department department) {
		if (department.getId() == null) {
			dao.insert(department);
		}
		else {
			dao.update(department);
		}
	}
	
	public void remove(Department obj) {
		dao.deleteById(obj.getId());
	}

}
