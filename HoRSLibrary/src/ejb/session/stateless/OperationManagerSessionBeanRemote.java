/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Room;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.RoomStatusEnum;
import util.RoomTypeStatusEnum;

/**
 *
 * @author jamiewee
 */
@Remote
public interface OperationManagerSessionBeanRemote {
    
    public RoomType retrieveRoomTypeByName(String typeName);

    public Room retrieveRoomByNumber(String roomNum);

    public void deleteRoomType(String typeName);

    public void updateTypeName(String typeName, String newTypeName);

    public void updateDescription(String typeName, String newDescription);

    public void updateSize(String typeName, BigDecimal newSize);

    public void updateBed(String typeName, String newBed);

    public void updateCapacity(String typeName, Long newCapacity);

    public void updateAmenities(String typeName, String newAmenities);

    public void updateRoomTypeStatus(String typeName, RoomTypeStatusEnum newRoomTypeStatus);

    public void updateTierNumber(String typeName, Integer newTierNumber);

    public void updateInventory(String typeName, Long newInventory);

    public Long createNewRoom(Room r);

    public void deleteRoom(String roomNumber);

    public void updateRoomNumber(String currentRoomNumber, String newRoomNumber);

    public void updateRoomStatus(String roomNumber, RoomStatusEnum newRoomStatus);

    public List<Room> retrieveAllRooms();

    public List<RoomType> retrieveAllRoomTypes();

    public List<ExceptionReport> retrieveExceptionReportsByDate(LocalDate date);

    public ExceptionReport retrieveExceptionReportByID(Long exceptionReportID);
}
