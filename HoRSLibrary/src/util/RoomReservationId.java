/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author kevinlim
 */
import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class RoomReservationId implements Serializable {

    private Long roomId;
    private Long reservationId;

    // Default constructor
    public RoomReservationId() {}

    // Parameterized constructor
    public RoomReservationId(Long roomId, Long reservationId) {
        this.roomId = roomId;
        this.reservationId = reservationId;
    }

    // Getters and Setters
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    // equals method (used for comparing instances in JPA)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomReservationId that = (RoomReservationId) o;
        return roomId.equals(that.roomId) && reservationId.equals(that.reservationId);
    }
    
    @Override
    public String toString() {
        return "RoomReservationId{" +
                "roomId=" + roomId +
                ", reservationId=" + reservationId +
                '}';
    }

    // No hashCode method as per your request
}
