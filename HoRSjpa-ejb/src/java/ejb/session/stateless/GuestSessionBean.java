/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.RoomRateTypeEnum;
import util.RoomStatusEnum;
import util.exceptions.GuestNotFoundException;
import util.exceptions.ReservationNotFoundException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author jamiewee
 */
@Stateless
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;

    // Retrieval Methods
    public RoomType retrieveRoomTypeByID(Long roomTypeId) throws RoomTypeNotFoundException {
        try {
            Query q = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.roomTypeID = :roomTypeId");
            q.setParameter("roomTypeId", roomTypeId);
            return (RoomType) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("NO ROOM TYPE FOUND");
            throw new RoomTypeNotFoundException("No room type found with ID: " + roomTypeId);
        }
    }

    public Room retrieveRoomById(Long roomId) throws RoomNotFoundException {
        try {
            Query q = em.createQuery("SELECT r FROM Room r WHERE r.roomID = :roomId");
            q.setParameter("roomId", roomId);
            return (Room) q.getSingleResult();
        } catch (NoResultException e) {
            throw new RoomNotFoundException("No room found with ID: " + roomId);
        }
    }

    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException {
        try {
            Query q = em.createQuery("SELECT g FROM Guest g WHERE g.guestID = :guestId");
            q.setParameter("guestId", guestId);
            return (Guest) q.getSingleResult();
        } catch (NoResultException e) {
            throw new GuestNotFoundException("No guest found with ID: " + guestId);
        }
    }

    public RoomType retrieveRoomTypeByName(String typeName) throws RoomTypeNotFoundException {
        try {
            Query q = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.typeName = :typeName ");
            q.setParameter("typeName", typeName);
            return (RoomType) q.getSingleResult();
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with the name: " + typeName);
        }
    }

    public Reservation retrieveReservationById(Long reservationID) throws ReservationNotFoundException {
        try {
            return em.createQuery(
                    "SELECT r FROM Reservation r WHERE r.reservationID = :reservationId", Reservation.class)
                    .setParameter("reservationId", reservationID)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new ReservationNotFoundException("Reservation with ID " + reservationID + " not found.");
        }
    }

    public Guest retrieveGuestByEmail(String email) throws GuestNotFoundException {
        try {
            return em.createQuery("SELECT g FROM Guest g WHERE g.email = :email", Guest.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new GuestNotFoundException("Guest not found"); // Guest not found
        }
    }

    // 1. Guest Login
    public Guest doLogin(String username, String password) throws GuestNotFoundException {
        TypedQuery<Guest> query = em.createQuery("SELECT g FROM Guest g WHERE g.username = :username", Guest.class);
        query.setParameter("username", username);

        Guest guest;
        try {
            guest = query.getSingleResult();
        } catch (Exception e) {
            throw new GuestNotFoundException("Username not found.");
        }

        if (guest.getPassword().equals(password)) {
            return guest;
        } else {
            throw new GuestNotFoundException("Invalid password.");
        }
    }

    // 2. Register as Guest
    public void createGuest(Guest g) {
        em.persist(g);
        em.flush();
    }

    // 3. Search Hotel Room
    public boolean doSearchHotelRoom(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate,
            int requiredInventory) {
        LocalDate currentDate = checkInDate;

        int totalInventory = (int) roomType.getRooms().stream()
                .filter(room -> room.getRoomStatus() != RoomStatusEnum.DISABLED)
                .count();

        while (currentDate.isBefore(checkOutDate) || currentDate.equals(checkOutDate)) { // Exclude checkOutDate
            int bookedRoomsCount = getReservationsForRoomType(roomType, currentDate);

            // Calculate available rooms for the current day based on roomType's inventory
            int availableRooms = totalInventory - bookedRoomsCount;

            // Check if the available rooms meet the required inventory for the current day
            if (availableRooms < requiredInventory) {
                return false; // Not enough rooms available for at least one day
            }

            // Move to the next day in the range
            currentDate = currentDate.plusDays(1);
        }

        // Sufficient rooms are available for each day in the date range
        return true;
    }
    // take into account start and end date of peak + promo rates -> check how they
    // apply; at this date is there peak / promo

    public int getReservationsForRoomType(RoomType roomType, LocalDate date) {
        Query query = em.createQuery(
            "SELECT SUM(res.numberOfRooms) " +
            "FROM Reservation res " +
            "WHERE res.roomType = :roomType " +
            "AND :date > res.checkInDate " +
            "AND :date < res.checkOutDate"
        );
        query.setParameter("roomType", roomType);
        query.setParameter("date", date);

        Long count = (Long) query.getSingleResult();
        return count.intValue();
    }

    @Override
    public BigDecimal calculateTotalAmountForStay(String roomTypeName, LocalDate checkInDate, LocalDate checkOutDate,
            int requiredRooms) throws RoomTypeNotFoundException {
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Loop from checkInDate to the day before checkOutDate
        LocalDate currentDate = checkInDate;
        Long roomTypeId = retrieveRoomTypeByName(roomTypeName).getRoomTypeID();
        while (currentDate.isBefore(checkOutDate)) {
            BigDecimal dailyRate = getApplicableRateForDate(roomTypeId, currentDate);

            // Multiply daily rate by the required number of rooms for each night
            totalAmount = totalAmount.add(dailyRate.multiply(BigDecimal.valueOf(requiredRooms)));

            // Move to the next night
            currentDate = currentDate.plusDays(1);
        }

        return totalAmount;
    }

    // Helper method to determine the applicable rate for a specific date
    private BigDecimal getApplicableRateForDate(Long roomTypeId, LocalDate date) {
        // Query for promotion rate

        Query promotionRateQuery = em.createQuery(
                "SELECT rr.nightlyRateAmount FROM RoomRate rr "
                        + "WHERE rr.rateType = :promotionRateType "
                        + "AND rr.roomType.roomTypeID = :roomTypeId "
                        + "AND :date BETWEEN rr.startDate AND rr.endDate");
        promotionRateQuery.setParameter("promotionRateType", RoomRateTypeEnum.PROMOTION);
        promotionRateQuery.setParameter("roomTypeId", roomTypeId);
        promotionRateQuery.setParameter("date", date);

        // Query for peak rate
        Query peakRateQuery = em.createQuery(
                "SELECT rr.nightlyRateAmount FROM RoomRate rr "
                        + "WHERE rr.rateType = :peakRateType "
                        + "AND rr.roomType.roomTypeID = :roomTypeId "
                        + "AND :date BETWEEN rr.startDate AND rr.endDate");
        peakRateQuery.setParameter("peakRateType", RoomRateTypeEnum.PEAK);
        peakRateQuery.setParameter("roomTypeId", roomTypeId);
        peakRateQuery.setParameter("date", date);

        // Query for normal rate
        Query normalRateQuery = em.createQuery(
                "SELECT rr.nightlyRateAmount FROM RoomRate rr "
                        + "WHERE rr.rateType = :normalRateType "
                        + "AND rr.roomType.roomTypeID = :roomTypeId ");
        normalRateQuery.setParameter("normalRateType", RoomRateTypeEnum.NORMAL);
        normalRateQuery.setParameter("roomTypeId", roomTypeId);

        // promotion > peak > normal
        // Check if promotion rate is defined
        try {
            return (BigDecimal) promotionRateQuery.getSingleResult();
        } catch (NoResultException e) {
            // Promotion rate not found for this date
        }

        // Check if peak rate is defined if promotion rate not available
        try {
            return (BigDecimal) peakRateQuery.getSingleResult();
        } catch (NoResultException e) {
            // Peak rate not found for this date
        }

        // Fallback to normal rate if neither promotion nor peak rate is available
        return (BigDecimal) normalRateQuery.getSingleResult();
    }

    // 4. Reserve Hotel Room - implement do reservation for more than one room
    @Override
    public Long createReservation(Long roomTypeId, Long guestId, Reservation reservation)
            throws GuestNotFoundException, RoomTypeNotFoundException {

        RoomType rt = retrieveRoomTypeByID(roomTypeId);
        Guest guest = retrieveGuestById(guestId);

        reservation.setGuest(guest);

        guest.getReservations().size();

        // Step 2: Add the reservation to the guest's reservations list
        guest.getReservations().add(reservation);
        rt.getReservations().add(reservation);

        // Step 3: Persist the reservation
        em.persist(reservation);

        // Step 4: Flush to generate the ID and return it

        em.flush();
        return reservation.getReservationID();

    }

    // 5. View My Reservation Details
    public Reservation viewReservationDetails(Long reservationID) throws ReservationNotFoundException {

        try {
            // Assuming we have a method to fetch reservation by ID
            Reservation reservation = retrieveReservationById(reservationID);
            if (reservation == null) {
                throw new IllegalArgumentException("Reservation not found with ID: " + reservationID);
            }
            return reservation;
        } catch (NoResultException e) {
            throw new ReservationNotFoundException("Reservation not found.");
        }

    }

    // 6. Display List of Reservation Records
    public List<Reservation> viewAllReservations(String email) throws GuestNotFoundException {
        TypedQuery<Guest> query = em.createQuery(
                "SELECT g FROM Guest g WHERE g.email = :email", Guest.class);
        query.setParameter("email", email);

        List<Guest> guests = query.getResultList();

        if (guests.isEmpty()) {
            throw new GuestNotFoundException("Guest not found with email: " + email);
        } else if (guests.size() > 1) {
            throw new IllegalStateException("Data integrity issue: Multiple guests found with the same unique email.");
        }

        Guest guest = guests.get(0);
        // Initialize the reservations to avoid lazy loading outside the session
        guest.getReservations().size(); // Accessing size() will trigger the lazy load

        return guest.getReservations();
    }

}
