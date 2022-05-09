package com.example.Employeeinfo.repository;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.Employeeinfo.models.Employee;

@Profile("dev")
public interface EmployeeRepository extends CrudRepository<Employee,Integer>{
	
	@Query(value="select * from employee b inner join department c on b.department_dep_id=c.dep_id order by b.emp_salary desc,b.emp_name asc,c.department_name",nativeQuery=true)
	public List<Employee> getEmployees();
	
	List<Employee> findByEmpNameStartsWith(String empName);
}
