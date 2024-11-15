    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author kevinlim
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogIn")
    public Partner partnerLogIn(@WebParam(name = "username") String username, 
                                    @WebParam(name = "password") String password) {
        Partner p = partnerSessionBeanLocal.partnerLogIn(username, password); 
        em.detach(p);
        p.setReservations(null);
        return p; 
    }
    
    @WebMethod(operationName = "searchAvailableRoom")
    public List<RoomType> searchAvailableRoom(@WebParam(name = "checkInDate") String checkInDate, 
                                @WebParam(name = "checkOutDate") String checkOutDate, 
                                    @WebParam(name = "requiredRooms") int requiredRooms) {
        System.out.println("@Webmethod searchAvailableRoom triggered");
        List<RoomType> roomTypes = partnerSessionBeanLocal.searchAvailableRooms(checkInDate, checkOutDate, requiredRooms); 
        for (RoomType r : roomTypes) {
            em.detach(r);
            r.setRooms(null);
            r.setReservations(null);
            r.setRoomRates(null);
        }
        return roomTypes; 
    }
    
    @WebMethod(operationName = "viewAllReservations")
    public List<Reservation> viewAllReservations(@WebParam(name = "partnerId") Long partnerId) {
        List<Reservation> reservations = partnerSessionBeanLocal.viewAllReservations(partnerId); 
        for(Reservation r : reservations) {
            em.detach(r);
            r.setPartner(null);
            r.getGuest().setReservations(null);
            r.getRoomType().setReservations(null);
            r.getRoomType().setRoomRates(null);
            r.getRoomType().setRooms(null);
            
        }
        return reservations; 
    }    
    
    @WebMethod(operationName = "viewReservation") 
    public Reservation viewReservation(@WebParam(name = "reservationId") Long reservationId) {
        Reservation reservation = partnerSessionBeanLocal.viewReservation(reservationId); 
        em.detach(reservation);
        em.detach(reservation.getGuest());
        em.detach(reservation.getPartner());
        em.detach(reservation.getRoomType());
        
        reservation.getGuest().setReservations(null);
        reservation.getPartner().setReservations(null);
        reservation.getRoomType().setReservations(null); 
        reservation.getRoomType().setRoomRates(null);
        reservation.getRoomType().setRooms(null);
        
        
        return reservation; 
    }
    
    @WebMethod(operationName = "calculateTotalAmountForStay")
    public BigDecimal calculateTotalAmountForStay (
            @WebParam(name = "roomTypeName") String roomTypeName, 
            @WebParam(name = "checkInDate") String checkInDate, 
            @WebParam(name = "checkOutDate") String checkOutDate, 
            @WebParam(name = "requiredRooms") int requiredRooms) {

        try {
            return partnerSessionBeanLocal.calculateTotalAmountForStay(roomTypeName, checkInDate, checkOutDate, requiredRooms);
        } catch (RoomTypeNotFoundException e) {
            System.out.println(e.getMessage());
            return new BigDecimal(0);
        }
        
    }    
        
    @WebMethod(operationName = "findGuestByEmail")
    public Guest findGuestByEmail(@WebParam(name = "email") String email) {
        Guest g = partnerSessionBeanLocal.findGuestByEmail(email); 
        g.setReservations(null);
        return g; 
    }
     @WebMethod(operationName = "reserveRoom")
    public void reserveRoom(
        @WebParam(name = "bookingDate") String bookingDate,
        @WebParam(name = "checkInDate") String checkInDate,
        @WebParam(name = "checkOutDate") String checkOutDate,
        @WebParam(name = "totalAmount") BigDecimal totalAmount,
        @WebParam(name = "requiredRooms") int requiredRooms,
        @WebParam(name = "guest") Guest guest,
        @WebParam(name = "selectedRoomType") RoomType selectedRoomType,
        @WebParam(name = "partner") Partner partner
    ) {
  
        partnerSessionBeanLocal.reserveRoom(
            bookingDate,
            checkInDate,
            checkOutDate,
            totalAmount,
            requiredRooms,
            guest,
            selectedRoomType,
            partner
        );
    }
    
}
