package com.samplecompany.computermanagementsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminNotificationPayload {
  private String level;
  private String employeeAbbreviation;
  private String message;
}
