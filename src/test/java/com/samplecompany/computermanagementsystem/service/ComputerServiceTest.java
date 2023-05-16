package com.samplecompany.computermanagementsystem.service;

import com.samplecompany.computermanagementsystem.common.ComputerMapper;
import com.samplecompany.computermanagementsystem.domain.Computer;
import com.samplecompany.computermanagementsystem.domain.Employee;
import com.samplecompany.computermanagementsystem.exception.ComputerNotFoundException;
import com.samplecompany.computermanagementsystem.exception.EmployeeNotFoundException;
import com.samplecompany.computermanagementsystem.repository.ComputerRepository;
import com.samplecompany.computermanagementsystem.repository.EmployeeRepository;
import com.samplecompany.computermanagementsystem.resource.ComputerResource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ComputerServiceTest {
  @Mock
  private ComputerRepository computerRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private AdminNotificationService adminNotificationService;
  @Mock
  private ComputerMapper computerMapper;

  private ComputerService computerService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    computerService =
        new ComputerService(computerRepository, employeeRepository, adminNotificationService);
  }

  @Test
  public void testAddComputer_Success() {
    // Prepare the input data
    ComputerResource computerResource = new ComputerResource();
    computerResource.setMacAddress("12345");
    computerResource.setComputerName("Test Computer");
    computerResource.setIpAddress("192.168.1.100");
    computerResource.setEmployeeAbbreviation("EMP001");
    computerResource.setDescription("Test Description");

    Employee employee = new Employee();
    employee.setAbbreviation("EMP001");

    when(employeeRepository.findByAbbreviation("EMP001")).thenReturn(Optional.of(employee));

    //When the save method is called with any Computer object, it will capture the object, assign an ID to it, and return it.
    when(computerRepository.save(any(Computer.class))).thenAnswer(invocation -> {
      Computer savedComputer = invocation.getArgument(0);
      savedComputer.setId(1L); // Assign an ID to the saved computer
      return savedComputer;
    });

    ComputerResource result = computerService.addComputer(computerResource);

    assertNotNull(result);
    assertEquals("12345", result.getMacAddress());
    assertEquals("Test Computer", result.getComputerName());
    assertEquals("192.168.1.100", result.getIpAddress());
    assertEquals("Test Description", result.getDescription());

    verify(computerRepository).save(any(Computer.class));
  }

  @Test
  public void testGetAllComputers_Success() {
    Computer computer = new Computer();
    when(computerRepository.findAll()).thenReturn(Collections.singletonList(computer));

    List<ComputerResource> result = computerService.getAllComputers();

    assertNotNull(result);
    assertEquals(1, result.size());

    verify(computerRepository).findAll();
  }

  @Test
  public void testGetComputersByEmployee_Success() {
    // Arrange
    String employeeAbbreviation = "EMP001";
    Employee employee = new Employee();
    employee.setAbbreviation(employeeAbbreviation);

    Computer computer1 = new Computer();
    computer1.setId(1L);
    computer1.setMacAddress("MAC001");
    computer1.setComputerName("Computer 1");
    computer1.setEmployee(employee);

    Computer computer2 = new Computer();
    computer2.setId(2L);
    computer2.setMacAddress("MAC002");
    computer2.setComputerName("Computer 2");
    computer2.setEmployee(employee);

    List<Computer> computers = Arrays.asList(computer1, computer2);

    ComputerMapper computerMapper = Mockito.mock(ComputerMapper.class);
    when(employeeRepository.findByAbbreviation(employeeAbbreviation)).thenReturn(
        Optional.of(employee));
    when(computerRepository.findByEmployee(employee)).thenReturn(computers);
    when(computerMapper.toCustomerResource(computer1)).thenReturn(mapToResource(computer1));
    when(computerMapper.toCustomerResource(computer2)).thenReturn(mapToResource(computer2));

    List<ComputerResource> result = computerService.getComputersByEmployeeByAbbreviation(employeeAbbreviation);

    assertEquals(2, result.size());
    assertEquals(computer1.getId(), result.get(0).getId());
    assertEquals(computer1.getMacAddress(), result.get(0).getMacAddress());
    assertEquals(computer1.getComputerName(), result.get(0).getComputerName());
    assertEquals(computer2.getId(), result.get(1).getId());
    assertEquals(computer2.getMacAddress(), result.get(1).getMacAddress());
    assertEquals(computer2.getComputerName(), result.get(1).getComputerName());
  }

  @Test
  public void testGetComputersByEmployee_EmployeeNotFound() {
    String employeeAbbreviation = "EMP001";

    when(employeeRepository.findByAbbreviation(employeeAbbreviation)).thenReturn(Optional.empty());

    assertThrows(EmployeeNotFoundException.class,
        () -> computerService.getComputersByEmployeeByAbbreviation(employeeAbbreviation));
  }

  @Test
  public void testAssignComputerToEmployee_WhenComputerNotFound() {
    Computer computer = new Computer();
    computer.setId(1L);

    Employee employee = new Employee();
    employee.setId(1L);
    employee.setAbbreviation("EMP001");

    // Mock the repository methods to return empty optionals
    when(computerRepository.findById(anyLong())).thenReturn(Optional.empty());
    when(employeeRepository.findByAbbreviation(anyString())).thenReturn(Optional.of(employee));

    assertThrows(ComputerNotFoundException.class,
        () -> computerService.assignComputerToEmployee(1L, employee.getId()));

    assertNull(computer.getEmployee());

    verify(computerRepository, never()).save(any());
  }

  @Test
  public void testAssignComputerToEmployee_WhenEmployeeNotFound() {
    Computer computer = new Computer();
    computer.setId(1L);

    // Mock the repository methods to return empty optionals
    when(computerRepository.findById(anyLong())).thenReturn(Optional.of(computer));
    when(employeeRepository.findByAbbreviation(anyString())).thenReturn(Optional.empty());

    assertThrows(EmployeeNotFoundException.class,
        () -> computerService.assignComputerToEmployee(computer.getId(), anyLong()));

    assertNull(computer.getEmployee());

    // Verify that the computer has not been saved
    verify(computerRepository, never()).save(any());
  }

  @Test
  public void testAssignComputerToEmployee_Success() {
    Long computerId = 1L;
    Long employeeId = 2L;

    Computer computer = new Computer();
    computer.setId(computerId);

    Employee employee = new Employee();
    employee.setId(employeeId);

    ComputerResource expectedComputerResource = new ComputerResource();

    when(computerRepository.findById(computerId)).thenReturn(Optional.of(computer));
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(computerMapper.toCustomerResource(computer)).thenReturn(expectedComputerResource);

    computerService.assignComputerToEmployee(computerId, employeeId);

    verify(computerRepository, times(1)).findById(computerId);
    verify(employeeRepository, times(1)).findById(employeeId);
    verify(computerRepository, times(1)).save(computer);
  }

  @Test
  public void testAssignComputerToEmployeeMoreThanTree_Success() {
    Long computer1Id = 1L;
    Long computer2Id = 2L;
    Long computer3Id = 3L;
    Long employeeId = 2L;

    Computer computer1 = new Computer();
    computer1.setId(computer1Id);

    Computer computer2 = new Computer();
    computer2.setId(computer2Id);

    Computer computer3 = new Computer();
    computer3.setId(computer3Id);

    Employee employee = new Employee();
    employee.setId(employeeId);

    when(computerRepository.findById(computer1Id)).thenReturn(Optional.of(computer1));
    when(computerRepository.findById(computer2Id)).thenReturn(Optional.of(computer2));
    when(computerRepository.findById(computer3Id)).thenReturn(Optional.of(computer3));

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(computerRepository.findByEmployee(employee)).thenReturn(
        Arrays.asList(computer1, computer2, computer3));

    computerService.assignComputerToEmployee(3L, 2L);

    verify(adminNotificationService, times(1))
        .sendNotification("Warning",
        null,
        "Employee null has been assigned 3 computers.");
  }

  private ComputerResource mapToResource(Computer computer) {
    ComputerResource computerResource = new ComputerResource();
    computerResource.setId(computer.getId());
    computerResource.setMacAddress(computer.getMacAddress());
    computerResource.setComputerName(computer.getComputerName());

    return computerResource;
  }
}