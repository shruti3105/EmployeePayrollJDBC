#UC1
show databases;
create database payroll_service;
use payroll_service;

#UC2
create table employee_payroll
(
id int(50) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
name varchar(150) NOT NULL,
salary double NOT NULL,
start date NOT NULL
);
show tables;
desc employee_payroll;

#UC3
insert into employee_payroll values
('Bill',100000.00,'2018-01-03'),
('Martha',200000.00,'2019-11-13'),
('Charlie',300000.00,'2020-05-21');

#UC4
select * from employee_payroll;

#UC5
select salary from employee_payroll where name='Bill';
select * from employee_payroll where start between cast('2018-01-01' as date) and date(now());

#UC6
alter table employee_payroll add gender char(1) after name;
update employee_payroll set gender='M' where name='Bill' or name='Charlie';

#UC7
update employee_payroll set gender='F' where name='Martha';
select sum(salary) from employee_payroll where gender='F' group by gender;
select sum(salary) from employee_payroll where gender='M' group by gender;
select avg(salary),gender from employee_payroll group by gender;
select min(salary) from employee_payroll;
select max(salary) from employee_payroll;
select count(*) from employee_payroll;

#UC8
alter table employee_payroll add phone int after gender;
alter table employee_payroll add address varchar(250) DEFAULT 'TBD' after phone;
alter table employee_payroll add department varchar(250) NOT NULL after address;

#UC9
alter table employee_payroll change salary basic_pay double;
alter table employee_payroll add deductions double NOT NULL after basic_pay;
alter table employee_payroll add taxable_pay double NOT NULL after deductions;
alter table employee_payroll add income_tax double NOT NULL after taxable_pay;
alter table employee_payroll add net_pay double NOT NULL after income_tax;