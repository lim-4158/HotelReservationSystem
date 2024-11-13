/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jamiewee
 */
@Local
public interface GuestRelationOfficerSessionBeanLocal {
    
    // Retrieval Methods
    public Guest findGuestByEmail(String email);
    public List<Reservation> findReservationsByGuest(Long guestId);
    public List<RoomType> getAllRoomTypes();
    
    // 1. Walk-in Search Room
    public List<RoomType> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int requiredInventory);
    public BigDecimal calculateTotalAmountForStay(String roomTypeName, LocalDate checkInDate, LocalDate checkOutDate, int numberOfRooms);
    public int getReservationsForRoomType(RoomType roomType, LocalDate date);
    public boolean isRoomTypeAvailable(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, int requiredInventory, int totalInventory);
    
    // 2. Walk-in Reserve Room
    public Long createReservation(Reservation r);

    // 3. & 4. CheckIn and CheckOut Guests
    public void checkOutGuest(Long reservationID, LocalDate date);
    public List<String> checkInGuest(Long reservationID, LocalDate checkInDate);
    
}
