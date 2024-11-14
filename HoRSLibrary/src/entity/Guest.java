/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author kevinlim
 */
@Entity
public class Guest extends Visitor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestID;
    
    @Column (length = 32, nullable = false)
    private String firstName;
    
    @Column(length = 32, nullable = true)
    private String lastName;
    
    @Column(length = 64, nullable = false, unique = true) 
    private String email;
    
    @Column(length = 20, nullable = true)
    private String phoneNumber;
    
    @Column(length = 20, nullable = true, unique = true)
    private String passportNumber;
    
    @Column(length = 32, nullable = true, unique = true)
    private String username;
    
    @Column(length = 64, nullable = true)
    private String password;
    
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<Reservation>();

    public Guest(String firstName, String lastName, String email, String phoneNumber, String passportNumber, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
        this.username = username;
        this.password = password;
    }
    
    public Guest(String firstName, String email) {
        this.firstName = firstName;
        this.email = email;
    }

    public Guest(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Guest() {
        
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getGuestID() {
        return guestID;
    }

    public void setGuestID(Long guestID) {
        this.guestID = guestID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (guestID != null ? guestID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the guestID fields are not set
        if (!(object instanceof Guest)) {
            return false;
        }
        Guest other = (Guest) object;
        if ((this.guestID == null && other.guestID != null) || (this.guestID != null && !this.guestID.equals(other.guestID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Guest[ id=" + guestID + " ]";
    }
    
}
