package com.samplecompany.computermanagementsystem.controller;

import com.samplecompany.computermanagementsystem.resource.ComputerResource;
import com.samplecompany.computermanagementsystem.service.ComputerService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/computers") public class ComputerController {

  private final ComputerService computerService;

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
    String errorMessage = "Reason: " + ex.getMessage();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
  }

  @Autowired public ComputerController(ComputerService computerService) {
    this.computerService = computerService;
  }

  @PostMapping public ResponseEntity<ComputerResource> addComputer(
      @RequestBody ComputerResource ComputerResource) {
    ComputerResource createdComputer = computerService.addComputer(ComputerResource);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdComputer);
  }

  @GetMapping public ResponseEntity<List<ComputerResource>> getAllComputers() {
    List<ComputerResource> computers = computerService.getAllComputers();
    return ResponseEntity.ok(computers);
  }


  @GetMapping("/employee/id/{employeeId}")
  public ResponseEntity<List<ComputerResource>> getComputersByEmployeeId(@PathVariable Long employeeId) {
    List<ComputerResource> computers = computerService.getComputersByEmployeeById(employeeId);
    return ResponseEntity.ok(computers);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ComputerResource> getComputerById(@PathVariable Long id) {
    ComputerResource computer = computerService.getComputerById(id);
    return ResponseEntity.ok(computer);
  }

  @DeleteMapping("/{id}") public ResponseEntity<Void> removeComputer(@PathVariable Long id) {
    computerService.removeComputer(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{computerId}/assign/{employeeId}")
  public ResponseEntity<ComputerResource> assignComputerToEmployee(@PathVariable Long computerId,
      @PathVariable Long employeeId) {

    ComputerResource computerResource =
        computerService.assignComputerToEmployee(computerId, employeeId);

    return ResponseEntity.ok(computerResource);
  }
}
