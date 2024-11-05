/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author kevinlim
 */
@Remote
public interface BatchAllocationSessionBeanRemote {
    public void allocateRooms(LocalDate date, List<Reservation> reservations, List<Room> rooms); 
}
