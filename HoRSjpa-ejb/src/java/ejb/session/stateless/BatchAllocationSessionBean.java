/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author kevinlim
 */
@Stateless
public class BatchAllocationSessionBean implements BatchAllocationSessionBeanRemote, BatchAllocationSessionBeanLocal {

    @Override
    public void allocateRooms(LocalDate date, List<Reservation> reservations, List<Room> rooms) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
