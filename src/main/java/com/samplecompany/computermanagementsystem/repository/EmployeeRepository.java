package com.samplecompany.computermanagementsystem.repository;

import com.samplecompany.computermanagementsystem.domain.Employee;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  Optional<Employee> findByAbbreviation(String abbreviation);
}
