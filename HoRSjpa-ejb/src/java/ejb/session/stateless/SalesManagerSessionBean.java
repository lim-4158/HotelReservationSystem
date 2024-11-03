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
    
    public void updateRateName(String rateName, String newRateName) {
        RoomRate rate = retrieveRoomRateByName(rateName);
        rate.setRoomRateName(newRateName);
        em.merge(rate);
    
    }
    
    public void updateRoomType(String rateName, String newRoomType) {
        RoomRate rate = retrieveRoomRateByName(rateName);
        rate.setRoomType(newRoomType);
        em.merge(rate);
    
    }
    
    public void updateRateType(String rateName, RoomRateTypeEnum rateTypeEnum) {
        RoomRate rate = retrieveRoomRateByName(rateName);
        rate.setRateType(rateTypeEnum);
        em.merge(rate);
    }
    
    public void updateRateAmount(String rateName, BigDecimal newAmount) {
        RoomRate rate = retrieveRoomRateByName(rateName);
        rate.setNightlyRateAmount(newAmount);
        em.merge(rate);
    }
    
    public void updateStartDate(String rateName, LocalDate newDate) {
        RoomRate rate = retrieveRoomRateByName(rateName);
        rate.setStartDate(newDate);
        em.merge(rate);
    }
    
    public void updateEndDate(String rateName, LocalDate newDate) {
        RoomRate rate = retrieveRoomRateByName(rateName);
        rate.setEndDate(newDate);
        em.merge(rate);
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
        Query q = em.createQuery("SELECT r FROM RoomRate r WHERE r.roomRateName = :rateName ");
        q.setParameter("rateName", rateName);
 
        return (RoomRate) q.getSingleResult();
    }
    
    
    
    
    
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
