/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import javax.ejb.Remote;

/**
 *
 * @author jamiewee
 */
@Remote
public interface OperationManagerSessionBeanRemote {
    public Long createNewRoomType(RoomType rt); 
    public RoomType viewRoomTypeDetails(String typeName);
}
