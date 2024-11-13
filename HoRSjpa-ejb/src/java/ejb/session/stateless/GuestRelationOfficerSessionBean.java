/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import entity.RoomReservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.RoomStatusEnum;
import util.RoomTypeStatusEnum;

/**
 *
 * @author jamiewee
 */
@Stateless
public class GuestRelationOfficerSessionBean implements GuestRelationOfficerSessionBeanRemote, GuestRelationOfficerSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createReservation(Reservation r) {
        em.persist(r);
        em.flush();
        return r.getReservationID();
    }
    // for every single day from check in date to check out date non inclusive
    // search for if there is a common room type across all days available
    
    @Override
    public List<Reservation> findReservationsByGuest(Long guestId) {
        Query query = em.createQuery(
            "SELECT r FROM Reservation r WHERE r.guest.guestID = :guestId", Reservation.class
        );
        query.setParameter("guestId", guestId);
        return query.getResultList();
    }
    
    @Override
    public Guest findGuestByEmail(String email) {
        try {
            Query query = em.createQuery(
                "SELECT g FROM Guest g WHERE g.email = :email", Guest.class
            );
            query.setParameter("email", email);
            return (Guest) query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Return null if no guest is found
        }
    }
    
    @Override
    public List<RoomType> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int requiredInventory) {
        List<RoomType> allRoomTypes = getAllRoomTypes();
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
            
            if (isRoomTypeAvailable(roomType, checkInDate, checkOutDate, requiredInventory, totalInventory)) {
                availableRoomTypes.add(roomType);
            }
        }

        return availableRoomTypes;
    }

    @Override
    public boolean isRoomTypeAvailable(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, int requiredInventory, int totalInventory) {
        LocalDate currentDate = checkInDate;

        while (currentDate.isBefore(checkOutDate)) {
            int bookedRoomsCount = getReservationsForRoomType(roomType, currentDate);
            int availableRooms = totalInventory - bookedRoomsCount;

            if (availableRooms < requiredInventory) {
                return false;
            }

            currentDate = currentDate.plusDays(1);
        }

        return true;
    }
    
    @Override
    public int getReservationsForRoomType(RoomType roomType, LocalDate date) {
        Query query = em.createQuery(
            "SELECT SUM(res.numberOfRooms) " +
            "FROM Reservation res " +
            "WHERE res.roomType = :roomType " +
            "AND :date BETWEEN res.checkInDate AND res.checkOutDate"
        );
        query.setParameter("roomType", roomType);
        query.setParameter("date", date);

        Long totalRooms = (Long) query.getSingleResult();
        return (totalRooms != null) ? totalRooms.intValue() : 0;
    }

    @Override
    public BigDecimal calculateTotalAmountForStay(String roomTypeName, LocalDate checkInDate, LocalDate checkOutDate, int numberOfRooms) {
        Query query = em.createQuery(
            "SELECT rr.nightlyRateAmount " +
            "FROM RoomRate rr " +
            "WHERE rr.roomRateName = CONCAT(:roomTypeName, ' Published')"
        );
        query.setParameter("roomTypeName", roomTypeName);

        BigDecimal nightlyRateAmount = (BigDecimal) query.getSingleResult();

        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nightlyRateAmount
            .multiply(BigDecimal.valueOf(numberOfNights))
            .multiply(BigDecimal.valueOf(numberOfRooms));
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return em.createQuery("SELECT rt FROM RoomType rt", RoomType.class).getResultList();
    }
    
    @Override
    public void checkOutGuest(Long reservationID, LocalDate date) {
        // Step 1: Retrieve the reservation by its ID
        Reservation reservation = em.find(Reservation.class, reservationID);

        if (reservation == null) {
            System.out.println("Reservation with ID " + reservationID + " not found.");
            return;
        }

        // Step 2: Update the check-out date
        reservation.setCheckOutDate(date);

        // Step 3: Persist the updated reservation
        em.merge(reservation);

        System.out.println("Guest checked out successfully. Check-out date set to " + date);
        
    }
    
    @Override
    public List<String> checkInGuest(Long reservationID, LocalDate checkInDate) {
        // Step 1: Retrieve the reservation by its ID
        Reservation reservation = em.find(Reservation.class, reservationID);

        if (reservation == null) {
            System.out.println("Reservation with ID " + reservationID + " not found.");
            return Collections.emptyList(); // Return an empty list if the reservation is not found
        }

        // Step 2: Check if the check-in date matches the reservation's check-in date
        if (!reservation.getCheckInDate().equals(checkInDate)) {
            System.out.println("The check-in date does not match the reservation's check-in date.");
            return Collections.emptyList(); // Return an empty list if the dates do not match
        }

        // Step 3: Retrieve all room reservations associated with this reservation
        Query query = em.createQuery(
            "SELECT rr FROM RoomReservation rr WHERE rr.reservation.reservationID = :reservationID",
            RoomReservation.class
        );
        query.setParameter("reservationID", reservationID);

        List<RoomReservation> roomReservations = query.getResultList();

        // Step 4: Collect the room numbers for each room reservation
        List<String> roomNumbers = new ArrayList<>();
        for (RoomReservation roomReservation : roomReservations) {
            roomNumbers.add(roomReservation.getRoom().getRoomNumber());
        }

        // Step 5: Print confirmation and return the list of room numbers
        System.out.println("Guest checked in successfully. Room numbers: " + roomNumbers);
        return roomNumbers;
    }



    
}
