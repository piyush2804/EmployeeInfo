package com.example.Employeeinfo.exception;

public class NameNotFoundException extends RuntimeException{
	public NameNotFoundException()
	{
		super("No records found");
	}
}
