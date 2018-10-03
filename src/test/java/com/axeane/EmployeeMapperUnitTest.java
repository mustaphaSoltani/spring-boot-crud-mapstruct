package com.axeane;

import com.axeane.dto.EmployeeDTO;
import com.axeane.entity.Employee;
import com.axeane.mapper.EmployeeMapper;
import com.axeane.service.EmployeeService;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.Assert.assertEquals;

public class EmployeeMapperUnitTest {

    private EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);
    private EmployeeService employeeService;
    @Test
    public void givenEmployeeToEmployeeDto_whenMaps_thenCorrect() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeName("empName");
        employeeDTO.setEmployeeAge(70);

        Employee employee = mapper.employeDTOToEmployee(employeeDTO);
        //employeeService.save(employee);
        assertEquals(employee.getName(), employeeDTO.getEmployeeName());
        assertEquals(employee.getAge(), employeeDTO.getEmployeeAge());
    }

    @Test
    public void givenDestinationToSource_whenMaps_thenCorrect() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeName("DestinationName");
        employeeDTO.setEmployeeAge(40);
        Employee employee = mapper.employeDTOToEmployee(employeeDTO);
        assertEquals(employeeDTO.getEmployeeName(), employee.getName());
        assertEquals(employeeDTO.getEmployeeAge(),employee.getAge());
    }
}

