package com.samplecompany.computermanagementsystem.repository;

import com.samplecompany.computermanagementsystem.domain.Computer;
import com.samplecompany.computermanagementsystem.domain.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ComputerRepository extends JpaRepository<Computer, Long> {

  List<Computer> findByEmployee(Employee employee);
}

