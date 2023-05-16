package com.samplecompany.computermanagementsystem.exception;

import jakarta.persistence.EntityNotFoundException;

public class EmployeeNotFoundException extends EntityNotFoundException {
  public EmployeeNotFoundException(String message) {
    super(message);
  }
}
