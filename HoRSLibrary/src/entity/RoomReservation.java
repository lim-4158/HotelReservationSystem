/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author kevinlim
 */
@Entity
public class RoomReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomReservationID;

    public Long getRoomReservationID() {
        return roomReservationID;
    }

    public void setRoomReservationID(Long roomReservationID) {
        this.roomReservationID = roomReservationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomReservationID != null ? roomReservationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomReservationID fields are not set
        if (!(object instanceof RoomReservation)) {
            return false;
        }
        RoomReservation other = (RoomReservation) object;
        if ((this.roomReservationID == null && other.roomReservationID != null) || (this.roomReservationID != null && !this.roomReservationID.equals(other.roomReservationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomReservation[ id=" + roomReservationID + " ]";
    }
    
}
