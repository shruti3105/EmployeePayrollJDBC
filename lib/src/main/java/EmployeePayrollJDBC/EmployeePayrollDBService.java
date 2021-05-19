package EmployeePayrollJDBC;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {
	
	private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement employeePayrollDataStatement;

    EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance(){
        if (employeePayrollDBService==null)
            employeePayrollDBService=new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3307/payroll_service";
        String userName = "root";
        String password = "Shruti@3105";
        Connection connection;
        System.out.println("Connecting to database:" + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successful...!!!!" + connection);
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "select * from employee_payroll;";
        List<EmployeePayrollData> employeePayrollData = new ArrayList<>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate localDate = resultSet.getDate("start").toLocalDate();
                employeePayrollData.add(new EmployeePayrollData(id, name, salary, localDate));
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return employeePayrollData;
    }
    public List<EmployeePayrollData> getEmployeePayrollData(String name) throws DBException {
        List<EmployeePayrollData> employeePayrollDataList=null;
        if (this.employeePayrollDataStatement==null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1,name);
            ResultSet resultSet=employeePayrollDataStatement.executeQuery();
            employeePayrollDataList=this.employeePayrollData(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private List<EmployeePayrollData> employeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollDataList=new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate localDate = resultSet.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, localDate));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection=this.getConnection();
            String sql="SELECT * FROM employee_payroll where name=?";
            employeePayrollDataStatement=connection.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
    public Map<String, Double> readAverageSalaryByGender() throws DBException {
        String sql="select gender,avg(salary) as avg_salary from employee_payroll group by gender;";
        Map<String,Double> genderToAverageSalary=new HashMap<>();
        try(Connection connection=this.getConnection()){
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            while (resultSet.next()){
                String gender=resultSet.getString("gender");
                double avg_salary=resultSet.getDouble("avg_salary");
                genderToAverageSalary.put(gender,avg_salary);
            }
        } catch (SQLException throwables) {
            throw new DBException("Connection is Failed", DBException.ExceptionType.CONNECTION_FAIL);
        }
        return genderToAverageSalary;
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name,salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql=String.format("update employee_payroll set salary = %.2f where name = '%s';",salary,name);
        try(Connection connection=this.getConnection()) {
            Statement statement=connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
    
    public EmployeePayrollData addEmployeeToPayrollString(String name, double salary, LocalDate startDate, String gender){
    	int employeeId = -1;
    	EmployeePayrollData employeePayrollData = null;
        String sql=String.format("insert into employee_payroll (name,gender,salary,start) " +
                                    "values('%s','%s',%.2f,'%s');",name,gender,salary,Date.valueOf(startDate));
        try (Connection connection=this.getConnection()){
            Statement statement=connection.createStatement();
            int rowAffected=statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
            if (rowAffected==1){
                ResultSet resultSet=statement.getGeneratedKeys();
                if (resultSet.next()){
                    employeeId=resultSet.getInt(1);
                }
            }
            employeePayrollData=new EmployeePayrollData(employeeId,name,salary,startDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollData;
    }


}