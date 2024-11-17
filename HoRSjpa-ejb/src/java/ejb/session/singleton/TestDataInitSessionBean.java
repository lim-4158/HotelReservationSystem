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
        try {
            // Check if Employees already exist to avoid duplicate initialization
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class);
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
//                RoomType presidentialSuite = new RoomType("Presidential Penthouse", "", BigDecimal.ONE, "", 1L, "", RoomTypeStatusEnum.ENABLED, 6, 0L);

                System.out.println("----------------------------------------------2");

                em.persist(grandSuite);
                em.persist(juniorSuite);
                em.persist(familyRoom);
                em.persist(premierRoom);
                em.persist(deluxeRoom);
//                em.persist(presidentialSuite);
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
                
//                Room room = new Room(presidentialSuite, "8001", RoomStatusEnum.AVAILABLE); 
//                em.persist(room);
//
//                Guest guest1 = new Guest("John", "Doe", "john", "12345678", "ab", "john", "password");
//                LocalDate.of(2024,5,1);//2024-05-01.
//                Reservation reservation1 = new Reservation(LocalDate.now(), LocalDate.of(2025,1,1), LocalDate.of(2025,1,4), ReservationTypeEnum.ONLINE, guest1, presidentialSuite, 1);
//                
//                em.persist(guest1);
//                em.persist(reservation1);

            }
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error initializing test data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
