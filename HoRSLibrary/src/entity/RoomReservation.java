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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author kevinlim
 */
@Entity
public class RoomReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomReservationId;
    
        
    @ManyToOne
    private Room room;

    @ManyToOne
    private Reservation reservation;

    @OneToOne (mappedBy = "roomReservation")
    private ExceptionReport exceptionReport;

    public RoomReservation() {
    }

    public RoomReservation(Room room, Reservation reservation) {
        this.room = room;
        this.reservation = reservation;
    }
    

    public RoomReservation(Room room, Reservation reservation, ExceptionReport exceptionReport) {
        this.room = room;
        this.reservation = reservation;
        this.exceptionReport = exceptionReport;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public ExceptionReport getExceptionReport() {
        return exceptionReport;
    }

    public void setExceptionReport(ExceptionReport exceptionReport) {
        this.exceptionReport = exceptionReport;
    }


    public Long getRoomReservationId() {
        return roomReservationId;
    }

    public void setRoomReservationId(Long roomReservationId) {
        this.roomReservationId = roomReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomReservationId != null ? roomReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomReservationId fields are not set
        if (!(object instanceof RoomReservation)) {
            return false;
        }
        RoomReservation other = (RoomReservation) object;
        if ((this.roomReservationId == null && other.roomReservationId != null) || (this.roomReservationId != null && !this.roomReservationId.equals(other.roomReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomReservation[ id=" + roomReservationId + " ]";
    }
    
}
