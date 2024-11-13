/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.RoomRateTypeEnum;
import util.exceptions.RoomRateNotFoundException;

/**
 *
 * @author jamiewee
 */
@Remote
public interface SalesManagerSessionBeanRemote {
        // Retrieval Methods
    
    public RoomRate retrieveRoomRateByName(String rateName);
    public RoomRate retrieveRoomRateByID(Long rateID) throws RoomRateNotFoundException;
    
    // 21. View All Room Rates
    
    public List<RoomRate> retrieveAllRoomRates();  
    public List<RoomType> retrieveAllRoomTypes();
    
    // 17. Create New Room Rate
    
    public Long createNewRoomRate(RoomRate roomRate);
    
    // 19. Update Room Rate
    
    public void updateRateName(Long rateID, String newRateName) throws RoomRateNotFoundException;
    public void updateRoomType(Long rateID, RoomType newRoomType) throws RoomRateNotFoundException;
    public void updateRateType(Long rateID, RoomRateTypeEnum rateTypeEnum) throws RoomRateNotFoundException;
    public void updateRateAmount(Long rateID, BigDecimal newAmount) throws RoomRateNotFoundException;
    public void updateStartDate(Long rateID, LocalDate newDate) throws RoomRateNotFoundException;
    public void updateEndDate(Long rateID, LocalDate newDate) throws RoomRateNotFoundException;
    public void updateIsDisabled(Long rateID, boolean isDisabled) throws RoomRateNotFoundException;

    // 20. Delete Room Rate
    public void deleteRoomRate(Long roomRateID) throws RoomRateNotFoundException;
    
}
