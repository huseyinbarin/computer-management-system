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
public class EmployeeResource {
  private Long id;
  private String fullName;
  private String abbreviation;
}
