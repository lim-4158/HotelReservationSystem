/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import entity.Employee;
import entity.ExceptionReport;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomReservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import util.EmployeeRoleEnum;
import util.RoomRateTypeEnum;
import util.RoomStatusEnum;
import util.RoomTypeStatusEnum;

/**
 * Singleton Session Bean responsible for initializing sample data into the database.
 * This bean runs once at application startup and populates the database with
 * predefined entities for testing and demonstration purposes.
 * 
 * @author
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;

    /**
     * Method executed after the bean's construction.
     * Initializes the database with sample data if not already present.
     */
    @PostConstruct
    public void postConstruct() {
        // Check if data is already initialized by verifying the presence of at least one Employee
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class);
        Long count = query.getSingleResult();

        if (count == 0) {
            System.out.println("Initializing database with sample data...");

            // ---------- Initialize Room Types ----------
            RoomType singleRoomType = new RoomType();
            singleRoomType.setTypeName("Single");
            singleRoomType.setDescription("Single bed room with basic amenities.");
            singleRoomType.setSize(new BigDecimal("250.00")); // in sq ft
            singleRoomType.setBed("Single Bed");
            singleRoomType.setCapacity(1L);
            singleRoomType.setAmenities("WiFi, TV, Mini Bar");
            singleRoomType.setRoomTypeStatus(RoomTypeStatusEnum.ENEABLED);
            singleRoomType.setTierNumber(1);
            singleRoomType.setInventory(50L);
            em.persist(singleRoomType);

            RoomType doubleRoomType = new RoomType();
            doubleRoomType.setTypeName("Double");
            doubleRoomType.setDescription("Double bed room with enhanced amenities.");
            doubleRoomType.setSize(new BigDecimal("350.00"));
            doubleRoomType.setBed("Double Bed");
            doubleRoomType.setCapacity(2L);
            doubleRoomType.setAmenities("WiFi, TV, Mini Bar, Coffee Maker");
            doubleRoomType.setRoomTypeStatus(RoomTypeStatusEnum.ENEABLED);
            doubleRoomType.setTierNumber(2);
            doubleRoomType.setInventory(30L);
            em.persist(doubleRoomType);

            // Additional RoomTypes can be added here (up to 10)
            RoomType deluxeRoomType = new RoomType();
            deluxeRoomType.setTypeName("Deluxe");
            deluxeRoomType.setDescription("Deluxe room with premium amenities and larger space.");
            deluxeRoomType.setSize(new BigDecimal("500.00"));
            deluxeRoomType.setBed("King Bed");
            deluxeRoomType.setCapacity(3L);
            deluxeRoomType.setAmenities("WiFi, TV, Mini Bar, Coffee Maker, Jacuzzi");
            deluxeRoomType.setRoomTypeStatus(RoomTypeStatusEnum.ENEABLED);
            deluxeRoomType.setTierNumber(3);
            deluxeRoomType.setInventory(20L);
            em.persist(deluxeRoomType);

            // ---------- Initialize Room Rates ----------
            RoomRate standardRateSingle = new RoomRate();
            standardRateSingle.setRoomRateName("Standard Rate");
            standardRateSingle.setRateType(RoomRateTypeEnum.PUBLISHED);
            standardRateSingle.setNightlyRateAmount(new BigDecimal("150.00"));
            standardRateSingle.setStartDate(LocalDate.of(2024, 1, 1));
            standardRateSingle.setEndDate(LocalDate.of(2024, 12, 31));
            standardRateSingle.setRoomType(singleRoomType);
            em.persist(standardRateSingle);
            
            RoomRate standardRateDouble = new RoomRate();
            standardRateDouble.setRoomRateName("Standard Rate");
            standardRateDouble.setRateType(RoomRateTypeEnum.PUBLISHED);
            standardRateDouble.setNightlyRateAmount(new BigDecimal("300.00"));
            standardRateDouble.setStartDate(LocalDate.of(2024, 1, 1));
            standardRateDouble.setEndDate(LocalDate.of(2024, 12, 31));
            standardRateDouble.setRoomType(doubleRoomType);
            em.persist(standardRateDouble);

            RoomRate premiumRateDouble = new RoomRate();
            premiumRateDouble.setRoomRateName("Premium Rate");
            premiumRateDouble.setRateType(RoomRateTypeEnum.PROMOTION);
            premiumRateDouble.setNightlyRateAmount(new BigDecimal("250.00"));
            premiumRateDouble.setStartDate(LocalDate.of(2024, 1, 1));
            premiumRateDouble.setEndDate(LocalDate.of(2024, 12, 31));
            premiumRateDouble.setRoomType(doubleRoomType);
            em.persist(premiumRateDouble);
            
            RoomRate premiumRateSingle = new RoomRate();
            premiumRateSingle.setRoomRateName("Premium Rate");
            premiumRateSingle.setRateType(RoomRateTypeEnum.PROMOTION);
            premiumRateSingle.setNightlyRateAmount(new BigDecimal("250.00"));
            premiumRateSingle.setStartDate(LocalDate.of(2024, 1, 1));
            premiumRateSingle.setEndDate(LocalDate.of(2024, 12, 31));
            premiumRateSingle.setRoomType(doubleRoomType);
            em.persist(premiumRateSingle);

            // ---------- Initialize Rooms ----------
            Room room101 = new Room();
            room101.setRoomNumber("101");
            room101.setRoomStatus(RoomStatusEnum.AVAILABLE);
            room101.setRoomType(singleRoomType);
            em.persist(room101);

            Room room102 = new Room();
            room102.setRoomNumber("102");
            room102.setRoomStatus(RoomStatusEnum.AVAILABLE);
            room102.setRoomType(doubleRoomType);
            em.persist(room102);

            Room room103 = new Room();
            room103.setRoomNumber("103");
            room103.setRoomStatus(RoomStatusEnum.UNAVAILABLE);
            room103.setRoomType(deluxeRoomType);
            em.persist(room103);

            // Additional Rooms can be added here (up to 10)
            Room room104 = new Room();
            room104.setRoomNumber("104");
            room104.setRoomStatus(RoomStatusEnum.AVAILABLE);
            room104.setRoomType(deluxeRoomType);
            em.persist(room104);

            // ---------- Initialize Guests ----------
            Guest guest1 = new Guest();
            guest1.setFirstName("John");
            guest1.setLastName("Doe");
            guest1.setUsername("johndoe");
            guest1.setPassword("password123"); // In production, ensure passwords are hashed
            guest1.setEmail("johndoe@example.com");
            guest1.setPhoneNumber("12345678");
            guest1.setPassportNumber("A1234567");
            em.persist(guest1);

            Guest guest2 = new Guest();
            guest2.setFirstName("Jane");
            guest2.setLastName("Smith");
            guest2.setUsername("janesmith");
            guest2.setPassword("password456");
            guest2.setEmail("janesmith@example.com");
            guest2.setPhoneNumber("87654321");
            guest2.setPassportNumber("B7654321");
            em.persist(guest2);

            // Additional Guests can be added here (up to 10)
            Guest guest3 = new Guest();
            guest3.setFirstName("Alice");
            guest3.setLastName("Johnson");
            guest3.setUsername("alicejohnson");
            guest3.setPassword("alicepass");
            guest3.setEmail("alicejohnson@example.com");
            guest3.setPhoneNumber("11223344");
            guest3.setPassportNumber("C9876543");
            em.persist(guest3);

            // ---------- Initialize Partners ----------
            Partner partner1 = new Partner();
            partner1.setPartnerName("Travel Agency A");
            partner1.setUsername("travela");
            partner1.setPassword("travelpassA"); // In production, ensure passwords are hashed
            em.persist(partner1);

            Partner partner2 = new Partner();
            partner2.setPartnerName("Travel Agency B");
            partner2.setUsername("travelb");
            partner2.setPassword("travelpassB");
            em.persist(partner2);

            // Additional Partners can be added here (up to 10)
            Partner partner3 = new Partner();
            partner3.setPartnerName("Travel Agency C");
            partner3.setUsername("travelc");
            partner3.setPassword("travelpassC");
            em.persist(partner3);

            // ---------- Initialize Employees ----------
            Employee employee1 = new Employee();
            employee1.setUsername("markjohnson");
            employee1.setPassword("emp1234"); // In production, ensure passwords are hashed
            employee1.setRole(EmployeeRoleEnum.SYSTEMADMIN);
            em.persist(employee1);

            Employee employee2 = new Employee();
            employee2.setUsername("sarahlee");
            employee2.setPassword("emp5678");
            employee2.setRole(EmployeeRoleEnum.OPERATIONSMANAGER);
            em.persist(employee2);

            // Additional Employees can be added here (up to 10)
            Employee employee3 = new Employee();
            employee3.setUsername("tomwilson");
            employee3.setPassword("emp9012");
            employee3.setRole(EmployeeRoleEnum.SALESMANAGER);
            em.persist(employee3);

            Employee employee4 = new Employee();
            employee4.setUsername("lindabrown");
            employee4.setPassword("emp3456");
            employee4.setRole(EmployeeRoleEnum.GUESTRELATIONOFFIER);
            em.persist(employee4);

            // ---------- Initialize Reservations ----------
            Reservation reservation1 = new Reservation();
            reservation1.setReservationDate(LocalDate.now());
            reservation1.setCheckInDate(LocalDate.of(2024, 11, 5));
            reservation1.setCheckOutDate(LocalDate.of(2024, 11, 10));
            reservation1.setTotalAmount(new BigDecimal("750.00"));
            reservation1.setGuest(guest1);
            reservation1.setRoomType(singleRoomType);
            em.persist(reservation1);

            Reservation reservation2 = new Reservation();
            reservation2.setReservationDate(LocalDate.now());
            reservation2.setCheckInDate(LocalDate.of(2024, 12, 1));
            reservation2.setCheckOutDate(LocalDate.of(2024, 12, 5));
            reservation2.setTotalAmount(new BigDecimal("1250.00"));
            reservation2.setGuest(guest2);
            reservation2.setRoomType(doubleRoomType);
            em.persist(reservation2);

            // Additional Reservations can be added here (up to 10)
            Reservation reservation3 = new Reservation();
            reservation3.setReservationDate(LocalDate.now());
            reservation3.setCheckInDate(LocalDate.of(2024, 12, 10));
            reservation3.setCheckOutDate(LocalDate.of(2024, 12, 15));
            reservation3.setTotalAmount(new BigDecimal("2000.00"));
            reservation3.setGuest(guest3);
            reservation3.setRoomType(deluxeRoomType);
            em.persist(reservation3);

            // ---------- Initialize Room Reservations ----------
            RoomReservation roomReservation1 = new RoomReservation();
            roomReservation1.setRoom(room101);
            roomReservation1.setReservation(reservation1);
            em.persist(roomReservation1);

            RoomReservation roomReservation2 = new RoomReservation();
            roomReservation2.setRoom(room102);
            roomReservation2.setReservation(reservation2);
            em.persist(roomReservation2);

            RoomReservation roomReservation3 = new RoomReservation();
            roomReservation3.setRoom(room104);
            roomReservation3.setReservation(reservation3);
            em.persist(roomReservation3);

            // Additional RoomReservations can be added here (up to 10)
            RoomReservation roomReservation4 = new RoomReservation();
            roomReservation4.setRoom(room103);
            roomReservation4.setReservation(reservation1); // Example: multiple rooms for one reservation
            em.persist(roomReservation4);

            // ---------- Initialize Exception Reports ----------
            // Assuming ExceptionReport has fields like reportID, reservationID, reportType, creationDate, etc.
            // If not, adjust accordingly based on your actual entity definition.

            // Exception Report 1
            ExceptionReport exceptionReport1 = new ExceptionReport();
            exceptionReport1.setReservationID(reservation1.getReservationID());
            em.persist(exceptionReport1);

            // Exception Report 2
            ExceptionReport exceptionReport2 = new ExceptionReport();
            exceptionReport2.setReservationID(reservation2.getReservationID());
            em.persist(exceptionReport2);

            // Additional ExceptionReports can be added here (up to 10)
            ExceptionReport exceptionReport3 = new ExceptionReport();
            exceptionReport3.setReservationID(reservation3.getReservationID());
            em.persist(exceptionReport3);

            // ---------- Completion Message ----------
            System.out.println("Database initialized with sample data.");
        } else {
            System.out.println("Database already initialized. Skipping data initialization.");
        }
    }
}
