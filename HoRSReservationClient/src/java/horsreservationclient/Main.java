/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsreservationclient;

import ejb.session.stateless.GuestRelationOfficerSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.SalesManagerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author kevinlim
 */
public class Main {

    @EJB
    private static GuestRelationOfficerSessionBeanRemote guestRelationOfficerSessionBeanRemote;

    @EJB
    private static SalesManagerSessionBeanRemote salesManagerSessionBeanRemote;

    @EJB
    private static GuestSessionBeanRemote guestSessionBeanRemote;
    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(guestSessionBeanRemote, salesManagerSessionBeanRemote, guestRelationOfficerSessionBeanRemote);
        mainApp.runApp();
    }
    
}
