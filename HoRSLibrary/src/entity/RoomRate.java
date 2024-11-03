/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.RoomRateTypeEnum;

/**
 *
 * @author kevinlim
 */
@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateID;
    private String roomRateName;
    private String roomType;
    private RoomRateTypeEnum rateType;
    private BigDecimal nightlyRateAmount; 
    private LocalDate startDate;
    private LocalDate endDate;

    public Long getRoomRateID() {
        return roomRateID;
    }

    public void setRoomRateID(Long roomRateID) {
        this.roomRateID = roomRateID;
    }

    public RoomRate() {
        
    }
    
    public RoomRate(String roomRateName, String roomType, RoomRateTypeEnum rateType, BigDecimal nightlyRateAmount, LocalDate startDate, LocalDate endDate) {
        this.roomRateName = roomRateName;
        this.roomType = roomType;
        this.rateType = rateType;
        this.nightlyRateAmount = nightlyRateAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateID != null ? roomRateID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateID fields are not set
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.roomRateID == null && other.roomRateID != null) || (this.roomRateID != null && !this.roomRateID.equals(other.roomRateID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ id=" + roomRateID + " ]";
    }

    /**
     * @return the roomRateName
     */
    public String getRoomRateName() {
        return roomRateName;
    }

    /**
     * @param roomRateName the roomRateName to set
     */
    public void setRoomRateName(String roomRateName) {
        this.roomRateName = roomRateName;
    }

    /**
     * @return the roomType
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the rateType
     */
    public RoomRateTypeEnum getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(RoomRateTypeEnum rateType) {
        this.rateType = rateType;
    }

    /**
     * @return the nightlyRateAmount
     */
    public BigDecimal getNightlyRateAmount() {
        return nightlyRateAmount;
    }

    /**
     * @param nightlyRateAmount the nightlyRateAmount to set
     */
    public void setNightlyRateAmount(BigDecimal nightlyRateAmount) {
        this.nightlyRateAmount = nightlyRateAmount;
    }

    /**
     * @return the startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
}
