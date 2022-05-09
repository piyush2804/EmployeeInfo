package com.example.Employeeinfo.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.Employeeinfo.MyUserDetailsService;
import com.example.Employeeinfo.exception.NameNotFoundException;
import com.example.Employeeinfo.models.AuthenticationRequest;
import com.example.Employeeinfo.models.AuthenticationResponse;
import com.example.Employeeinfo.models.Employee;
import com.example.Employeeinfo.repository.EmployeeRepository;
import com.example.Employeeinfo.util.JwtUtil;

@RestController
@CrossOrigin
public class EmployeeController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	String jwt2;
	
	@GetMapping("/restTemplate")
	public void GetUsers(@RequestHeader("Authorization") String jwt) {

		String customerAPIUrl = "http://localhost:8082/getEmployee";
        HttpHeaders headers = new HttpHeaders();
//      accessToken can be the secret key you generate.
        headers.set("Authorization", jwt); 
//      String requestJson = "{\"email\":\"abc@gmail.com\"}";
        HttpEntity <Employee> entity= new HttpEntity<Employee>(headers);
//      HttpEntity <String> entity = new HttpEntity <> (requestJson, headers);
        ResponseEntity <List<Employee>> employees = restTemplate.exchange(customerAPIUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Employee>>() {
          });
        System.out.println(employees);
	}
	//get all employees
	@GetMapping(value="/getEmployee")
	public List<Employee> getEmployeeInfo() {
		return employeeRepository.getEmployees();
	}
	//add employee with department name
	@PostMapping(value="/setEmployee")
	public void setEmployeeInfo(@RequestBody Employee employee) {
		employee.setCreated(LocalDateTime.now());
		employeeRepository.save(employee);
	}
	//delete employee
	@DeleteMapping(value="/{id}")
	public Integer DeleteEmployee(@PathVariable Integer id) {
		employeeRepository.deleteById(id);
		return id;
	}
	//search by name
	@PostMapping(value="/search")
	public List<Employee> setEmployeeInfo(@RequestBody String name) {
		try {
		if(employeeRepository.findByEmpNameStartsWith(name).isEmpty())
		{
			throw new NameNotFoundException();
		}
		}
		catch(NameNotFoundException e) {
			System.out.println(e);
		}
		return (employeeRepository.findByEmpNameStartsWith(name));
	}
	//search by name
	@PostMapping(value="/search3")
	public List<Employee> setEmployeeInfo3(@RequestBody Employee obj) {
		return (employeeRepository.findByEmpNameStartsWith(obj.getEmpName()));
	}
	//search using java 8 new features
	@PostMapping(value="/search2")
	public List<Employee> searchUsingStream(@RequestParam String name) {
		
		List<Employee> employees=(List<Employee>) employeeRepository.findAll();
		
		List<Employee> list=new ArrayList<>();
		
		employees.stream().filter(e->e.getEmpName().equals(name)).forEach(e-> {
				list.add(e);
		});
		return list;		
	}
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		jwt2=jwt;
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
