/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Room;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.RoomStatusEnum;
import util.RoomTypeStatusEnum;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author jamiewee
 */
@Local
public interface OperationManagerSessionBeanLocal {
  
    // Retrieving By Name / Number Methods
    public RoomType retrieveRoomTypeByName(String typeName) throws RoomTypeNotFoundException;
    public Room retrieveRoomByNumber(String roomNum) throws RoomNotFoundException;
    
    // Retrieving By Id Methods
    public Room retrieveRoomById(Long roomId) throws RoomNotFoundException;
    public RoomType retrieveRoomTypeByID(Long roomTypeId) throws RoomTypeNotFoundException;

    // CRUD Room Type Methods
    public Long createNewRoomType(RoomType rt);
    public void deleteRoomType(Long roomTypeID) throws RoomTypeNotFoundException;
    public void updateTypeName(Long roomTypeID, String newTypeName) throws RoomTypeNotFoundException;
    public void updateDescription(Long roomTypeID, String newDescription) throws RoomTypeNotFoundException;
    public void updateSize(Long roomTypeID, BigDecimal newSize) throws RoomTypeNotFoundException;
    public void updateBed(Long roomTypeID, String newBed) throws RoomTypeNotFoundException;
    public void updateCapacity(Long roomTypeID, Long newCapacity) throws RoomTypeNotFoundException;
    public void updateAmenities(Long roomTypeID, String newAmenities) throws RoomTypeNotFoundException;
    public void updateRoomTypeStatus(Long roomTypeID, RoomTypeStatusEnum newRoomTypeStatus) throws RoomTypeNotFoundException;
    public void updateTierNumber(Long roomTypeID, Integer newTierNumber) throws RoomTypeNotFoundException;
    public void updateInventory(Long roomTypeID, Long newInventory) throws RoomTypeNotFoundException;

    // CRUD Room Methods
    public Long createNewRoom(Long roomTypeId, Room r) throws RoomTypeNotFoundException;
    public void deleteRoom(Long roomID) throws RoomNotFoundException;
    public void updateRoomNumber(Long roomID, String newRoomNumber) throws RoomNotFoundException; 
    public void updateRoomStatus(Long roomID, RoomStatusEnum newRoomStatus) throws RoomNotFoundException;
    public void updateRoomType(Long roomID, RoomType newRoomType) throws RoomNotFoundException;

    // Retrieving All Records Methods
    public List<Room> retrieveAllRooms();
    public List<RoomType> retrieveAllRoomTypes();

    // Exception Report Methods
    public List<ExceptionReport> retrieveExceptionReportsByDate(LocalDate date);
    public ExceptionReport retrieveExceptionReportByID(Long exceptionReportID);

    // Helper Methods
    public boolean roomIsInUse(Room room);
    public boolean roomTypeIsInUse(RoomType roomType);
  
}
