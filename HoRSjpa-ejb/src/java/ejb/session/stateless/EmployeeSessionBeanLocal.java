/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.InvalidLoginException;

/**
 *
 * @author jamiewee
 */
@Local
public interface EmployeeSessionBeanLocal {

    public Employee employeeLogin(String username, String password) throws InvalidLoginException;
    
}
