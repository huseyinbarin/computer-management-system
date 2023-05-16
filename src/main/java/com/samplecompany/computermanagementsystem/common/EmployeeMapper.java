package com.samplecompany.computermanagementsystem.common;

import com.samplecompany.computermanagementsystem.domain.Employee;
import com.samplecompany.computermanagementsystem.resource.EmployeeResource;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
  public EmployeeResource toEmployeeResource(Employee employee) {
    return EmployeeResource.builder()
        .id(employee.getId())
        .fullName(employee.getFullName())
        .abbreviation(employee.getAbbreviation())
        .build();
  }

  public Employee toEntity(EmployeeResource employeeResource) {
    return Employee.builder()
        .id(employeeResource.getId())
        .fullName(employeeResource.getFullName())
        .abbreviation(employeeResource.getAbbreviation())
        .build();
  }
}
