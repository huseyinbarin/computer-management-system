package com.samplecompany.computermanagementsystem.exception;

import jakarta.persistence.EntityNotFoundException;

public class ComputerNotFoundException extends EntityNotFoundException {
  public ComputerNotFoundException(String message) {
    super(message);
  }
}
