/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.RoomRateTypeEnum;
import util.exceptions.RoomRateNotFoundException;

/**
 *
 * @author jamiewee
 */
@Stateless
public class SalesManagerSessionBean implements SalesManagerSessionBeanRemote, SalesManagerSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;
    
    // Retrieval Methods
    @Override
    public RoomRate retrieveRoomRateByName(String rateName) {
        Query q = em.createQuery("SELECT r FROM RoomRate r WHERE r.roomRateName = :rateName ");
        q.setParameter("rateName", rateName);
 
        return (RoomRate) q.getSingleResult();
    }
    @Override
    public RoomRate retrieveRoomRateByID(Long rateID) throws RoomRateNotFoundException {
        RoomRate roomRate = em.find(RoomRate.class, rateID);

        if (roomRate == null) {
            throw new RoomRateNotFoundException("RoomRate with ID " + rateID + " not found.");
        }

        return roomRate;
    }
    
    // 21. View All Room Rates
    @Override
    public List<RoomRate> retrieveAllRoomRates() {
        return em.createQuery("SELECT r FROM RoomRate r", RoomRate.class).getResultList();
    }
    @Override
    public List<RoomType> retrieveAllRoomTypes() {
        Query q = em.createQuery("SELECT rt FROM RoomType rt"); 
        return q.getResultList(); 
    }
    
    // 17. Create New Room Rate
    @Override
    public Long createNewRoomRate(RoomRate roomRate) {
        em.persist(roomRate);
        em.flush();
        
        return roomRate.getRoomRateID();
    }
    
    // 19. Update Room Rate
    @Override
    public void updateRateName(Long rateID, String newRateName) throws RoomRateNotFoundException {
        try {
            RoomRate rate = retrieveRoomRateByID(rateID);
            rate.setRoomRateName(newRateName);
            em.merge(rate);
        } catch (NoResultException e) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateID + " not found.");
        }
    }
    @Override
    public void updateRoomType(Long rateID, RoomType newRoomType) throws RoomRateNotFoundException {
        try {
            RoomRate rate = retrieveRoomRateByID(rateID);
            rate.setRoomType(newRoomType);
            em.merge(rate);
        } catch (NoResultException e) {
            throw new RoomRateNotFoundException("Room Rate Not Found.");
        }
    }
    @Override
    public void updateRateType(Long rateID, RoomRateTypeEnum rateTypeEnum) throws RoomRateNotFoundException {
        try {
            RoomRate rate = retrieveRoomRateByID(rateID);
            rate.setRateType(rateTypeEnum);
            em.merge(rate);
        } catch (NoResultException e) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateID + " not found.");
        }
    }
    @Override
    public void updateRateAmount(Long rateID, BigDecimal newAmount) throws RoomRateNotFoundException {
        try {
            RoomRate rate = retrieveRoomRateByID(rateID);
            rate.setNightlyRateAmount(newAmount);
            em.merge(rate);
        } catch (NoResultException e) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateID + " not found.");
        }
    }
    @Override
    public void updateStartDate(Long rateID, LocalDate newDate) throws RoomRateNotFoundException {
        try {
            RoomRate rate = retrieveRoomRateByID(rateID);
            rate.setStartDate(newDate);
            em.merge(rate);
        } catch (NoResultException e) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateID + " not found.");
        }
    }
    @Override
    public void updateEndDate(Long rateID, LocalDate newDate) throws RoomRateNotFoundException {
        try {
            RoomRate rate = retrieveRoomRateByID(rateID);
            rate.setEndDate(newDate);
            em.merge(rate);
        } catch (NoResultException e) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateID + " not found.");
        }
    }
    @Override
    public void updateIsDisabled(Long rateID, boolean isDisabled) throws RoomRateNotFoundException {
        RoomRate rate = retrieveRoomRateByID(rateID);
        if (rate == null) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateID + " not found.");
        }
        rate.setIsDisabled(isDisabled);
        em.merge(rate);
    }

    // 20. Delete Room Rate
    
    @Override
    public void deleteRoomRate(Long roomRateID) throws RoomRateNotFoundException {
        try {
            // Step 1: Retrieve the RoomRate by ID
            RoomRate roomRate = em.find(RoomRate.class, roomRateID);

            if (roomRate == null) {
                throw new RoomRateNotFoundException("RoomRate ID " + roomRateID + " not found.");
            }

            // Step 2: Check if the RoomRate is in use
            if (!isRoomRateInUse(roomRate)) {
                // If not in use, delete it
                em.remove(roomRate);
                System.out.println("RoomRate deleted successfully.");
            } else {
                // If in use, disable it by setting an `isDisabled` flag or similar
                roomRate.setIsDisabled(true);
                em.merge(roomRate);
                System.out.println("RoomRate is in use and has been disabled.");
            }
        } catch (NoResultException e) {
            throw new RoomRateNotFoundException("RoomRate with ID " + roomRateID + " not found.");
        }
    }
    
    private boolean isRoomRateInUse(RoomRate roomRate) {
        // Retrieve the RoomType associated with the given RoomRate
        RoomType roomType = roomRate.getRoomType();

        if (roomType != null) {
            // Query to count the number of RoomRates associated with this RoomType
            Query query = em.createQuery(
                "SELECT COUNT(rr) " +
                "FROM RoomRate rr " +
                "WHERE rr.roomType = :roomType"
            );
            query.setParameter("roomType", roomType);

            Long count = (Long) query.getSingleResult();

            // If there is more than one RoomRate, it's not in use; if exactly one, it is in use
            return count == 1;
        }

        // If the RoomType is null, we assume the RoomRate is not in use
        return false;
    }

}
