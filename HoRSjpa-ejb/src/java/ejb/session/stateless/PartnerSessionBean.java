/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.ReservationTypeEnum;
import util.RoomStatusEnum;
import util.RoomTypeStatusEnum;

/**
 *
 * @author kevinlim
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;

    @EJB 
    private GuestRelationOfficerSessionBeanLocal guestRelationOfficerSessionBeanLocal; 
    
    // i dont think i should return null
    @Override
    public Partner partnerLogIn(String username, String password) {
        List<Partner> partners = em.createQuery("SELECT p FROM Partner p").getResultList(); 
        for (Partner p : partners) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                System.out.println("-----------------------------------returned a partner");               
                return p;
            }
        }
        System.out.println("--------------------------did not return a partner");
        return null;
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<Reservation> viewAllReservations(Long partnerId) {
        return em.createQuery(
            "SELECT r FROM Reservation r WHERE r.partner.partnerID = :partnerId", Reservation.class)
                .setParameter("partnerId", partnerId)
                .getResultList();
    }

    @Override
    public Reservation viewReservation(Long reservationId) {
        return em.find(Reservation.class, reservationId);
    }


    @Override
    public BigDecimal calculateTotalAmountForStay(String roomTypeName, String checkInDate, String checkOutDate, int requiredRooms) {
        LocalDate checkInDateLD = convertToLocalDate(checkInDate); 
        LocalDate checkOutDateLD = convertToLocalDate(checkOutDate); 
        return guestRelationOfficerSessionBeanLocal.calculateTotalAmountForStay(roomTypeName, checkInDateLD, checkOutDateLD, requiredRooms); 
    }

    @Override
    public Guest findGuestByEmail(String email) {
        List<Guest> guests = em.createQuery("SELECT g FROM Guest g WHERE g.email = :email", Guest.class)
                    .setParameter("email", email).getResultList(); 
        if (guests.isEmpty()) {
            return null;
        } else {
            return guests.get(0);
        }    
    }

    @Override
    public List<RoomType> searchAvailableRooms(String checkInDate, String checkOutDate, int requiredInventory) {
        System.out.println("searchAvailableRooms PartnerSessionBean triggered");
        LocalDate checkInDateLD = convertToLocalDate(checkInDate); 
        LocalDate checkOutDateLD = convertToLocalDate(checkOutDate); 
        List<RoomType> allRoomTypes = guestRelationOfficerSessionBeanLocal.getAllRoomTypes();
        List<RoomType> availableRoomTypes = new ArrayList<>();

        for (RoomType roomType : allRoomTypes) {
            // Skip this room type if it is disabled
            if (roomType.getRoomTypeStatus() == RoomTypeStatusEnum.DISABLED) {
                continue;
            }

            int totalInventory = (int) roomType.getRooms().stream()
                .filter(room -> room.getRoomStatus() != RoomStatusEnum.DISABLED)
                .count();

            System.out.println("REACHED HERE! ----------------");
            System.out.println("total inventory is " + totalInventory);
            
            if (guestRelationOfficerSessionBeanLocal.isRoomTypeAvailable(roomType, checkInDateLD, checkOutDateLD, requiredInventory, totalInventory)) {
                availableRoomTypes.add(roomType);
            }
        }
        
        System.out.println("fucking hell");

        return availableRoomTypes;
    }

    @Override
    public void reserveRoom(String bookingDate, String checkInDate, String checkOutDate, BigDecimal totalAmount, int requiredRooms, Guest guest, RoomType selectedRoomType, Partner partner) {
        LocalDate checkInDateLD = convertToLocalDate(checkInDate); 
        LocalDate checkOutDateLD = convertToLocalDate(checkOutDate); 
        LocalDate bookingDateLD = convertToLocalDate(bookingDate); 
        Reservation reservation = new Reservation(bookingDateLD, checkInDateLD, checkOutDateLD, totalAmount, ReservationTypeEnum.PARTNER, requiredRooms, partner, selectedRoomType, guest); 
        em.persist(reservation);

        // is partner already persisted? or should i em.persist()
        // retrieve partner reservation list and add to it 
        List<Reservation> partnerReservations = partner.getReservations(); 
        partnerReservations.add(reservation); 
        partner.setReservations(partnerReservations);
        
        // check if guest currently exist in database, if not, persist entity 
        // add reservation to reservation list
        List<Guest> guests = em.createQuery("SELECT g FROM Guest g").getResultList(); 
        boolean isExistingGuest = false;
        for (Guest g : guests) {
            if (g.getLastName().equals(guest.getLastName()) && g.getEmail().equals(guest.getEmail())) {
                isExistingGuest = true;
                //do i need persist here?
                guest.getReservations().add(reservation);
            }
        }
        if (!isExistingGuest) {
            em.persist(guest);
            em.flush();
            guest.getReservations().add(reservation);
        }
        
    }
    
    public static LocalDate convertToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

 

   
    
}
