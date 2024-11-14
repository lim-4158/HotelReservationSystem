/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import util.ReservationTypeEnum;

/**
 *
 * @author kevinlim
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationID;
    
    @Column(nullable = false)
    private LocalDate reservationDate;
    
    @Column(nullable = false)
    private LocalDate checkInDate;
    
    @Column(nullable = false)
    private LocalDate checkOutDate;
    private BigDecimal totalAmount;
    private ReservationTypeEnum reservationType;
    
    @Column(nullable = false)
    private int numberOfRooms; 
    
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private Guest guest;
    
    @ManyToOne(optional = true)
    private Partner partner;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;

    public Reservation() {
    }

    public Reservation(LocalDate reservationDate, LocalDate checkInDate, LocalDate checkOutDate, ReservationTypeEnum reservationType, Guest guest, RoomType roomType, int numberOfRooms) {
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservationType = reservationType;
        this.guest = guest;
        this.roomType = roomType; 
        this.numberOfRooms = numberOfRooms;
    }   

    public Reservation(LocalDate reservationDate, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalAmount, ReservationTypeEnum reservationType, int numberOfRooms, Guest guest, RoomType roomType) {
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.reservationType = reservationType;
        this.numberOfRooms = numberOfRooms;
        this.guest = guest;
        this.roomType = roomType;
    }    
    
    public Reservation(LocalDate reservationDate, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalAmount, ReservationTypeEnum reservationType, int numberOfRooms, Partner partner, RoomType roomType, Guest guest) {
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.reservationType = reservationType;
        this.numberOfRooms = numberOfRooms;
        this.partner = partner;
        this.roomType = roomType;
        this.guest = guest;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Long getReservationID() {
        return reservationID;
    }

    public void setReservationID(Long reservationID) {
        this.reservationID = reservationID;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ReservationTypeEnum getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationTypeEnum reservationType) {
        this.reservationType = reservationType;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationID != null ? reservationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationID fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationID == null && other.reservationID != null) || (this.reservationID != null && !this.reservationID.equals(other.reservationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationID + " + guestname = " + guest.getFirstName() + " + roomtype " + roomType + " " + numberOfRooms +  "]" ;
    }
    
}