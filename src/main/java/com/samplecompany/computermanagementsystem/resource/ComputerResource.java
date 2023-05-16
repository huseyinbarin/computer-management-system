package com.samplecompany.computermanagementsystem.resource;

import com.samplecompany.computermanagementsystem.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComputerResource {
  private Long id;
  private String macAddress;
  private String computerName;
  private String ipAddress;
  private String employeeAbbreviation;
  private String description;
  private Employee employee;
}
