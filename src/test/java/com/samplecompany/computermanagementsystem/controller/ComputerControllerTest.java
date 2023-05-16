package com.samplecompany.computermanagementsystem.controller;

import com.samplecompany.computermanagementsystem.domain.Computer;
import com.samplecompany.computermanagementsystem.domain.Employee;
import com.samplecompany.computermanagementsystem.repository.ComputerRepository;
import com.samplecompany.computermanagementsystem.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ComputerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ComputerRepository computerRepository;

  @Autowired
  private EmployeeRepository employeeRepository;


  @Before
  public void setup() {
    // Clear the database tables before each test
    computerRepository.deleteAll();
    employeeRepository.deleteAll();
  }
  @Test
  public void testGetAllComputers() throws Exception {
    // Add test data to the database
    Employee employee = Employee.builder()
        .fullName("John")
        .abbreviation("EMP001")
        // Set other properties of the employee object as needed
        .build();
    Employee savedEmployee = employeeRepository.save(employee);

    Computer computer1 = Computer.builder()
        .macAddress("12345")
        .computerName("Computer 1")
        .ipAddress("192.168.0.1")
        .employee(savedEmployee)
        .build();

    Computer computer2 = Computer.builder()
        .macAddress("67890")
        .computerName("Computer 2")
        .ipAddress("192.168.0.2")
        .employee(savedEmployee)
        .build();

    computerRepository.saveAll(Arrays.asList(computer1, computer2));

    // Perform GET request to retrieve all computers
    mockMvc.perform(get("/api/computers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].macAddress", is("12345")))
        .andExpect(jsonPath("$[0].computerName", is("Computer 1")))
        .andExpect(jsonPath("$[0].ipAddress", is("192.168.0.1")))
        .andExpect(jsonPath("$[0].employeeAbbreviation", is("EMP001")))
        .andExpect(jsonPath("$[1].macAddress", is("67890")))
        .andExpect(jsonPath("$[1].computerName", is("Computer 2")))
        .andExpect(jsonPath("$[1].ipAddress", is("192.168.0.2")))
        .andExpect(jsonPath("$[1].employeeAbbreviation", is("EMP001")));
  }

  @Test
  public void testGetComputerById() throws Exception {
    // Add test data to the database
    Computer computer = Computer.builder()
        .macAddress("12345")
        .computerName("Computer 1")
        .ipAddress("192.168.0.1")
        .employee(null)
        .build();

    computerRepository.save(computer);

    // Perform GET request to retrieve a specific computer by ID
    mockMvc.perform(get("/api/computers/{id}", computer.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(computer.getId().intValue())))
        .andExpect(jsonPath("$.macAddress", is(computer.getMacAddress())))
        .andExpect(jsonPath("$.computerName", is(computer.getComputerName())))
        .andExpect(jsonPath("$.ipAddress", is(computer.getIpAddress())))
        .andExpect(jsonPath("$.employeeAbbreviation",is("N/A")))
        .andExpect(jsonPath("$.description", is(computer.getDescription())));

  }

  @Test
  public void testAddComputer() throws Exception {


    // Create a JSON object representing the computer to be added
    JSONObject computerJson = new JSONObject();
    computerJson.put("macAddress", "12345");
    computerJson.put("computerName", "Computer 1");
    computerJson.put("ipAddress", "192.168.0.1");
    computerJson.put("description", "Description");
    computerJson.put("employeeAbbreviation","hb");


    // Perform POST request to add a new computer
    mockMvc.perform(post("/api/computers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(computerJson.toString()))
        .andExpect(status().isCreated());


    // Verify that the computer is added by checking the database
    List<Computer> computers = computerRepository.findAll();
    assertThat(computers, hasSize(1));
    Computer computer = computers.get(0);
    assertThat(computer.getMacAddress(), is("12345"));
    assertThat(computer.getComputerName(), is("Computer 1"));
    assertThat(computer.getIpAddress(), is("192.168.0.1"));
    assertThat(computer.getDescription(), is("Description"));

  }

  @Test
  public void test_AssignComputerToEmployee() throws Exception {
    // Create a test computer
    Computer computer = new Computer();
    computer.setMacAddress("12345");
    computer.setComputerName("Test Computer");
    computer.setIpAddress("192.168.1.100");
    computer.setDescription("Test computer description");
    computer = computerRepository.save(computer);

    // Create a test employee
    Employee employee =  Employee.builder()
        .fullName("Huseyin")
        .abbreviation("hb")
        .build();
    employeeRepository.save(employee);

    // Assign the computer to the employee
    mockMvc.perform(put("/api/computers/{id}/assign/{employeeId}", computer.getId(),employee.getId())
            .param("abbreviation", employee.getAbbreviation()))
        .andExpect(status().isOk());

    // Verify that the computer is assigned to the employee
    Computer updatedComputer = computerRepository.findById(computer.getId())
        .orElseThrow(() -> new EntityNotFoundException("Computer not found"));
    assertThat(updatedComputer.getEmployee(), is(employee));
  }
}