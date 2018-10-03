package com.axeane.controller;

import com.axeane.Exception.EmployeeNotFoundException;
import com.axeane.entity.Employee;
import com.axeane.dto.EmployeeDTO;
import com.axeane.repository.EmployeeRepository;
import com.axeane.service.EmployeeService;
import com.axeane.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    private final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private static final String ENTITY_NAME = "employee";
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    public EmployeeController(EmployeeService employeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeService;
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping("/")
    public String welcome() {
        return "Welcome to MupStruct Example.";
    }

    @GetMapping(value = "/employee")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        log.debug("REST request to get a page of Employee");
        List<EmployeeDTO> page = employeeService.getAllEmployees();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping(value = "/employee/{id}")
    public ResponseEntity<Optional<EmployeeDTO>> getEmployee(@PathVariable("id") Integer id) {
        log.debug("REST request to get  of Employee");
        Optional<EmployeeDTO> employee = employeeService.findOne(id);
        if (!employee.isPresent())
            throw new EmployeeNotFoundException("id-" + id);
        Optional<EmployeeDTO> employee1 = employeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(employee1));
    }

    @PostMapping(value = "/employee")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeDTO emp) throws URISyntaxException {
        System.out.println("(Service Side) Creating Empployee: " + emp.getEmployeeName());

        Employee result = employeeService.save(emp);
        return ResponseEntity.created(new URI("/employee"))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping(value = "/employee")
    public ResponseEntity<Employee> updateSalarie(@Valid @RequestBody EmployeeDTO emp) throws URISyntaxException {
        System.out.println("(Service Side) Editing employee: " + emp.getEmployeeName());
        if (emp.getEmployeeId() == null) {
            return addEmployee(emp);
        }
        Employee result = employeeService.save(emp);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, emp.getEmployeeId().toString()))
                .body(result);
    }

    @DeleteMapping(value = "/employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Integer id) {
        System.out.println("(Service Side) Deleting employee: " + id);
        employeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
