/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import util.EmployeeRoleEnum;
import util.RoomRateTypeEnum;
import util.RoomStatusEnum;

/**
 *
 * @author kevinlim
 */
@Singleton
@LocalBean
@Startup
public class TestDataInitSessionBean {

    @PersistenceContext(unitName = "HoRSjpa-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct(){
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class);
        Long count = query.getSingleResult();
        
        if (count == 0) {
            Employee sysadmin = new Employee("sysadmin", "password", EmployeeRoleEnum.SYSTEMADMIN);
            Employee opManager = new Employee("opmanager", "password", EmployeeRoleEnum.OPERATIONSMANAGER);
            Employee salesManager = new Employee("salesmanager", "password", EmployeeRoleEnum.SALESMANAGER);
            Employee guestRelOfficer = new Employee("guestrelo", "password", EmployeeRoleEnum.GUESTRELATIONOFFIER);

            em.persist(sysadmin);
            em.persist(opManager);
            em.persist(salesManager);
            em.persist(guestRelOfficer);

            RoomType grandSuite = new RoomType("Grand Suite", 1);
            RoomType juniorSuite = new RoomType("Junior Suite", 2);
            RoomType familyRoom = new RoomType("Family Room", 3);
            RoomType premierRoom = new RoomType("Premier Room", 4);
            RoomType deluxeRoom = new RoomType("Deluxe Room", 5);

            em.persist(deluxeRoom);
            em.persist(premierRoom);
            em.persist(familyRoom);
            em.persist(juniorSuite);
            em.persist(grandSuite);

            em.persist(new RoomRate("Deluxe Room Published", deluxeRoom, RoomRateTypeEnum.PUBLISHED, new BigDecimal("100")));
            em.persist(new RoomRate("Deluxe Room Normal", deluxeRoom, RoomRateTypeEnum.NORMAL, new BigDecimal("50")));
            em.persist(new RoomRate("Premier Room Published", premierRoom, RoomRateTypeEnum.PUBLISHED, new BigDecimal("200")));
            em.persist(new RoomRate("Premier Room Normal", premierRoom, RoomRateTypeEnum.NORMAL, new BigDecimal("100")));
            em.persist(new RoomRate("Family Room Published", familyRoom, RoomRateTypeEnum.PUBLISHED, new BigDecimal("300")));
            em.persist(new RoomRate("Family Room Normal", familyRoom, RoomRateTypeEnum.NORMAL, new BigDecimal("150")));
            em.persist(new RoomRate("Junior Suite Published", juniorSuite, RoomRateTypeEnum.PUBLISHED, new BigDecimal("400")));
            em.persist(new RoomRate("Junior Suite Normal", juniorSuite, RoomRateTypeEnum.NORMAL, new BigDecimal("200")));
            em.persist(new RoomRate("Grand Suite Published", grandSuite, RoomRateTypeEnum.PUBLISHED, new BigDecimal("500")));
            em.persist(new RoomRate("Grand Suite Normal", grandSuite, RoomRateTypeEnum.NORMAL, new BigDecimal("250")));

            // Creating Deluxe Room instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(deluxeRoom, "0" + i + "01", RoomStatusEnum.AVAILABLE));
            }

            // Creating Premier Room instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(premierRoom, "0" + i + "02", RoomStatusEnum.AVAILABLE));
            }

            // Creating Family Room instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(familyRoom, "0" + i + "03", RoomStatusEnum.AVAILABLE));
            }

            // Creating Junior Suite instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(juniorSuite, "0" + i + "04", RoomStatusEnum.AVAILABLE));
            }

            // Creating Grand Suite instances
            for (int i = 1; i <= 5; i++) {
                em.persist(new Room(grandSuite, "0" + i + "05", RoomStatusEnum.AVAILABLE));
            }            
            
        }
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
  
    
}
