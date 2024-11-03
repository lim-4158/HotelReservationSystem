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
import util.EmployeeRoleEnum;

/**
 *
 * @author jamiewee
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void PostConstruct() {
        // Initialize Room Types
        RoomType singleRoomType = new RoomType();
        singleRoomType.setTypeName("Single");
        singleRoomType.setDescription("Single bed room");
        em.persist(singleRoomType);
        
        RoomType doubleRoomType = new RoomType();
        doubleRoomType.setTypeName("Double");
        doubleRoomType.setDescription("Double bed room");
        em.persist(doubleRoomType);

        // Initialize Room Rates

        RoomRate doubleRoomRate = new RoomRate();
        doubleRoomRate.setRoomRateName("Standard Rate");
        doubleRoomRate.setNightlyRateAmount(new BigDecimal("150.00"));
        em.persist(doubleRoomRate);

       // Initialize Rooms

        Room room102 = new Room();
        room102.setRoomNumber("102");
        em.persist(room102);

        // Initialize Guests
        Guest guest1 = new Guest();
        guest1.setFirstName("John");
        guest1.setLastName("Doe");
        guest1.setUsername("johndoe");
        guest1.setPassword("password123");
        guest1.setEmail("johndoe@example.com");
        guest1.setPhoneNumber("12345678");
        guest1.setPassportNumber("A1234567");
        em.persist(guest1);
//
//        Guest guest2 = new Guest();
//        guest2.setFirstName("Jane");
//        guest2.setLastName("Smith");
//        guest2.setUsername("janesmith");
//        guest2.setPassword("password456");
//        guest2.setEmail("janesmith@example.com");
//        guest2.setPhoneNumber("87654321");
//        guest2.setPassportNumber("B7654321");
//        em.persist(guest2);
//
//        // Initialize Partner
        Partner partner = new Partner();
        partner.setPartnerName("Travel Agency");
        em.persist(partner);
//
//        // Initialize Employee
        Employee employee = new Employee();
        employee.setUsername("markjohnson");
        employee.setPassword("emp1234");
        employee.setRole(EmployeeRoleEnum.SALESMANAGER);
        em.persist(employee);
//
//        // Initialize Reservation
        Reservation reservation1 = new Reservation();
        reservation1.setReservationDate(LocalDate.now());
        reservation1.setCheckInDate(LocalDate.of(2024, 11, 5));
        reservation1.setCheckOutDate(LocalDate.of(2024, 11, 10));
        reservation1.setTotalAmount(new BigDecimal("500.00"));
        reservation1.setGuest(guest1);
        reservation1.setRoomType(singleRoomType);
        em.persist(reservation1);

        // Initialize Room Reservations
//        RoomReservation roomReservation1 = new RoomReservation();
//        roomReservation1.setRoom(room101);
//        roomReservation1.setReservation(reservation1);
//        em.persist(roomReservation1);
//
//        RoomReservation roomReservation2 = new RoomReservation();
//        roomReservation2.setRoom(room102);
//        roomReservation2.setReservation(reservation2);
//        em.persist(roomReservation2);

        // Log initialization
        System.out.println("Database initialized with sample data.");
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
