package com.samplecompany.computermanagementsystem.service;

import com.samplecompany.computermanagementsystem.common.ComputerMapper;
import com.samplecompany.computermanagementsystem.common.EmployeeMapper;
import com.samplecompany.computermanagementsystem.domain.Employee;
import com.samplecompany.computermanagementsystem.repository.EmployeeRepository;
import com.samplecompany.computermanagementsystem.resource.EmployeeResource;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper;

  @Autowired
  public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
    this.employeeRepository = employeeRepository;
    this.employeeMapper = employeeMapper;
  }

  public EmployeeResource addEmployee(EmployeeResource employeeResource) {
    Employee employee = employeeMapper.toEntity(employeeResource);

    if (employeeResource.getAbbreviation()== null && employeeResource.getFullName() != null) {
      employee.setAbbreviation(generateAbbreviation(employeeResource.getFullName()));
    }

    Employee savedEmployee = employeeRepository.save(employee);
    return employeeMapper.toEmployeeResource(savedEmployee);
  }

  private String generateAbbreviation(String fullName) {
    String[] nameParts = fullName.split(" ");
    StringBuilder abbreviationBuilder = new StringBuilder();

    for (String part : nameParts) {
      if (!Strings.isBlank(part)) {
        abbreviationBuilder.append(part.charAt(0));
      }

      if (abbreviationBuilder.length() >= 3) {
        break;
      }
    }

    return abbreviationBuilder.toString().toLowerCase();
  }
}
