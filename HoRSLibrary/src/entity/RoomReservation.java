/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.RoomReservationId;

/**
 *
 * @author kevinlim
 */

@Entity
public class RoomReservation implements Serializable {

    @EmbeddedId
    private RoomReservationId id;
    
    @ManyToOne
    private Room room;

    @ManyToOne
    private Reservation reservation;

    @OneToOne (mappedBy = "roomReservation")
    private ExceptionReport exceptionReport;
    

    // Default constructor
    public RoomReservation() {}

    // Parameterized constructor
    public RoomReservation(Room room, Reservation reservation) {
        this.room = room;
        this.reservation = reservation;
        this.id = new RoomReservationId(room.getRoomID(), reservation.getReservationID());
    }

    // Getter and Setter for id
    public RoomReservationId getId() {
        return id;
    }

    public void setId(RoomReservationId id) {
        this.id = id;
    }

    // toString method
    @Override
    public String toString() {
        return "RoomReservation{" +
                "id=" + id +
                '}';
    }

    // equals method (used for comparing instances in JPA)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomReservation that = (RoomReservation) o;
        return id.equals(that.id);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * @return the reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * @return the exceptionReport
     */
    public ExceptionReport getExceptionReport() {
        return exceptionReport;
    }

    /**
     * @param exceptionReport the exceptionReport to set
     */
    public void setExceptionReport(ExceptionReport exceptionReport) {
        this.exceptionReport = exceptionReport;
    }
}