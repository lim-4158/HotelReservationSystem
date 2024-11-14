    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.GuestRelationOfficerSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    @EJB 
    private GuestRelationOfficerSessionBeanLocal guestRelationOfficerSessionBeanLocal; 

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogIn")
    public Partner partnerLogIn(@WebParam(name = "username") String username, 
                                    @WebParam(name = "password") String password) {
        return partnerSessionBeanLocal.partnerLogIn(username, password); 
    }
    
    @WebMethod(operationName = "searchAvailableRoom")
    public List<RoomType> searchAvailableRoom(@WebParam(name = "checkInDate") Date checkInDate, 
                                @WebParam(name = "checkOutDate") Date checkOutDate, 
                                    @WebParam(name = "requiredRooms") int requiredRooms) {
        return partnerSessionBeanLocal.searchAvailableRooms(checkInDate, checkOutDate, requiredRooms); 
    }
    
    @WebMethod(operationName = "viewAllReservations")
    public List<Reservation> viewAllReservations(@WebParam(name = "partnerId") Long partnerId) {
        return partnerSessionBeanLocal.viewAllReservations(partnerId); 
    }    
    
    @WebMethod(operationName = "viewReservation") 
    public Reservation viewReservation(@WebParam(name = "reservationId") Long reservationId) {
        return partnerSessionBeanLocal.viewReservation(reservationId); 
    }
    
    @WebMethod(operationName = "calculateTotalAmountForStay")
    public BigDecimal calculateTotalAmountForStay(
            @WebParam(name = "roomTypeName") String roomTypeName, 
            @WebParam(name = "checkInDate") Date checkInDate, 
            @WebParam(name = "checkOutDate") Date checkOutDate, 
            @WebParam(name = "requiredRooms") int requiredRooms) {

        return partnerSessionBeanLocal.calculateTotalAmountForStay(roomTypeName, checkInDate, checkOutDate, requiredRooms);
    }    
        
    @WebMethod(operationName = "findGuestByEmail")
    public Guest findGuestByEmail(@WebParam(name = "email") String email) {
        return partnerSessionBeanLocal.findGuestByEmail(email); 
    }
     @WebMethod(operationName = "reserveRoom")
    public void reserveRoom(
        @WebParam(name = "bookingDate") Date bookingDate,
        @WebParam(name = "checkInDate") Date checkInDate,
        @WebParam(name = "checkOutDate") Date checkOutDate,
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
