/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jamiewee
 */
@Local
public interface SystemAdminSessionBeanLocal {
    // USE CASE 4
    public List<Employee> retrieveAllEmployees();
    
    // USE CASE 3
    public Long createNewEmployee(Employee newEmployee); 
   
    
}
