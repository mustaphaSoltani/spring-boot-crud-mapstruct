package com.axeane.mapper;

import com.axeane.entity.Employee;
import com.axeane.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
@Mapper
public interface EmployeeMapper {
//    EmployeeDto employeeToEmployeDTO(Employee source);
//    Employee employeDTOToEmployee(EmployeeDto destination);


    @Mappings({ @Mapping(target = "employeeId", source = "entity.id"), @Mapping(target = "employeeName", source = "entity.name"), @Mapping(target = "employeeFirstName", source = "entity.firstName"),@Mapping(target = "employeeAge", source = "entity.age"),@Mapping(target = "employeeSexe", source = "entity.sexe") })
    EmployeeDTO employeeToEmployeDTO(Employee entity);

    @Mappings({ @Mapping(target = "id", source = "employeeId"), @Mapping(target = "name", source = "employeeName"), @Mapping(target = "firstName", source = "employeeFirstName"),@Mapping(target = "age", source = "employeeAge"),@Mapping(target = "sexe", source = "employeeSexe") })
    Employee employeDTOToEmployee(EmployeeDTO dto);

    List<Employee> convertEmployeeDTOListToEmployeeList(List<EmployeeDTO> list);

    List<EmployeeDTO> convertEmployeeListToEmployeeDTOList(List<Employee> list);

}
