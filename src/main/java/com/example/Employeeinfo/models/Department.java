package com.example.Employeeinfo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="department")
public class Department {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int dep_id;
	private String departmentName;
	public Department() {
		
	}
	public int getDep_id() {
		return dep_id;
	}
	public void setDep_id(int dep_id) {
		this.dep_id = dep_id;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	@Override
	public String toString() {
		return "Department [dep_id=" + dep_id + ", departmentName=" + departmentName + "]";
	}
	public Department(int dep_id, String departmentName) {
		super();
		this.dep_id = dep_id;
		this.departmentName = departmentName;
	}
	
}
