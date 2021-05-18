package EmployeePayrollJDBC;

import static EmployeePayrollJDBC.EmployeePayrollService.IOService.DB_IO;
import static EmployeePayrollJDBC.EmployeePayrollService.IOService.FILE_IO;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class EmployeePayrollServiceTest {
	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchNumberOfEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmployees = { 
				new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
				new EmployeePayrollData(2, "Bill Gates", 200000.0),
				new EmployeePayrollData(3, "Mark Zukerberg", 300000.0) };

		EmployeePayrollService payrollServiceObject = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
		payrollServiceObject.writeEmployeeData(EmployeePayrollService.IOService.FILE_IO);
		payrollServiceObject.printEmployeePayrollData();
		Assert.assertEquals(3, payrollServiceObject.countEnteries(EmployeePayrollService.IOService.FILE_IO));
	}

	@Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assert.assertEquals(5, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch() throws DBException {
        EmployeePayrollService employeePayrollService=new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }
}