package com.samplecompany.computermanagementsystem.service;

import com.samplecompany.computermanagementsystem.domain.AdminNotificationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdminNotificationService {
  private static final String NOTIFICATION_ENDPOINT = "http://localhost:8080/api/notify";

  private final RestTemplate restTemplate;

  @Autowired
  public AdminNotificationService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void sendNotification(String level, String employeeAbbreviation, String message) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    AdminNotificationPayload payload = new AdminNotificationPayload(level, employeeAbbreviation, message);
    HttpEntity<AdminNotificationPayload> requestEntity = new HttpEntity<>(payload, headers);

    restTemplate.exchange(NOTIFICATION_ENDPOINT, HttpMethod.POST, requestEntity, Void.class);
  }
}