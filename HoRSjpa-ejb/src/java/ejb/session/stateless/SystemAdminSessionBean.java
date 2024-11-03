/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jamiewee
 */
@Stateless
public class SystemAdminSessionBean implements SystemAdminSessionBeanRemote, SystemAdminSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;
    
    
    // USE CASE 4
    @Override 
    public List<Employee> retrieveAllEmployees() {
        return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }
    
    // USE CASE 3
    @Override
    public Long createNewEmployee(Employee newEmployee) // throws EntityManagerException 
    {
        em.persist(newEmployee);
        em.flush();
        
        return newEmployee.getEmployeeID();
    }
        // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Long createNewPartner(Partner newPartner) {
        em.persist(newPartner);
        em.flush();
        
        return newPartner.getPartnerID(); 
    }

    @Override
    public List<Partner> retrieveAllPartners() {
        return em.createQuery("SELECT p FROM Partner p", Partner.class).getResultList();
    }

}
