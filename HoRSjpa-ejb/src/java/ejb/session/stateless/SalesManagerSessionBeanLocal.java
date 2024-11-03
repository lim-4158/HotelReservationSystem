/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.RoomRateTypeEnum;

/**
 *
 * @author jamiewee
 */
@Local
public interface SalesManagerSessionBeanLocal {

    public Long createNewRoomRate(RoomRate roomRate);

    public void updateRateName(String rateName, String roomType, String rateType, String newRateName);

    public void updateRoomType(String rateName, String roomType, String rateType, String newRoomType);

    public void updateRateType(String rateName, String roomType, String rateType, RoomRateTypeEnum rateTypeEnum);

    public void updateRateAmount(String rateName, String roomType, String rateType, BigDecimal newAmount);

    public void updateStartDate(String rateName, String roomType, String rateType, LocalDate newDate);

    public void updateEndDate(String rateName, String roomType, String rateType, LocalDate newDate);

    public void deleteRoomRate(String rateName);

    public List<RoomRate> retrieveAllRoomRates();

    public RoomRate retrieveRoomRateByName(String rateName);


}
