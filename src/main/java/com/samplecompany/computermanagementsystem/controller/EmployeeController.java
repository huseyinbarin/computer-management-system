package com.samplecompany.computermanagementsystem.controller;

import com.samplecompany.computermanagementsystem.domain.Employee;
import com.samplecompany.computermanagementsystem.repository.EmployeeRepository;
import com.samplecompany.computermanagementsystem.resource.EmployeeResource;
import com.samplecompany.computermanagementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @PostMapping
  public ResponseEntity<EmployeeResource> addEmployee(@RequestBody EmployeeResource employeeResource) {

    EmployeeResource savedEmployee = employeeService.addEmployee(employeeResource);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
  }

}
