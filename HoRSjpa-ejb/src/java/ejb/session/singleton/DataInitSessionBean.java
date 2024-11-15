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
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import util.EmployeeRoleEnum;
import util.ReservationTypeEnum;
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

    @PostConstruct
    public void postConstruct(){
        try {
            // Check if Employees already exist to avoid duplicate initialization
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class);
            System.out.println("COYUNT ===== " + query.getResultList());
            Long count = query.getSingleResult();

            if (count == 0) {
                // ----------------------------- Create Employees --------------------------------
                Employee sysadmin = new Employee("sysadmin", "password", EmployeeRoleEnum.SYSTEMADMIN);
                Employee opManager = new Employee("opmanager", "password", EmployeeRoleEnum.OPERATIONSMANAGER);
                Employee salesManager = new Employee("salesmanager", "password", EmployeeRoleEnum.SALESMANAGER);
                Employee guestRelOfficer = new Employee("guestrelo", "password", EmployeeRoleEnum.GUESTRELATIONOFFIER);

                em.persist(sysadmin);
                em.persist(opManager);
                em.persist(salesManager);
                em.persist(guestRelOfficer);

                System.out.println("----------------------------------------------1");

                // ----------------------------- Create Room Types --------------------------------
                RoomType grandSuite = new RoomType("Grand Suite", "", BigDecimal.ONE, "", 1L, "", RoomTypeStatusEnum.ENABLED, 5, 0L);
                RoomType juniorSuite = new RoomType("Junior Suite",  "", BigDecimal.ONE, "", 1L, "", RoomTypeStatusEnum.ENABLED, 4, 0L);
                RoomType familyRoom = new RoomType("Family Room",  "", BigDecimal.ONE, "", 1L, "", RoomTypeStatusEnum.ENABLED, 3, 0L);
                RoomType premierRoom = new RoomType("Premier Room",  "", BigDecimal.ONE, "", 1L, "", RoomTypeStatusEnum.ENABLED, 2, 0L);
                RoomType deluxeRoom = new RoomType("Deluxe Room", "", BigDecimal.ONE, "", 1L, "", RoomTypeStatusEnum.ENABLED, 1, 0L);

                System.out.println("----------------------------------------------2");

                em.persist(grandSuite);
                em.persist(juniorSuite);
                em.persist(familyRoom);
                em.persist(premierRoom);
                em.persist(deluxeRoom);
                em.flush();

                System.out.println("----------------------------------------------3");

                // ----------------------------- Create Room Rates --------------------------------
                RoomRate deluxePublished = new RoomRate("Deluxe Room Published", deluxeRoom, RoomRateTypeEnum.PUBLISHED, new BigDecimal("100"));
                RoomRate deluxeNormal = new RoomRate("Deluxe Room Normal", deluxeRoom, RoomRateTypeEnum.NORMAL, new BigDecimal("50"));
                RoomRate premierPublished = new RoomRate("Premier Room Published", premierRoom, RoomRateTypeEnum.PUBLISHED, new BigDecimal("200"));
                RoomRate premierNormal = new RoomRate("Premier Room Normal", premierRoom, RoomRateTypeEnum.NORMAL, new BigDecimal("100"));
                RoomRate familyPublished = new RoomRate("Family Room Published", familyRoom, RoomRateTypeEnum.PUBLISHED, new BigDecimal("300"));
                RoomRate familyNormal = new RoomRate("Family Room Normal", familyRoom, RoomRateTypeEnum.NORMAL, new BigDecimal("150"));
                RoomRate juniorPublished = new RoomRate("Junior Suite Published", juniorSuite, RoomRateTypeEnum.PUBLISHED, new BigDecimal("400"));
                RoomRate juniorNormal = new RoomRate("Junior Suite Normal", juniorSuite, RoomRateTypeEnum.NORMAL, new BigDecimal("200"));
                RoomRate grandPublished = new RoomRate("Grand Suite Published", grandSuite, RoomRateTypeEnum.PUBLISHED, new BigDecimal("500"));
                RoomRate grandNormal = new RoomRate("Grand Suite Normal", grandSuite, RoomRateTypeEnum.NORMAL, new BigDecimal("250"));

                // Set bidirectional relationship between RoomRate and RoomType
                // Adding RoomRates to RoomType's roomRates list and setting RoomType in RoomRate
                deluxeRoom.getRoomRates().add(deluxePublished);
                deluxeRoom.getRoomRates().add(deluxeNormal);
                deluxePublished.setRoomType(deluxeRoom);
                deluxeNormal.setRoomType(deluxeRoom);

                premierRoom.getRoomRates().add(premierPublished);
                premierRoom.getRoomRates().add(premierNormal);
                premierPublished.setRoomType(premierRoom);
                premierNormal.setRoomType(premierRoom);

                familyRoom.getRoomRates().add(familyPublished);
                familyRoom.getRoomRates().add(familyNormal);
                familyPublished.setRoomType(familyRoom);
                familyNormal.setRoomType(familyRoom);

                juniorSuite.getRoomRates().add(juniorPublished);
                juniorSuite.getRoomRates().add(juniorNormal);
                juniorPublished.setRoomType(juniorSuite);
                juniorNormal.setRoomType(juniorSuite);

                grandSuite.getRoomRates().add(grandPublished);
                grandSuite.getRoomRates().add(grandNormal);
                grandPublished.setRoomType(grandSuite);
                grandNormal.setRoomType(grandSuite);

                // Persist RoomRates
                em.persist(deluxePublished);
                em.persist(deluxeNormal);
                em.persist(premierPublished);
                em.persist(premierNormal);
                em.persist(familyPublished);
                em.persist(familyNormal);
                em.persist(juniorPublished);
                em.persist(juniorNormal);
                em.persist(grandPublished);
                em.persist(grandNormal);

                // ----------------------------- Create Additional Room Rates --------------------------------
                // Creating Grand Promo and Grand Peak Room Rates
                RoomRate grandPromo = new RoomRate("Grand Suite Promo", grandSuite, RoomRateTypeEnum.PROMOTION, new BigDecimal("450"), LocalDate.now(), LocalDate.now().plusDays(100));
                RoomRate grandPeak = new RoomRate("Grand Suite Peak", grandSuite, RoomRateTypeEnum.PEAK, new BigDecimal("550"), LocalDate.now(), LocalDate.now().plusDays(100));

                // Creating Junior Promo Room Rate
                RoomRate juniorPromo = new RoomRate("Junior Suite Promo", juniorSuite, RoomRateTypeEnum.PROMOTION, new BigDecimal("350"), LocalDate.now(), LocalDate.now().plusDays(100));

                // Creating Family Peak Room Rate
                RoomRate familyPeak = new RoomRate("Family Room Peak", familyRoom, RoomRateTypeEnum.PEAK, new BigDecimal("350"), LocalDate.now(), LocalDate.now().plusDays(100));

                // Set bidirectional relationships
                grandSuite.getRoomRates().add(grandPromo);
                grandSuite.getRoomRates().add(grandPeak);
                grandPromo.setRoomType(grandSuite);
                grandPeak.setRoomType(grandSuite);

                juniorSuite.getRoomRates().add(juniorPromo);
                juniorPromo.setRoomType(juniorSuite);

                familyRoom.getRoomRates().add(familyPeak);
                familyPeak.setRoomType(familyRoom);

                // Persist new RoomRates
                em.persist(grandPromo);
                em.persist(grandPeak);
                em.persist(juniorPromo);
                em.persist(familyPeak);

                // ----------------------------- Create Rooms --------------------------------
                // Creating Deluxe Room instances
                for (int i = 1; i <= 5; i++) {
                    Room room = new Room(deluxeRoom, "0" + i + "01", RoomStatusEnum.AVAILABLE);
                    deluxeRoom.getRooms().add(room);
                    em.persist(room);
                }

                System.out.println("plsss");
                for (Room r : deluxeRoom.getRooms()) {
                    System.out.println(r.getRoomNumber());
                }

                // Creating Premier Room instances
                for (int i = 1; i <= 5; i++) {
                    Room room = new Room(premierRoom, "0" + i + "02", RoomStatusEnum.AVAILABLE);
                    premierRoom.getRooms().add(room);
                    em.persist(room);
                }

                // Creating Family Room instances
                for (int i = 1; i <= 5; i++) {
                    Room room = new Room(familyRoom, "0" + i + "03", RoomStatusEnum.AVAILABLE);
                    familyRoom.getRooms().add(room);
                    em.persist(room);
                }

                // Creating Junior Suite instances
                for (int i = 1; i <= 5; i++) {
                    Room room = new Room(juniorSuite, "0" + i + "04", RoomStatusEnum.AVAILABLE);
                    juniorSuite.getRooms().add(room);
                    em.persist(room);
                }

                // Creating Grand Suite instances
                for (int i = 1; i <= 5; i++) {
                    Room room = new Room(grandSuite, "0" + i + "05", RoomStatusEnum.AVAILABLE);
                    grandSuite.getRooms().add(room);
                    em.persist(room);
                }            

                // ----------------------------- Create Guests --------------------------------
                Guest guest1 = new Guest("John", "Doe", "john.doe@example.com", "12345678", "ab", "guest1", "password");
                Guest guest2 = new Guest("Jane", "Smith", "jane.smith@example.com", "09876543", "ac", "guest2", "password");
                Guest guest3 = new Guest("Alice", "Johnson", "alice.johnson@example.com", "22334455", "ad", "guest3", "password");
                Guest guest4 = new Guest("Bob", "Brown", "bob.brown@example.com", "55667799", "ae", "guest4", "password");
                Guest guest5 = new Guest("Charlie", "Davis", "charlie.davis@example.com", "77889900", "af", "guest5", "password");

                em.persist(guest1);
                em.persist(guest2);
                em.persist(guest3);
                em.persist(guest4);
                em.persist(guest5);            

                // ----------------------------- Create Reservations --------------------------------
                LocalDate allocationDate = LocalDate.now().plusDays(1);

                // Existing Reservations
                Reservation reservation1 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(3), ReservationTypeEnum.ONLINE, guest1, grandSuite, 1);
                Reservation reservation2 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(2), ReservationTypeEnum.ONLINE, guest2, juniorSuite, 3);
                Reservation reservation3 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(4), ReservationTypeEnum.ONLINE, guest3, juniorSuite, 2);
                Reservation reservation4 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(5), ReservationTypeEnum.ONLINE, guest4, deluxeRoom, 1);
                Reservation reservation5 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(2), ReservationTypeEnum.ONLINE, guest5, deluxeRoom, 1);
                Reservation reservation6 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(3), ReservationTypeEnum.ONLINE, guest1, deluxeRoom, 5); // For Type 2 test
                Reservation reservation7 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(3), ReservationTypeEnum.ONLINE, guest2, familyRoom, 2); // For Type 2 test
                Reservation reservation8 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(3), ReservationTypeEnum.ONLINE, guest5, juniorSuite, 2); // For Type 2 test

                // Associate Reservations with Guests and RoomTypes
                // Reservation 1
                reservation1.setGuest(guest1);
                guest1.getReservations().add(reservation1);
                reservation1.setRoomType(grandSuite);
                grandSuite.getReservations().add(reservation1);
                em.persist(reservation1);

                // Reservation 2
                reservation2.setGuest(guest2);
                guest2.getReservations().add(reservation2);
                reservation2.setRoomType(juniorSuite);
                juniorSuite.getReservations().add(reservation2);
                em.persist(reservation2);

                // Reservation 3
                reservation3.setGuest(guest3);
                guest3.getReservations().add(reservation3);
                reservation3.setRoomType(juniorSuite);
                juniorSuite.getReservations().add(reservation3);
                em.persist(reservation3);

                // Reservation 4
                reservation4.setGuest(guest4);
                guest4.getReservations().add(reservation4);
                reservation4.setRoomType(deluxeRoom);
                deluxeRoom.getReservations().add(reservation4);
                em.persist(reservation4);

                // Reservation 5
                reservation5.setGuest(guest5);
                guest5.getReservations().add(reservation5);
                reservation5.setRoomType(deluxeRoom);
                deluxeRoom.getReservations().add(reservation5);
                em.persist(reservation5);

                // Reservation 6
                reservation6.setGuest(guest1);
                guest1.getReservations().add(reservation6);
                reservation6.setRoomType(deluxeRoom);
                deluxeRoom.getReservations().add(reservation6);
                em.persist(reservation6);

                // Reservation 7
                reservation7.setGuest(guest2);
                guest2.getReservations().add(reservation7);
                reservation7.setRoomType(familyRoom);
                familyRoom.getReservations().add(reservation7);
                em.persist(reservation7);

                // Reservation 8
                reservation8.setGuest(guest5);
                guest5.getReservations().add(reservation8);
                reservation8.setRoomType(juniorSuite);
                juniorSuite.getReservations().add(reservation8); 
                em.persist(reservation8);

                // ----------------------------- Create Additional Reservations --------------------------------
                // Reservation 9
                Reservation reservation9 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(3), ReservationTypeEnum.ONLINE, guest1, grandSuite, 3);
                reservation9.setGuest(guest1);
                guest1.getReservations().add(reservation9);
                reservation9.setRoomType(grandSuite);
                grandSuite.getReservations().add(reservation9);
                em.persist(reservation9);

                // Reservation 10
                Reservation reservation10 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(3), ReservationTypeEnum.ONLINE, guest2, grandSuite, 3);
                reservation10.setGuest(guest2);
                guest2.getReservations().add(reservation10);
                reservation10.setRoomType(grandSuite);
                grandSuite.getReservations().add(reservation10);
                em.persist(reservation10);

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

                em.flush();
            }
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error initializing test data: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
