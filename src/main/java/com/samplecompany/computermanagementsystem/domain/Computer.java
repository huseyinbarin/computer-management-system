package com.samplecompany.computermanagementsystem.domain;

import com.samplecompany.computermanagementsystem.domain.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "computers")
public class Computer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String macAddress;

  @Column(nullable = false)
  private String computerName;

  @Column(nullable = false)
  private String ipAddress;

  @Column(name = "employee_abbreviation")
  private String employeeAbbreviation;

  private String description;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;
}
