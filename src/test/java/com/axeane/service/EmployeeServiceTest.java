
package com.axeane.service;

        import com.axeane.MapstructCrudEmployeeApplication;
        import com.axeane.dto.EmployeeDTO;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.test.context.junit4.SpringRunner;

        import java.util.ArrayList;
        import java.util.List;

        import static org.hamcrest.CoreMatchers.is;
        import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MapstructCrudEmployeeApplication.class)
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void getAllSalaries() throws Exception {
        int sizeListSalarieBeforeSave = employeeService.getAllEmployees().size();
        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeName("soltani");
        employee.setEmployeeFirstName("Mustapha");
        employee.setEmployeeSexe("M");
        employee.setEmployeeAge(80);
        List<EmployeeDTO> listEmployeeAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            employeeService.save(employee);
            listEmployeeAfterSave = employeeService.getAllEmployees();
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listEmployeeAfterSave.size(), is(sizeListSalarieBeforeSave + 1));
        assertThat(throwException, is(false));
    }

    @Test
    public void addEmployee() throws Exception {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeName("soltani");
        employee.setEmployeeFirstName("Mustapha");

        employeeService.save(employee);
        EmployeeDTO employeeResult = employeeService.getAllEmployees().get(employeeService.getAllEmployees().size() - 1);
        assertThat(employeeResult.getEmployeeName(), is("soltani"));
        assertThat(employeeResult.getEmployeeFirstName(), is("Mustapha"));
    }


    @Test
    public void deleteSalarie() throws Exception {
        int sizeListEmployeeBeforeDelete = employeeService.getAllEmployees().size();
        EmployeeDTO employee = employeeService.getAllEmployees().get(employeeService.getAllEmployees().size() - 1);
        employeeService.delete(employee.getEmployeeId());
        int sizeListEmployeeAfterDelete = employeeService.getAllEmployees().size();
        assertThat(sizeListEmployeeBeforeDelete - 1, is(sizeListEmployeeAfterDelete));
    }
}