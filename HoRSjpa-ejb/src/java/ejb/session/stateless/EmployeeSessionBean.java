/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exceptions.InvalidLoginException;

/**
 *
 * @author jamiewee
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;
    @EJB
    private SystemAdminSessionBeanLocal systemAdminSessionBeanLocal;
    // USE CASE 1
    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginException
    {
        List<Employee> employees = systemAdminSessionBeanLocal.retrieveAllEmployees();
        
        for(Employee employee:employees)
        {
            if(employee.getUsername().equals(username) && employee.getPassword().equals(password))
            {
                return employee;
            }
        }
        
        throw new InvalidLoginException("Invalid login credentials. Please check your username and password.");
    }
}
