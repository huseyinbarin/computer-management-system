package com.samplecompany.computermanagementsystem.service;

import com.samplecompany.computermanagementsystem.common.ComputerMapper;
import com.samplecompany.computermanagementsystem.domain.Computer;
import com.samplecompany.computermanagementsystem.domain.Employee;
import com.samplecompany.computermanagementsystem.exception.ComputerNotFoundException;
import com.samplecompany.computermanagementsystem.exception.EmployeeNotFoundException;
import com.samplecompany.computermanagementsystem.repository.ComputerRepository;
import com.samplecompany.computermanagementsystem.repository.EmployeeRepository;
import com.samplecompany.computermanagementsystem.resource.ComputerResource;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service public class ComputerService {
  private final ComputerRepository computerRepository;
  private final EmployeeRepository employeeRepository;
  private final AdminNotificationService adminNotificationService;
  private final ComputerMapper computerMapper;

  @Autowired public ComputerService(ComputerRepository computerRepository,
      EmployeeRepository employeeRepository, AdminNotificationService adminNotificationService,
      ComputerMapper computerMapper) {
    this.computerRepository = computerRepository;
    this.employeeRepository = employeeRepository;
    this.adminNotificationService = adminNotificationService;
    this.computerMapper = computerMapper;
  }

  public ComputerService(ComputerRepository computerRepository,
      EmployeeRepository employeeRepository, AdminNotificationService adminNotificationService) {
    this.computerRepository = computerRepository;
    this.employeeRepository = employeeRepository;
    this.adminNotificationService = adminNotificationService;
    this.computerMapper = new ComputerMapper();
  }

  public ComputerResource addComputer(ComputerResource computerResource) {

    Computer computer = Computer.builder()
        .macAddress(computerResource.getMacAddress())
        .computerName(computerResource.getComputerName())
        .ipAddress(computerResource.getIpAddress())
        .employeeAbbreviation(computerResource.getEmployeeAbbreviation())
        .employee(computerResource.getEmployee())
        .description(computerResource.getDescription())
        .build();
    computer = computerRepository.save(computer);

    return computerMapper.toCustomerResource(computer);
  }

  public List<ComputerResource> getAllComputers() {
    List<Computer> computers = computerRepository.findAll();
    return computers.stream().map(computerMapper::toCustomerResource).collect(Collectors.toList());
  }

  public List<ComputerResource> getComputersByEmployeeByAbbreviation(String abbreviation) {
    Employee employee = employeeRepository.findByAbbreviation(abbreviation)
        .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

    List<Computer> computers = computerRepository.findByEmployee(employee);
    return computers.stream().map(computerMapper::toCustomerResource).collect(Collectors.toList());
  }

  public List<ComputerResource> getComputersByEmployeeById(Long id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

    List<Computer> computers = computerRepository.findByEmployee(employee);
    return computers.stream().map(computerMapper::toCustomerResource).collect(Collectors.toList());
  }

  public ComputerResource getComputerById(Long id) {
    Computer computer = computerRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Computer not found"));
    return computerMapper.toCustomerResource(computer);
  }

  public void removeComputer(Long id) {
    Computer computer = computerRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Computer not found"));
    computerRepository.delete(computer);
  }

  public ComputerResource assignComputerToEmployee(Long id, Long employeeId) {
    Computer computer = computerRepository.findById(id)
        .orElseThrow(() -> new ComputerNotFoundException("Computer not found for given id"));

    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for given id"));

    computer.setEmployee(employee);
    computerRepository.save(computer);

    checkAndNotifyAssignedComputers(employee);

    return computerMapper.toCustomerResource(computer);
  }

  void checkAndNotifyAssignedComputers(Employee employee) {
    List<Computer> assignedComputers = computerRepository.findByEmployee(employee);
    int assignedComputerCount = assignedComputers.size();

    if (assignedComputerCount >= 3) {
      String message =
          String.format("Employee %s has been assigned %d computers.", employee.getAbbreviation(),
              assignedComputerCount);

      adminNotificationService.sendNotification("Warning", employee.getAbbreviation(), message);
    }
  }
}
