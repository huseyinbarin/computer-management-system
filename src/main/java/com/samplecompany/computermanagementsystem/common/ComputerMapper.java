package com.samplecompany.computermanagementsystem.common;

import com.samplecompany.computermanagementsystem.domain.Computer;
import com.samplecompany.computermanagementsystem.resource.ComputerResource;
import org.springframework.stereotype.Component;

@Component
public class ComputerMapper {

  public ComputerResource toCustomerResource(Computer computer) {
    return ComputerResource.builder()
        .id(computer.getId())
        .macAddress(computer.getMacAddress())
        .computerName(computer.getComputerName())
        .ipAddress(computer.getIpAddress())
        .employeeAbbreviation(computer.getEmployee() != null ? computer.getEmployee().getAbbreviation() : "N/A")
        .employee(computer.getEmployee())
        .description(computer.getDescription())
        .build();
  }

  public Computer toEntity(ComputerResource customerResource) {
    return Computer.builder()
        .id(customerResource.getId())
        .macAddress(customerResource.getMacAddress())
        .computerName(customerResource.getComputerName())
        .ipAddress(customerResource.getIpAddress())
        .employeeAbbreviation(customerResource.getEmployee() != null ? customerResource.getEmployee().getAbbreviation() : "N/A")
        .employee(customerResource.getEmployee())
        .description(customerResource.getDescription())
        .build();
  }
}