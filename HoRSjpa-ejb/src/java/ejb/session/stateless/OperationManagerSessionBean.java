/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Room;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.RoomStatusEnum;
import util.RoomTypeStatusEnum;

/**
 *
 * @author jamiewee
 */
@Stateless
public class OperationManagerSessionBean implements OperationManagerSessionBeanRemote, OperationManagerSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;
    
        
    @Override
    public RoomType retrieveRoomTypeByName(String typeName) {
        Query q = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.typeName = :typeName ");
        q.setParameter("typeName", typeName);
        return (RoomType) q.getSingleResult();
    }
    
    @Override
    public Room retrieveRoomByNumber(String roomNum) {
        Query q = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNum");
        q.setParameter("roomNum", roomNum);
        return (Room) q.getSingleResult();
    }
    

    @Override
    public Long createNewRoomType(RoomType rt) {
        em.persist(rt);
        em.flush();
        return rt.getRoomTypeID();
    }

    @Override
    public RoomType viewRoomTypeDetails(String typeName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void deleteRoomType(String typeName) {
        
        // check if it is in use
        
        RoomType roomType = retrieveRoomTypeByName(typeName);
        em.remove(roomType);
    }
    
    @Override
   public void updateTypeName(String typeName, String newTypeName) {
        RoomType roomType = retrieveRoomTypeByName(typeName);
        roomType.setTypeName(newTypeName);
        em.merge(roomType);
    }

    @Override
    public void updateDescription(String typeName, String newDescription) {
        RoomType roomType = retrieveRoomTypeByName(typeName);
        roomType.setDescription(newDescription);
        em.merge(roomType);
    }

    @Override
    public void updateSize(String typeName, BigDecimal newSize) {
        RoomType roomType = retrieveRoomTypeByName(typeName);
        roomType.setSize(newSize);
        em.merge(roomType);
    }

    @Override
    public void updateBed(String typeName, String newBed) {
        RoomType roomType = retrieveRoomTypeByName(typeName);
        roomType.setBed(newBed);
        em.merge(roomType);
    }

    @Override
    public void updateCapacity(String typeName, Long newCapacity) {
        RoomType roomType = retrieveRoomTypeByName(typeName);
        roomType.setCapacity(newCapacity);
        em.merge(roomType);
    }

    @Override
    public void updateAmenities(String typeName, String newAmenities) {
        RoomType roomType = retrieveRoomTypeByName(typeName);
        roomType.setAmenities(newAmenities);
        em.merge(roomType);
    }

    @Override
    public void updateRoomTypeStatus(String typeName, RoomTypeStatusEnum newRoomTypeStatus) {
        RoomType roomType = retrieveRoomTypeByName(typeName);
        roomType.setRoomTypeStatus(newRoomTypeStatus);
        em.merge(roomType);
    }

    @Override
    public void updateTierNumber(String typeName, Integer newTierNumber) {
        RoomType roomType = retrieveRoomTypeByName(typeName);
        roomType.setTierNumber(newTierNumber);
        em.merge(roomType);
    }

    @Override
    public void updateInventory(String typeName, Long newInventory) {
        RoomType roomType = retrieveRoomTypeByName(typeName);
        roomType.setInventory(newInventory);
        em.merge(roomType);
    }

    
    @Override
    public Long createNewRoom(Room r) {
        em.persist(r);
        em.flush();
        return r.getRoomID();
    }
    
    
    @Override
    public void deleteRoom(String roomNumber ) {
        
        // check if in use
        
        Room room = retrieveRoomByNumber(roomNumber);
        em.remove(room);
    }
    
    @Override
    public boolean roomIsInUse(Room r) {
        return true;
    }
    
    @Override
    public boolean roomTypeIsInUse(RoomType rt) {
        return true;
    }
    
    @Override
    public void updateRoomNumber(String currentRoomNumber, String newRoomNumber) {
    Room room = retrieveRoomByNumber(currentRoomNumber);
    room.setRoomNumber(newRoomNumber);
    em.merge(room);
    }

    @Override
    public void updateRoomStatus(String roomNumber, RoomStatusEnum newRoomStatus) {
        Room room = retrieveRoomByNumber(roomNumber);
        room.setRoomStatus(newRoomStatus);
        em.merge(room);
    }


    @Override
    public List<Room> retrieveAllRooms() {
        return em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }
    
    @Override
    public List<RoomType> retrieveAllRoomTypes() {
        return em.createQuery("SELECT rt FROM RoomType rt", RoomType.class).getResultList();
    }
    
    @Override
    public List<ExceptionReport> retrieveExceptionReportsByDate(LocalDate date) {
        return em.createQuery("SELECT e FROM ExceptionReport e WHERE e.creationDate = :date", ExceptionReport.class)
                 .setParameter("date", date)
                 .getResultList();
    }
    
    @Override
    public ExceptionReport retrieveExceptionReportByID(Long exceptionReportID) {
        return em.find(ExceptionReport.class, exceptionReportID);
    }    
}
