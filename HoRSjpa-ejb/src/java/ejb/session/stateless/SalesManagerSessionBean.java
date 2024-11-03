/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.RoomRateTypeEnum;

/**
 *
 * @author jamiewee
 */
@Stateless
public class SalesManagerSessionBean implements SalesManagerSessionBeanRemote, SalesManagerSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;
    
    public Long createNewRoomRate(RoomRate roomRate) {
        em.persist(roomRate);
        em.flush();
        
        return roomRate.getRoomRateID();
    }
    
    public void updateRateName(String rateName, String roomType, String rateType, String newRateName) {
    
    }
    
    public void updateRoomType(String rateName, String roomType, String rateType, String newRoomType) {
    
    }
    
    public void updateRateType(String rateName, String roomType, String rateType, RoomRateTypeEnum rateTypeEnum) {
    
    }
    
    public void updateRateAmount(String rateName, String roomType, String rateType, BigDecimal newAmount) {
    
    }
    
    public void updateStartDate(String rateName, String roomType, String rateType, LocalDate newDate) {
    
    }
    
    public void updateEndDate(String rateName, String roomType, String rateType, LocalDate newDate) {
    
    }
    
    
    
    
    
    public void deleteRoomRate(String rateName) {
        RoomRate rate = retrieveRoomRateByName(rateName);
                
//        try {
            
            em.remove(rate);  // Delete the entity
//        } catch (NoResultException e) {
//            System.out.println("RoomRate not found for deletion.");
//        }
    }
    
    public List<RoomRate> retrieveAllRoomRates() {
        return em.createQuery("SELECT r FROM RoomRate r", RoomRate.class).getResultList();
    }
    
    
    public RoomRate retrieveRoomRateByName(String rateName) {
        Query q = em.createQuery("SELECT r FROM RoomRate r WHERE r.rateName = :rateName ");
        q.setParameter("rateName", rateName);
 

        return (RoomRate) q.getSingleResult();
    }
    
    
    
    
    
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
