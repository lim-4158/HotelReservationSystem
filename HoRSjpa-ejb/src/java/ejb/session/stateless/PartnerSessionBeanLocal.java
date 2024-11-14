/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kevinlim
 */
@Local
public interface PartnerSessionBeanLocal {
 
    public Partner partnerLogIn(String username, String password);
            
    public List<Reservation> viewAllReservations(Long partnerId); 
    
    public Reservation viewReservation(Long reservationId); 
    
    public BigDecimal calculateTotalAmountForStay(String roomTypeName, String checkInDate, String checkOutDate, int requiredRooms);

    public Guest findGuestByEmail(String email);
    
    public List<RoomType> searchAvailableRooms(String checkInDate, String checkOutDate, int requiredInventory); 
    
    public void reserveRoom(
                    String bookingDate,
                    String checkInDate,
                    String checkOutDate,
                    BigDecimal totalAmount,
                    int requiredRooms,
                    Guest guest,
                    RoomType selectedRoomType,
                    Partner partner
            );
    
    
}
