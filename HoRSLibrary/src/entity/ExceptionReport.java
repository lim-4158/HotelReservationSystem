/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import util.ExceptionReportTypeEnum;

/**
 *
 * @author kevinlim
 */
@Entity
public class ExceptionReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportID;
    private Long reservationID;
    
    @OneToOne 
    private RoomReservation roomReservation;
    
    private ExceptionReportTypeEnum reportType;
    private LocalDate creationDate;

    public ExceptionReport() {
    }

    public ExceptionReport(RoomReservation roomReservation, ExceptionReportTypeEnum reportType, LocalDate creationDate, Long reservationID) {
        this.roomReservation = roomReservation;
        this.reportType = reportType;
        this.creationDate = creationDate;
        this.reservationID = reservationID;
    }

    public ExceptionReport(ExceptionReportTypeEnum reportType, LocalDate creationDate, Long reservationID) {
        this.reportType = reportType;
        this.creationDate = creationDate;
        this.reservationID = reservationID;
    }

    public Long getReservationID() {
        return reservationID;
    }

    public void setReservationID(Long reservationID) {
        this.reservationID = reservationID;
    }

    public Long getReportID() {
        return reportID;
    }

    public void setReportID(Long reportID) {
        this.reportID = reportID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportID != null ? reportID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reportID fields are not set
        if (!(object instanceof ExceptionReport)) {
            return false;
        }
        ExceptionReport other = (ExceptionReport) object;
        if ((this.reportID == null && other.reportID != null) || (this.reportID != null && !this.reportID.equals(other.reportID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ExceptionReport[ id=" + reportID + " ]";
    }
    
}
