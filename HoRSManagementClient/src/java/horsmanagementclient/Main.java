/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.BatchAllocationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestRelationOfficerSessionBeanRemote;
import ejb.session.stateless.OperationManagerSessionBeanRemote;
import ejb.session.stateless.SalesManagerSessionBeanRemote;
import ejb.session.stateless.SystemAdminSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author kevinlim
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    @EJB 
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote; 
    @EJB 
    private static GuestRelationOfficerSessionBeanRemote guestRelationOfficerSessionBeanRemote;
    @EJB 
    private static OperationManagerSessionBeanRemote operationManagerSessionBeanRemote;
    @EJB 
    private static SalesManagerSessionBeanRemote salesManagerSessionBeanRemote;
    @EJB 
    private static SystemAdminSessionBeanRemote systemAdminSessionBeanRemote;
    @EJB
    private static BatchAllocationSessionBeanRemote batchAllocationSessionBeanRemote;

    public static void main(String[] args)
    {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, guestRelationOfficerSessionBeanRemote, operationManagerSessionBeanRemote, salesManagerSessionBeanRemote, systemAdminSessionBeanRemote, batchAllocationSessionBeanRemote);
        mainApp.runApp();   
    }
}
    

