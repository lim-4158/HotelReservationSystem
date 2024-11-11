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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.RoomStatusEnum;
import util.RoomTypeStatusEnum;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author jamiewee
 */
@Stateless
public class OperationManagerSessionBean implements OperationManagerSessionBeanRemote, OperationManagerSessionBeanLocal {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;
    
        
    // Retrieving By Id Methods
    @Override
    public RoomType retrieveRoomTypeByID(Long roomTypeId) throws RoomTypeNotFoundException {
        try {
            Query q = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.roomTypeID = :roomTypeId");
            q.setParameter("roomTypeId", roomTypeId);
            return (RoomType) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("NO ROOM TYPE FOUND");
            throw new RoomTypeNotFoundException("No room type found with ID: " + roomTypeId);
        }
    }

    @Override
    public Room retrieveRoomById(Long roomId) throws RoomNotFoundException {
        try {
            Query q = em.createQuery("SELECT r FROM Room r WHERE r.roomID = :roomId");
            q.setParameter("roomId", roomId);
            return (Room) q.getSingleResult();
        } catch (NoResultException e) {
            throw new RoomNotFoundException("No room found with ID: " + roomId);
        }
    }
    
    // Retrieving By Name / Number Methods
    @Override
    public RoomType retrieveRoomTypeByName(String typeName) throws RoomTypeNotFoundException {
        try {
            Query q = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.typeName = :typeName ");
            q.setParameter("typeName", typeName);
            return (RoomType) q.getSingleResult();
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with the name: " + typeName);
        }
    }
    
    @Override
    public Room retrieveRoomByNumber(String roomNum) throws RoomNotFoundException {
        try {
            Query q = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNum");
            q.setParameter("roomNum", roomNum);
            return (Room) q.getSingleResult();
        } catch (NoResultException e) {
            throw new RoomNotFoundException("No Room Found.");
        }
    }
    
    // CRUD Room Type Methods
    @Override
    public Long createNewRoomType(RoomType rt) {
        em.persist(rt);
        em.flush();
        return rt.getRoomTypeID();
    }
    
    @Override
    public void deleteRoomType(Long roomTypeID) throws RoomTypeNotFoundException {
        
        // get input date
        LocalDate currentDate = LocalDate.now();
        
        RoomType roomType = retrieveRoomTypeByID(roomTypeID);
        
       if (roomTypeIsInUse(roomType, currentDate)) {
           System.out.println("entered here");
           roomType.setRoomTypeStatus(RoomTypeStatusEnum.DISABLED);
           em.merge(roomType);
       } else {
           em.remove(roomType);
           System.out.println("it has entered here yayayayay");
           
       }
        
    }

    @Override
    public void updateTypeName(Long roomTypeID, String newTypeName) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = retrieveRoomTypeByID(roomTypeID);
            roomType.setTypeName(newTypeName);
            em.merge(roomType);
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with that name.");
        }
        
    }

    @Override
    public void updateDescription(Long roomTypeID, String newDescription) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = retrieveRoomTypeByID(roomTypeID);
            roomType.setDescription(newDescription);
            em.merge(roomType);
        }
        catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with that name.");
        }
    }

    @Override
    public void updateSize(Long roomTypeID, BigDecimal newSize) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = retrieveRoomTypeByID(roomTypeID);
            roomType.setSize(newSize);
            em.merge(roomType);
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with the specified ID.");
        }
    }

    @Override
    public void updateBed(Long roomTypeID, String newBed) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = retrieveRoomTypeByID(roomTypeID);
            roomType.setBed(newBed);
            em.merge(roomType);
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with the specified ID.");
        }
    }

    @Override
    public void updateCapacity(Long roomTypeID, Long newCapacity) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = retrieveRoomTypeByID(roomTypeID);
            roomType.setCapacity(newCapacity);
            em.merge(roomType);
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with the specified ID.");
        }
    }

    @Override
    public void updateAmenities(Long roomTypeID, String newAmenities) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = retrieveRoomTypeByID(roomTypeID);
            roomType.setAmenities(newAmenities);
            em.merge(roomType);
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with the specified ID.");
        }
    }

    @Override
    public void updateRoomTypeStatus(Long roomTypeID, RoomTypeStatusEnum newRoomTypeStatus) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = retrieveRoomTypeByID(roomTypeID);
            roomType.setRoomTypeStatus(newRoomTypeStatus);
            em.merge(roomType);
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with the specified ID.");
        }
    }

    @Override
    public void updateTierNumber(Long roomTypeID, Integer newTierNumber) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = retrieveRoomTypeByID(roomTypeID);
            roomType.setTierNumber(newTierNumber);
            em.merge(roomType);
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with the specified ID.");
        }
    }

    @Override
    public void updateInventory(Long roomTypeID, Long newInventory) throws RoomTypeNotFoundException {
        try {
            RoomType roomType = retrieveRoomTypeByID(roomTypeID);
            roomType.setInventory(newInventory);
            em.merge(roomType);
        } catch (NoResultException e) {
            throw new RoomTypeNotFoundException("No room type found with the specified ID.");
        }
    }

    // CRUD Room Methods
    @Override
    public Long createNewRoom(Room r) {
        em.persist(r);
        em.flush();
        return r.getRoomID();
    }
       
    @Override
    public void deleteRoom(Long roomID) throws RoomNotFoundException {
        
        try {
            Room r = retrieveRoomById(roomID);
        
            if (!roomIsInUse(r, LocalDate.now())){
                r.getRoomType().getRooms().remove(r);
                em.merge(r);
            } else {
                r.setIsDisabled(true);

            }

            Room room = retrieveRoomById(roomID);
            em.remove(room);
        } catch (NoResultException e) {
            throw new RoomNotFoundException("no room found");
        }
    }
    
    @Override
    public void updatePartnerName(Long roomID, String newPartnerName) throws RoomNotFoundException {
        try {
            Room room = retrieveRoomById(roomID);
            room.setPartnerName(newPartnerName);
            em.merge(room);
        } catch (Exception e) {
            throw new RoomNotFoundException("room not found");
        }
    }
   
    @Override
    public void updateRoomNumber(Long roomID, String newRoomNumber) throws RoomNotFoundException {
        try {
            Room room = retrieveRoomById(roomID);
            room.setRoomNumber(newRoomNumber);
            em.merge(room);
        } catch (Exception e) {
            throw new RoomNotFoundException("room not found");
        }
    }

    @Override
    public void updateRoomStatus(Long roomID, RoomStatusEnum newRoomStatus) throws RoomNotFoundException {
        try {
            Room room = retrieveRoomById(roomID);
            room.setRoomStatus(newRoomStatus);
            em.merge(room);
        } catch (Exception e) {
            throw new RoomNotFoundException("room not found");
        } 
    }

    @Override
    public void updateRoomType(Long roomID, RoomType newRoomType) throws RoomNotFoundException {
        try {
            Room room = retrieveRoomById(roomID);
            room.setRoomType(newRoomType);
            em.merge(room);
        } catch (Exception e) {
            throw new RoomNotFoundException("room not found");
        }
    }

    // retrieving all rooms / roomtypes
    @Override
    public List<Room> retrieveAllRooms() {
        return em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }
    
    @Override
    public List<RoomType> retrieveAllRoomTypes() {
        return em.createQuery("SELECT rt FROM RoomType rt", RoomType.class).getResultList();
    }
    
    // exception report methods
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
    
    // helper methods
    public boolean roomIsInUse(Room room, LocalDate inputDate) {
        Query query = em.createQuery(
            "SELECT COUNT(rr) " +
            "FROM RoomReservation rr " +
            "JOIN rr.reservation res " +
            "WHERE rr.room = :room " +
            "AND res.checkOutDate > :inputDate"
        );
        query.setParameter("room", room);
        query.setParameter("inputDate", inputDate);

        Long count = (Long) query.getSingleResult();
        return count > 0;
    }
    
    public boolean roomTypeIsInUse(RoomType roomType, LocalDate inputDate) {
    // Check if the room type has no associated rooms
        List<Room> rooms = roomType.getRooms();
        if (rooms == null || rooms.isEmpty()) {
            return false; // Room type is not in use if it has no rooms
        }

        return true;
    }
}
