package com.axeane.service;

import com.axeane.entity.Employee;
import com.axeane.dto.EmployeeDTO;
import com.axeane.mapper.EmployeeMapper;
import com.axeane.repository.EmployeeRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

    /**
     * Service Implementation for managing Employee.
     */
    @Service
    @Transactional
    public class EmployeeService {
        private final Logger log = LoggerFactory.getLogger(EmployeeService.class);
        private EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);
        private final EmployeeRepository employeeRepository;

        public EmployeeService(EmployeeRepository proprietorRepository) {
            this.employeeRepository = proprietorRepository;
        }

        /**
         * Save a employee.
         *
         * @param employeeDTO the entity to save
         * @return the persisted entity
         */
        @Transactional
        public Employee save(EmployeeDTO employeeDTO) {
            log.debug("Request to save Employee : {}", employeeDTO);
           Employee employee =mapper.employeDTOToEmployee(employeeDTO);
            employee.setName(employeeDTO.getEmployeeName());
            employee.setFirstName(employeeDTO.getEmployeeFirstName());
            employee.setAge(employeeDTO.getEmployeeAge());
            employee.setSexe(employeeDTO.getEmployeeSexe());

            return employeeRepository.save(employee);
        }

        /**
         * Get all the Employee.
         *
         * @return the list of entities
         */
        @Transactional(readOnly = true)
        public List<EmployeeDTO> getAllEmployees() {
            log.debug("Request to get all Employee");
            List<Employee> list = (List<Employee>) employeeRepository.findAll();
            return mapper.convertEmployeeListToEmployeeDTOList(list);
        }
        /**
         * Get one Employee by id.
         *
         * @param id the id of the entity
         * @return the entity
         */
        @Transactional(readOnly = true)
        public Optional<EmployeeDTO> findOne(Integer id) {
            log.debug("Request to get Employee : {}", id);
            Optional<Employee> employee = employeeRepository.findById(id);
                return Optional.ofNullable(mapper.employeeToEmployeDTO(employee.get()));
        }

        /**
         * Delete the  Employee by id.
         *
         * @param id the id of the entity
         */

        public void delete(Integer id) {
            log.debug("Request to delete Employee : {}", id);
            employeeRepository.deleteById(id);
        }

}
