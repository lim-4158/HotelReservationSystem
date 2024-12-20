/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.InvalidLoginException;

/**
 *
 * @author jamiewee
 */
@Remote
public interface EmployeeSessionBeanRemote {
    
        public Employee employeeLogin(String username, String password) throws InvalidLoginException;
    
}
