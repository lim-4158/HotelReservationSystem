/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Partner;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jamiewee
 */
@Remote
public interface SystemAdminSessionBeanRemote {
    
    public List<Employee> retrieveAllEmployees();
    
    public Long createNewEmployee(Employee newEmployee);
    
    public Long createNewPartner(Partner newPartner); 
    
    public List<Partner> retrieveAllPartners();
}
