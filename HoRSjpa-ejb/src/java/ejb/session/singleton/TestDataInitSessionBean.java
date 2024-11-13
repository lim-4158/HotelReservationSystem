/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import entity.Employee;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
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
import util.ReservationTypeEnum;
import util.RoomRateTypeEnum;
import util.RoomStatusEnum;
import util.RoomTypeStatusEnum;

/**
 *
 * @author kevinlim
 */
@Singleton
@LocalBean
@Startup
public class TestDataInitSessionBean {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct(){
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class);
        Long count = query.getSingleResult();
        
        if (count == 0) {
            // -----------------------------created employee--------------------------------

            Employee sysadmin = new Employee("sysadmin", "password", EmployeeRoleEnum.SYSTEMADMIN);
            Employee opManager = new Employee("opmanager", "password", EmployeeRoleEnum.OPERATIONSMANAGER);
            Employee salesManager = new Employee("salesmanager", "password", EmployeeRoleEnum.SALESMANAGER);
            Employee guestRelOfficer = new Employee("guestrelo", "password", EmployeeRoleEnum.GUESTRELATIONOFFIER);

            em.persist(sysadmin);
            em.persist(opManager);
            em.persist(salesManager);
            em.persist(guestRelOfficer);

            System.out.println("----------------------------------------------1");
            // -----------------------------created room type--------------------------------
            RoomType grandSuite = new RoomType("Grand Suite", "", BigDecimal.ONE, "", 1l, "", RoomTypeStatusEnum.ENABLED, 1, 0L);
            RoomType juniorSuite = new RoomType("Junior Suite",  "", BigDecimal.ONE, "", 1l, "", RoomTypeStatusEnum.ENABLED, 2, 0L);
            RoomType familyRoom = new RoomType("Family Room",  "", BigDecimal.ONE, "", 1l, "", RoomTypeStatusEnum.ENABLED, 3, 0L);
            RoomType premierRoom = new RoomType("Premier Room",  "", BigDecimal.ONE, "", 1l, "", RoomTypeStatusEnum.ENABLED, 4, 0L);
            RoomType deluxeRoom = new RoomType("Deluxe Room", "", BigDecimal.ONE, "", 1l, "", RoomTypeStatusEnum.ENABLED, 5, 0L);
            
            System.out.println("----------------------------------------------2");

            em.persist(deluxeRoom);
            em.persist(premierRoom);
            em.persist(familyRoom);
            em.persist(juniorSuite);
            em.persist(grandSuite);
            em.flush();
            
            System.out.println("----------------------------------------------3");

            // -----------------------------created room rate--------------------------------
            em.persist(new RoomRate("Deluxe Room Published", deluxeRoom, RoomRateTypeEnum.PUBLISHED, new BigDecimal("100")));
            em.persist(new RoomRate("Deluxe Room Normal", deluxeRoom, RoomRateTypeEnum.NORMAL, new BigDecimal("50")));
            em.persist(new RoomRate("Premier Room Published", premierRoom, RoomRateTypeEnum.PUBLISHED, new BigDecimal("200")));
            em.persist(new RoomRate("Premier Room Normal", premierRoom, RoomRateTypeEnum.NORMAL, new BigDecimal("100")));
            em.persist(new RoomRate("Family Room Published", familyRoom, RoomRateTypeEnum.PUBLISHED, new BigDecimal("300")));
            em.persist(new RoomRate("Family Room Normal", familyRoom, RoomRateTypeEnum.NORMAL, new BigDecimal("150")));
            em.persist(new RoomRate("Junior Suite Published", juniorSuite, RoomRateTypeEnum.PUBLISHED, new BigDecimal("400")));
            em.persist(new RoomRate("Junior Suite Normal", juniorSuite, RoomRateTypeEnum.NORMAL, new BigDecimal("200")));
            em.persist(new RoomRate("Grand Suite Published", grandSuite, RoomRateTypeEnum.PUBLISHED, new BigDecimal("500")));
            em.persist(new RoomRate("Grand Suite Normal", grandSuite, RoomRateTypeEnum.NORMAL, new BigDecimal("250")));

            // -----------------------------created room--------------------------------
            // Creating Deluxe Room instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(deluxeRoom, "0" + i + "01", RoomStatusEnum.AVAILABLE));
            }

            // Creating Premier Room instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(premierRoom, "0" + i + "02", RoomStatusEnum.AVAILABLE));
            }

            // Creating Family Room instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(familyRoom, "0" + i + "03", RoomStatusEnum.AVAILABLE));
            }

            // Creating Junior Suite instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(juniorSuite, "0" + i + "04", RoomStatusEnum.AVAILABLE));
            }

            // Creating Grand Suite instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(grandSuite, "0" + i + "05", RoomStatusEnum.AVAILABLE));
            }            
            
            
            // ADDITIONAL FROM THIS POINT ONWARDS
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
            
            
            // -----------------------------Create Reservations and RoomReservations--------------------------------
            LocalDate allocationDate = LocalDate.now().plusDays(1);

            // Create Reservations for the allocation date
            // Using the new constructor
            Reservation reservation1 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(3), ReservationTypeEnum.ONLINE, guest1, grandSuite,1);
            Reservation reservation2 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(2), ReservationTypeEnum.ONLINE, guest2, juniorSuite,3);
            Reservation reservation3 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(4), ReservationTypeEnum.ONLINE, guest3, juniorSuite,2);
            Reservation reservation4 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(5), ReservationTypeEnum.ONLINE, guest4, deluxeRoom,1);
            Reservation reservation5 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(2), ReservationTypeEnum.ONLINE, guest5, deluxeRoom,1);
            Reservation reservation6 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(3), ReservationTypeEnum.ONLINE, guest1, deluxeRoom, 2); // For Type 2 test
            Reservation reservation7 = new Reservation(LocalDate.now(), allocationDate, allocationDate.plusDays(3), ReservationTypeEnum.ONLINE, guest2, familyRoom, 2); // For Type 2 test

            em.persist(reservation1);
            em.persist(reservation2);
            em.persist(reservation3);
            em.persist(reservation4);
            em.persist(reservation5);
            em.persist(reservation6);
            em.persist(reservation7);
            
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
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
  
    
}
