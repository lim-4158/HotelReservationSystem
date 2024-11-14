/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import util.exceptions.GuestNotFoundException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author jamiewee
 */
@Local
public interface GuestSessionBeanLocal {
    // Retrieval Methods
    public RoomType retrieveRoomTypeByID(Long roomTypeId) throws RoomTypeNotFoundException;
    public Room retrieveRoomById(Long roomId) throws RoomNotFoundException;
    public RoomType retrieveRoomTypeByName(String typeName) throws RoomTypeNotFoundException;
    public Reservation retrieveReservationById(Long reservationID) throws ReservationNotFoundException;
    public Guest retrieveGuestByEmail(String email) throws GuestNotFoundException;
    
    // 1. Guest Login
    public Guest doLogin(String username, String password) throws GuestNotFoundException;
    // 2. Register as Guest
    public void createGuest(Guest g);
    // 3. Search Hotel Room
    public boolean doSearchHotelRoom(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, int requiredInventory);
    public int getReservationsForRoomType(RoomType roomType, LocalDate date);
    public BigDecimal calculateTotalAmountForStay(String roomTypeName, LocalDate checkInDate, LocalDate checkOutDate, int requiredRooms);
    
    // 4. Reserve Hotel Room - implement do reservation for more than one room
    public Long createReservation(Long roomTypeId, Long guestId, Reservation reservation) throws GuestNotFoundException, RoomTypeNotFoundException;  
    // 5. View My Reservation Details
    public Reservation viewReservationDetails(Long reservationID) throws ReservationNotFoundException;
    
    // 6. Display List of Reservation Records
    public List<Reservation> viewAllReservations(String email) throws GuestNotFoundException;
    
}
