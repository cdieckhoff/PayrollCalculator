
/**
 *
 * Filename:                Payroll3.java
 * Description:             Updated application that takes user entered
 *                          data: hourly rate; hours worked; and computes the
 *                          employee's pay at a fixed 11% federal tax rate.
 * Author Name:             Chris Dieckhoff
 * Date:                    Jun 19, 2014
 */

import java.util.Scanner;

public class Payroll3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String sEmployeeName;       // holds value of entered employee name
        double dHourlyRate;         // holds value of employee's hourly rate
        double dTotalHoursWorked;   // holds value of employee's hours worked
        Employee employee;          // class to calculate employee information

        Scanner input = new Scanner(System.in);

        while(true){

            // Tell user how to exit
            System.out.println("Enter quit as the employee's name to exit.");

            //Start getting user input
            // Get employee's name
            do{
                System.out.println("Enter the employee's name.");
                sEmployeeName = input.nextLine();
                if(sEmployeeName.equals("")){
                    System.out.println("Invalid input. Employee's name can not be null.");
                }
            }while(sEmployeeName.equals(""));

            // Check the name for quit
            if(sEmployeeName.equalsIgnoreCase("quit")){
                break;
            }

            // Get employee's hourlyRate
            do{
                System.out.println("Enter the hourly wage for " + sEmployeeName);
                dHourlyRate = input.nextDouble();
                if(!(dHourlyRate > 0.0)){
                    System.out.println("Invalid input. Enter a postive amount.");
                }
            }while(!(dHourlyRate > 0.0));

            // Get employee's total hours worked
            do{
                System.out.println("Enter the total number of hours worked for " + sEmployeeName);
                dTotalHoursWorked = input.nextDouble();
                if(!(dTotalHoursWorked > 0.0)){
                    System.out.println("Invalid input. Enter a positive amount.");
                }
            }while(!(dTotalHoursWorked > 0.0));

            // OK, we have all the information for the employee that we need
            employee = new Employee(sEmployeeName, dHourlyRate, dTotalHoursWorked);
            System.out.println(Employee.getPayInforation(employee));
        }
    }

}

class Employee{
    private String name;                // variable for Name property
    private double hourlyRate;          // variable for HourlyRate property
    private double regularHoursWorked;  // variable for RegularHoursWorked property
    private double overtimeHoursWorked; // variable for OvertimeHoursWorked property
    protected boolean hasOvertimeHours; // variable for knowing if employee has overtime

    // Employee constructor
    public Employee(String sName, double dHourlyRate, double dTotalHoursWorked){
        name = sName;
        hourlyRate = dHourlyRate;
        if(dTotalHoursWorked > 40.0){
           overtimeHoursWorked = (dTotalHoursWorked - 40.0);
           regularHoursWorked = 40.0;
           hasOvertimeHours = true;
        }else{
        regularHoursWorked = dTotalHoursWorked;
        overtimeHoursWorked = 0.0;
        hasOvertimeHours = false;
        }
    }

    //Properties
    public String getName(){
        return name;
    }

    public void setName(String sName){
        name = sName;
    }

    public double getHourlyRate(){
        return hourlyRate;
    }

    public void setHourlyRate(double dHourlyRate){
        hourlyRate = dHourlyRate;
    }

    public double getRegularHoursWorked(){
        return regularHoursWorked;
    }

    public void setRegularHoursWorked(double dRegularHoursWorked){
        regularHoursWorked = dRegularHoursWorked;
    }

    public double getOvertimeHoursWorked(){
        return overtimeHoursWorked;
    }

    public void setOvertimeHoursWorked(double dOvertimeHoursWorked){
        overtimeHoursWorked = dOvertimeHoursWorked;
    }

    //Methods
    /**
     * @example double otPay = calculateOvertimePay();
     * @description calculates gross pay of employee
     * @return double (hours * OTPay)
     */
    public double calculateOvertimePay(){
        double dOTPay = (1.5 * getHourlyRate());
        return (overtimeHoursWorked * dOTPay);
    }

    /**
     * @example double grossPay = calculateGrossPay();
     * @description calculates the employee's gross pay
     * @return double (reg * OT)
     */
    public double calculateGrossPay(){
        // Gross pay = (regGross + OTGross)
        double regGross = (getHourlyRate() * getRegularHoursWorked());
        double OTGross = calculateOvertimePay();
        return (regGross + OTGross);
    }

    /**
     * @example double fedTax = calculateFederalTaxes();
     * @description calculates 11% of pay
     * @return  double (0.11 * grossPay)
     */
    public double calculateFederalTaxes(){
        return (0.11 * calculateGrossPay());
    }

    /**
     * @example boolean ot = hasOvertime();
     * @description determines whether the employee has overtime
     * @return boolean
     */
    public boolean hasOvertime(){
        return this.hasOvertimeHours;
    }

    /**
     * @example String payInfo = Employee.getPayInformation(new Employee("Hal", 10.00, 42.0);
     * @description converts all data contained within the class supplied to
     *              a readable string output
     * @param employee
     * @return string
     */
    public static String getPayInforation(Employee employee){
        StringBuilder sDataOutput = new StringBuilder();
        sDataOutput.append(String.format("Employee Name: %s\n", employee.getName()));
        sDataOutput.append(String.format("Hourly Rate: $%.2f\n", employee.getHourlyRate()));
        if(employee.hasOvertime()){
            sDataOutput.append(String.format("Overtime Hourly Rate: $%.2f\n",
                   (employee.getHourlyRate() * 1.5)));
            sDataOutput.append(String.format("Regular Hours Worked: %.2f\n", employee.getRegularHoursWorked()));
            sDataOutput.append(String.format("Overtime Hours Worked: %.2f\n", employee.getOvertimeHoursWorked()));
            sDataOutput.append(String.format("Regular Pay: $%.2f\n",
                    (employee.getHourlyRate() * employee.getRegularHoursWorked())));
            sDataOutput.append(String.format("Overtime Pay: $%.2f\n", employee.calculateOvertimePay()));

        }
        sDataOutput.append(String.format("Total Hours Worked: %.2f\n",
                (employee.getRegularHoursWorked() + employee.getOvertimeHoursWorked())));
        sDataOutput.append(String.format("\nGross Pay: $%.2f\n", employee.calculateGrossPay()));
        sDataOutput.append(String.format("Federal Tax (at 11%%): $%.2f\n", employee.calculateFederalTaxes()));
        sDataOutput.append(String.format("Net Pay: $%.2f\n",
                (employee.calculateGrossPay() - employee.calculateFederalTaxes())));
        return sDataOutput.toString();
    }
}