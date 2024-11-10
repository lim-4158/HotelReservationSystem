/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import util.RoomTypeStatusEnum;

/**
 *
 * @author kevinlim
 */
@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeID;
    private String typeName;
    private String description;
    private BigDecimal size;
    private String bed;
    private Long capacity;
    private String amenities;
    private RoomTypeStatusEnum roomTypeStatus;
    private Integer tierNumber;
    private Long inventory;
    @OneToMany(mappedBy =  "roomType", fetch = FetchType.EAGER)
    private List<Room> rooms = new ArrayList<Room>(); 
    @OneToMany (mappedBy = "roomType", fetch = FetchType.EAGER)
    private List<Reservation> reservations;
    @OneToMany(mappedBy = "roomType", fetch = FetchType.EAGER)
    private List<RoomRate> roomRates; 

    public RoomType() {
    }

    public RoomType(String typeName, String description, BigDecimal size, String bed, Long capacity, String amenities, RoomTypeStatusEnum roomTypeStatus, Integer tierNumber, Long inventory) {
        this.typeName = typeName;
        this.description = description;
        this.size = size;
        this.bed = bed;
        this.capacity = capacity;
        this.amenities = amenities;
        this.roomTypeStatus = roomTypeStatus;
        this.tierNumber = tierNumber;
        this.inventory = inventory;
    }
    
    public Long getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(Long roomTypeID) {
        this.roomTypeID = roomTypeID;
    }    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeID != null ? roomTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeID fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeID == null && other.roomTypeID != null) || (this.roomTypeID != null && !this.roomTypeID.equals(other.roomTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeID + " ]";
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the size
     */
    public BigDecimal getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(BigDecimal size) {
        this.size = size;
    }

    /**
     * @return the bed
     */
    public String getBed() {
        return bed;
    }

    /**
     * @param bed the bed to set
     */
    public void setBed(String bed) {
        this.bed = bed;
    }

    /**
     * @return the capacity
     */
    public Long getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the amenities
     */
    public String getAmenities() {
        return amenities;
    }

    /**
     * @param amenities the amenities to set
     */
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    /**
     * @return the roomTypeStatus
     */
    public RoomTypeStatusEnum getRoomTypeStatus() {
        return roomTypeStatus;
    }

    /**
     * @param roomTypeStatus the roomTypeStatus to set
     */
    public void setRoomTypeStatus(RoomTypeStatusEnum roomTypeStatus) {
        this.roomTypeStatus = roomTypeStatus;
    }

    /**
     * @return the tierNumber
     */
    public Integer getTierNumber() {
        return tierNumber;
    }

    /**
     * @param tierNumber the tierNumber to set
     */
    public void setTierNumber(Integer tierNumber) {
        this.tierNumber = tierNumber;
    }

    /**
     * @return the inventory
     */
    public Long getInventory() {
        return inventory;
    }

    /**
     * @param inventory the inventory to set
     */
    public void setInventory(Long inventory) {
        this.inventory = inventory;
    }

    /**
     * @return the rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * @param rooms the rooms to set
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * @return the roomRate
     */
    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

    /**
     * @param roomRate the roomRate to set
     */
    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }
    
}
