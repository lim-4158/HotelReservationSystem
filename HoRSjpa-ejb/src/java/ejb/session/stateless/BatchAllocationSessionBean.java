/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import entity.Room;
import entity.RoomReservation;
import entity.RoomType;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.ExceptionReportTypeEnum;
import util.RoomStatusEnum;

/**
 *
 * @author kevinlim
 */
@Stateless
public class BatchAllocationSessionBean implements BatchAllocationSessionBeanRemote, BatchAllocationSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;
    
    
    // Allocate Rooms method allocates the reservations for all reservations who check in on the date itself
    @Override
    public void allocateRooms(LocalDate date) {
        
        List<RoomType> rts = em.createQuery("SELECT rt FROM RoomType rt").getResultList(); 
        for (RoomType rt : rts) {
            int totalInventory = (int) rt.getRooms().stream()
                .filter(room -> room.getRoomStatus() != RoomStatusEnum.DISABLED)
                .count();
           
            System.out.println(rt.getTypeName() + " : " + totalInventory);
            for (Room r : rt.getRooms()) {
                System.out.println(r.getRoomNumber());
            }
        }
        
        System.out.println("printing out reservations here --->");
        // Allocate reservations with the highest priority room type first
        Query q = em.createQuery(
        "SELECT r FROM Reservation r WHERE r.checkInDate = :checkInDate ORDER BY r.roomType.tierNumber DESC", Reservation.class);
        q.setParameter("checkInDate", date);
        
        List<Reservation> reservations = q.getResultList();
        System.out.println(reservations.size());
        
        for (Reservation r : reservations) {
            System.out.println(r.toString());
        }
        
        for (Reservation r : reservations) {
            
            System.out.println("allocating reservation : " + r.toString());
            // Get the desired Room Type
            RoomType rt = r.getRoomType();
            
            // Get the desired number of Rooms 
            int requiredRooms = r.getNumberOfRooms();
            
            // For each room, attempt to allocate
            for (int i = 0; i < requiredRooms; i++) {
                System.out.println("ALLOCATING " + (i+1) + "/" + requiredRooms);
                Map.Entry<Boolean, RoomReservation> allocated = allocate(rt, date, r);
                
                System.out.println("ALLOCATED RESULT: " + allocated.getKey() + " " + allocated.getValue());
                
                if(allocated.getKey()) {
                    System.out.println("Reservation: " + allocated.getValue().getReservation().toString());
                    System.out.println("Room Number: " + allocated.getValue().getRoom().getRoomNumber());
                }
            
                if (!allocated.getKey()) {
                    // TYPE 1: check for the other room types, allocate, and then generate exception report
                    boolean isType1 = false;

                    System.out.println("Attempting to allocate type 1 exception");

                    int tierNumber = rt.getTierNumber();
                    Query query = em.createQuery(
                            "SELECT rt FROM RoomType rt WHERE rt.tierNumber > :currentTierNumber ORDER BY rt.tierNumber ASC", RoomType.class).setParameter("currentTierNumber", tierNumber);

                    List<RoomType> roomtypes = query.getResultList();
                    
                    System.out.println("List of Room Types of higher priority than " + rt.getTypeName());
                    
                    for (RoomType rtsss : roomtypes) {
                        System.out.println(rtsss.getTypeName());
                    }

                    for (RoomType rt1 : roomtypes) {
                        
                        System.out.println("Attempting to allocate to the next higher room type: " + rt1.getTypeName());

                        Map.Entry<Boolean, RoomReservation> nextAllocated = allocate(rt1, date, r);
                        System.out.println("NEXTALLOCATED RESULT: " + nextAllocated.getKey() + " : " + nextAllocated.getValue());
                        
                        if (nextAllocated.getKey()) {
                            isType1 = true;
                            
                            r.setRoomType(rt1);
                            rt1.getReservations().add(r);
                            
                            System.out.println("GENERATING TYPE 1 EXCEPTION");

                            RoomReservation rv = nextAllocated.getValue();
                            ExceptionReport er1 = new ExceptionReport(rv, ExceptionReportTypeEnum.TYPE1, LocalDate.now(), r.getReservationID());
                            em.persist(er1);
                            em.flush();
                            break;
                        }

                    }

                    // TYPE 2: check for the other room types, allocate, and then generate exception report
                    if (!isType1) {
                        
                        System.out.println("GENERATING TYPE 2 EXCEPTION");
                        ExceptionReport er2 = new ExceptionReport(ExceptionReportTypeEnum.TYPE2, LocalDate.now(), r.getReservationID());
                        em.persist(er2);
                        em.flush();
                    }    

                }
                
            }
        
        }
        
    }
    
    private Map.Entry<Boolean, RoomReservation> allocate(RoomType rt, LocalDate date, Reservation r) {
        
        System.out.println("---------- ALLOCATE FUNCTION TRIGGERED -----------");
        
        System.out.println("Allocating roomtype: " + rt.getTypeName());
        System.out.println("");
      
        List<Room> rooms = rt.getRooms();
        
        rooms = rooms.stream().filter(room -> room.getRoomStatus() != RoomStatusEnum.DISABLED).filter(room -> room.getRoomStatus() != RoomStatusEnum.UNAVAILABLE).collect(Collectors.toList()); 
        for (Room rooom : rooms) {
            System.out.println("--------------  IIIIIOOOOOOOO------------");
            System.out.println(rooom.getRoomID());
        }
        
        boolean allocated = false;

        for(Room room : rooms){
            
            System.out.println("CHECKING ROOM : " + room.getRoomNumber());

            boolean exist = true;

            // get roomreservations
            Query q1 = em.createQuery("SELECT rv FROM RoomReservation rv WHERE rv.room = :room")
                .setParameter("room", room);

            List<RoomReservation> roomreservations = q1.getResultList();

            for (RoomReservation rr : roomreservations){
                System.out.println("roomreservation id : " + rr.getRoomReservationId() + " : Reservation : " + rr.getReservation().toString());
                System.out.println("roomreservation id : " + rr.getRoomReservationId() + " : Rooms : " + rr.getRoom().getRoomNumber());
                Reservation currentReservation = rr.getReservation();
                LocalDate checkOutDate = currentReservation.getCheckOutDate();
                LocalDate checkInDate = currentReservation.getCheckInDate();

                System.out.println(checkInDate + " -- " + checkOutDate);
                System.out.println("FOR TARGET RESERVATION: " + r.getCheckInDate() + " -- " + r.getCheckOutDate());
                 
                if ((date.isAfter(checkInDate) || date.equals(checkInDate)) && checkOutDate.isAfter(date)) { // the room reservation is occupied
                    exist = false;
                    System.out.println("THIS ROOM IS OCCUPIED" + room.getRoomNumber());
                    break;
                }

            }

            if (exist == true) {
                RoomReservation rv = new RoomReservation(room, r);
                System.out.println("THIS ROOM IS NOT OCCUPIED" + room.getRoomNumber());
                allocated = true;
                em.persist(rv);
                em.flush();
                return new AbstractMap.SimpleEntry<>(allocated, rv);
            }
        }
        
        return new AbstractMap.SimpleEntry<>(allocated, null);
    }
    
    @Override
    public void getAllRoomReservations() {
        // Step 1: Query all RoomReservation records
        TypedQuery<RoomReservation> query = em.createQuery("SELECT rr FROM RoomReservation rr", RoomReservation.class);
        List<RoomReservation> roomReservations = query.getResultList();

        // Step 2: Print each RoomReservation record
        if (roomReservations.isEmpty()) {
            System.out.println("No Room Reservations found.");
        } else {
            System.out.println("Room Reservations:");
            for (RoomReservation roomReservation : roomReservations) {
                System.out.println("RoomReservation ID: " + roomReservation.getRoomReservationId());
                System.out.println("Room Number: " + roomReservation.getRoom().getRoomNumber());
                System.out.println("Reservation ID: " + roomReservation.getReservation().getReservationID());
                System.out.println("Room Reservation RoomType: " + roomReservation.getReservation().getRoomType().getTypeName());
                System.out.println("Guest: " + roomReservation.getReservation().getGuest().getFirstName());
                System.out.println("--------------------------------------");
            }
        }
    }
    
    @Override
    public void getAllExceptionReports() {
        // Step 1: Query all RoomReservation records
        TypedQuery<ExceptionReport> query = em.createQuery("SELECT er FROM ExceptionReport er", ExceptionReport.class);
        List<ExceptionReport> exceptionReports = query.getResultList();

        // Step 2: Print each RoomReservation record
        if (exceptionReports.isEmpty()) {
            System.out.println("No Exception Reports found.");
        } else {
            System.out.println("Exception Reports:");
            for (ExceptionReport exceptionReport : exceptionReports) {
                System.out.println("Reservation ID: " + exceptionReport.getResID());
                System.out.println("Exception Type: " + exceptionReport.getReportType());
                System.out.println("--------------------------------------");
            }
        }
    }
    
    @Override
    public void getAllReservations() {
        // Step 1: Query all RoomReservation records
        TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r", Reservation.class);
        List<Reservation> reservations = query.getResultList();

        // Step 2: Print each RoomReservation record
        if (reservations.isEmpty()) {
            System.out.println("No Reservations found.");
        } else {
            System.out.println("Reservations:");
            for (Reservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getReservationID());
                System.out.println("Reservation Guest: " + reservation.getGuest());
                System.out.println("Room Type: " + reservation.getRoomType());
                System.out.println("Number of Rooms: " + reservation.getNumberOfRooms());
                
                System.out.println("--------------------------------------");
            }
        }
    }


}
