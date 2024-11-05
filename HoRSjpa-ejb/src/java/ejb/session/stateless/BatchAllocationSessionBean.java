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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.ExceptionReportTypeEnum;

/**
 *
 * @author kevinlim
 */
@Stateless
public class BatchAllocationSessionBean implements BatchAllocationSessionBeanRemote, BatchAllocationSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;
    
    @Override
    public void allocateRooms(LocalDate date) {
        
        Query q = em.createQuery(
            "SELECT r FROM Reservation r WHERE r.checkInDate = :checkInDate", Reservation.class).setParameter("checkInDate", date);
        
        List<Reservation> reservations = q.getResultList();
        
        for (Reservation r : reservations) {
            RoomType rt = r.getRoomType();
            
            
            Map.Entry<Boolean, RoomReservation> allocated = allocate(rt, date, r);
            
        
            if (!allocated.getKey()) {
                // TYPE 1: check for the other room types, allocate, and then generate exception report
                boolean isType1 = false;
                
                
                int tierNumber = rt.getTierNumber();
                Query query = em.createQuery(
                        "SELECT rt FROM RoomType rt WHERE rt.tierNumber > :currentTierNumber ORDER BY rt.tierNumber ASC", RoomType.class).setParameter("currentTierNumber", tierNumber);

                List<RoomType> roomtypes = query.getResultList();
                
                for (RoomType rt1 : roomtypes) {
                
                    Map.Entry<Boolean, RoomReservation> nextAllocated = allocate(rt1, date, r);
                    if (nextAllocated.getKey()) {
                        isType1 = true;
                        
                        RoomReservation rv = nextAllocated.getValue();
                        ExceptionReport er1 = new ExceptionReport(rv, ExceptionReportTypeEnum.TYPE1, LocalDate.now(), r.getReservationID());
                        em.persist(er1);
                        em.flush();
                        break;
                    }
                
                }
                
                // TYPE 2: check for the other room types, allocate, and then generate exception report
                if (!isType1) {
                    ExceptionReport er2 = new ExceptionReport(ExceptionReportTypeEnum.TYPE2, LocalDate.now(), r.getReservationID());
                    em.persist(er2);
                    em.flush();
                }    
                
            }
        
        }
        
    }
    
    public Map.Entry<Boolean, RoomReservation> allocate(RoomType rt, LocalDate date, Reservation r) {
        List<Room> rooms = rt.getRooms();
            
        boolean allocated = false;

        for(Room room : rooms){

            boolean exist = true;

            // get roomreservations
            Query q1 = em.createQuery("SELECT rv FROM RoomReservation rv WHERE rv.room = :room AND rv.reservation = :reservation")
                .setParameter("room", room)
                .setParameter("reservation", r);

            List<RoomReservation> roomreservations = q1.getResultList();

            for (RoomReservation rr : roomreservations){
                Reservation currentReservation = rr.getReservation();
                LocalDate checkOutDate = currentReservation.getCheckOutDate();

                if (checkOutDate.isAfter(date)) { // the room reservation is occupied
                    exist = false;
                    break;
                }

            }

            if (exist == true) {
                RoomReservation rv = new RoomReservation(room, r);
                allocated = true;
                em.persist(rv);
                em.flush();
                return new AbstractMap.SimpleEntry<>(allocated, rv);
            }
        }
        
        return new AbstractMap.SimpleEntry<>(allocated, null);
    }


}
