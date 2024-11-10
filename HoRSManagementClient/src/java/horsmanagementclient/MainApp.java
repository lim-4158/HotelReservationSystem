/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestRelationOfficerSessionBeanRemote;
import ejb.session.stateless.OperationManagerSessionBeanRemote;
import ejb.session.stateless.SalesManagerSessionBeanRemote;
import ejb.session.stateless.SystemAdminSessionBeanRemote;
import entity.Employee;
import entity.Partner;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import util.EmployeeRoleEnum;
import util.RoomRateTypeEnum;

/**
 *
 * @author kevinlim
 */
public class MainApp {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote; 
    private GuestRelationOfficerSessionBeanRemote guestRelationOfficerSessionBeanRemote;
    private OperationManagerSessionBeanRemote operationManagerSessionBeanRemote;
    private SalesManagerSessionBeanRemote salesManagerSessionBeanRemote;
    private SystemAdminSessionBeanRemote systemAdminSessionBeanRemote;

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, GuestRelationOfficerSessionBeanRemote guestRelationOfficerSessionBeanRemote, OperationManagerSessionBeanRemote operationManagerSessionBeanRemote, SalesManagerSessionBeanRemote salesManagerSessionBeanRemote, SystemAdminSessionBeanRemote systemAdminSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.guestRelationOfficerSessionBeanRemote = guestRelationOfficerSessionBeanRemote;
        this.operationManagerSessionBeanRemote = operationManagerSessionBeanRemote;
        this.salesManagerSessionBeanRemote = salesManagerSessionBeanRemote;
        this.systemAdminSessionBeanRemote = systemAdminSessionBeanRemote;
    }
    
    public void runApp() {
        
        Scanner scanner = new Scanner(System.in);
        
        while(true) {
            System.out.println("*** Welcome to HoRS Management Client ***\n");
            System.out.println("Log In Portal");
            
            System.out.print("Username : ");
            String username = scanner.nextLine(); 
            System.out.print("Password : ");
            String password = scanner.nextLine(); 
            
            Employee employee = employeeSessionBeanRemote.employeeLogin(username, password);
            
            if (employee.getRole().equals(EmployeeRoleEnum.GUESTRELATIONOFFIER)) {
                guestRelationOfficerView();
                
            } else if (employee.getRole().equals(EmployeeRoleEnum.OPERATIONSMANAGER)) {
                operationsManagerView();

            } else if (employee.getRole().equals(EmployeeRoleEnum.SALESMANAGER)) {
                salesManagerView();
            
            } else if (employee.getRole().equals(EmployeeRoleEnum.SYSTEMADMIN)) {
               systemAdminView();
            }                         
        }                
    }
    
    public void guestRelationOfficerView() {
        System.out.println("Your role : Guest Relation Officer \n");
        System.out.println("1: Walk-in search room");
        System.out.println("2: Walk-in reserve room");
        System.out.println("3: Check-in guest");
        System.out.println("4: Check-out guest");
                
    }
    
    public void operationsManagerView() {
        System.out.println("Your role : Operations Manager \n");
        System.out.println("1: Create new room type");
        System.out.println("2: View room type details");
        System.out.println("3: Update room type");
        System.out.println("4: Delete room type");
        System.out.println("5: View all room type");
        System.out.println("6: Create new room");
        System.out.println("7: Update room");
        System.out.println("8: Delete room");
        System.out.println("9: View all rooms");
        System.out.println("10: View room allocation exceptioin report");         
    }
    
    public void salesManagerView() {
        System.out.println("Your role : Sales Manager \n");

        Scanner sc = new Scanner(System.in); 
        int response;
        
        while (true) {
            System.out.println("1: Create new room rate");
            System.out.println("2: View room rate details");
            System.out.println("3: Update room rate");
            System.out.println("4: Delete room rate");
            System.out.println("5: View all room rate");
        
            response = sc.nextInt(); 
            sc.nextLine(); 
            
            if (response == 1){
                
                // create new room rate
                System.out.println("Enter Rate Name: ");
                String rateName = sc.nextLine(); 
                System.out.println("Enter Rate type: ");
                RoomRateTypeEnum rrtEnum = selectEnum(RoomRateTypeEnum.class); 
                System.out.println("Enter nightly rate: ");
                BigDecimal nightlyRate = sc.nextBigDecimal(); 
                System.out.println("Enter start date");
                
                System.out.print("Enter start date (yyyy-MM-dd): ");
                LocalDate startDate = getInputDate();                  
                System.out.print("Enter end date (yyyy-MM-dd): ");
                LocalDate endDate = getInputDate();      
                                
                RoomRate rate = new RoomRate(rateName, rrtEnum, nightlyRate, startDate, endDate);
                salesManagerSessionBeanRemote.createNewRoomRate(rate); 
            
            } else if (response == 2) {
                String rate = doViewRateDetail(); 
             
                
            } else if (response == 3) {
                // updateUpdateRate(); 
//                doUpdateRate(); 
                String rateName = doViewRateDetail();
                System.out.println("Select detail to change (1-6): ");
               
                int choice = sc.nextInt(); 
                sc.nextLine();
                if (choice == 1) { 
                    System.out.println("Enter new rate name: ");
                    String newName = sc.nextLine(); 
                    salesManagerSessionBeanRemote.updateRateName(rateName, newName);

                } else if (choice == 2) {
                    System.out.println("Select new room type: ");
                    RoomType newRoomType = selectRoomType(); 
                    salesManagerSessionBeanRemote.updateRoomType(rateName, newRoomType);

                } else if (choice == 3) {
                    System.out.println(salesManagerSessionBeanRemote.retrieveRoomRateByName(rateName).getRateType());
                    RoomRateTypeEnum rrtEnum = selectEnum(RoomRateTypeEnum.class); 
                    System.out.println(rrtEnum);
                    salesManagerSessionBeanRemote.updateRateType(rateName, rrtEnum);
                    System.out.println("success!");

                } else if (choice == 4) {
                    System.out.println("Enter new nightly rate: ");
                    BigDecimal nightlyRate = sc.nextBigDecimal(); 
                    salesManagerSessionBeanRemote.updateRateAmount(rateName, nightlyRate);

                } else if (choice == 5) {
                    System.out.println("Enter new start date (yyyy-MM-dd): ");
                    LocalDate newDate = getInputDate(); 
                    salesManagerSessionBeanRemote.updateStartDate(rateName, newDate);

                } else if (choice == 6) {
                    System.out.println("Enter new end date (yyyy-MM-dd): ");
                    LocalDate newDate = getInputDate(); 
                    salesManagerSessionBeanRemote.updateEndDate(rateName, newDate);
                }
            } else if (response == 4) {
                //delete
                doDeleteRoomRate(); 
            } else if (response == 5) {
                doViewAllRoomRates();
            
            }
        }
        
        
    }
    
    public void systemAdminView() {
        System.out.println("Your role : System Administrator \n");

        Scanner sc = new Scanner(System.in); 
        int response; 
        
        while(true) {
     
            System.out.println("1: Create new employee");
            System.out.println("2: Create new partner");
            System.out.println("3 : View all employees");
            System.out.println("4: View all partners");
            response = sc.nextInt(); 
            sc.nextLine(); 
            
            if (response == 1) { 
                System.out.print("Enter employee username: ");
                String username = sc.nextLine(); 
                System.out.print("Enter employee password: ");
                String password = sc.nextLine(); 
                System.out.println("Choose employee role: ");
                EmployeeRoleEnum selectedRole = selectEnum(EmployeeRoleEnum.class);                
             
                Employee employee = new Employee(username, password, selectedRole); 
                systemAdminSessionBeanRemote.createNewEmployee(employee); 
            } else if (response == 2) {
                System.out.print("Enter partner name: ");
                String name = sc.nextLine();                 
                System.out.print("Enter partner username: ");
                String username = sc.nextLine(); 
                System.out.print("Enter partner password: ");
                String password = sc.nextLine();  
                Partner partner = new Partner(name, username, password); 
                systemAdminSessionBeanRemote.createNewPartner(partner); 
   
            } else if (response == 3) {
                List<Employee> employees = systemAdminSessionBeanRemote.retrieveAllEmployees();
                int count = 1; 
                for (Employee e : employees) {                    
                    System.out.println(count + ": " + e.getUsername());
                    count++; 
                }           
            } else if (response == 4) {
                List<Partner> partners = systemAdminSessionBeanRemote.retrieveAllPartners();
                int count = 1; 
                for (Partner p : partners) {                    
                    System.out.println(count + ": " + p.getPartnerName());
                    count++; 
                }
            }
        }
    }
    
    public static <T extends Enum<T>> T selectEnum(Class<T> enumClass) {
        T[] enumValues = enumClass.getEnumConstants();
        
        // Display all enum values with numbers
        System.out.println("Select an option:");
        for (int i = 0; i < enumValues.length; i++) {
            System.out.println((i + 1) + ": " + enumValues[i]);
        }

        Scanner scanner = new Scanner(System.in);
        int choice;

        // Loop until a valid input is provided
        while (true) {
            System.out.print("Enter a number to select an option: ");            
     
            choice = scanner.nextInt();

            if (choice >= 1 && choice <= enumValues.length) {
                return enumValues[choice - 1];  // Return the selected enum
            } else {
                System.out.println("Invalid choice. Please enter a number between 1 and " + enumValues.length + ".");
            }

        }
    }
    
    public static LocalDate getInputDate() {
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate = null;

        while (startDate == null) {
            String input = scanner.nextLine();

            // Validate the input format
            if (isValidDate(input)) {
                startDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }

        return startDate;
    }

    // Helper method to validate date format
    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    public String doViewRateDetail() {
        System.out.println("Enter rate name: ");
        Scanner sc = new Scanner(System.in);
        String rateName = sc.nextLine(); 
        RoomRate rate = salesManagerSessionBeanRemote.retrieveRoomRateByName(rateName); 
        // to format and printout (toString)
        System.out.println(rate);
        return rateName; 
    }
    
    public void doUpdateRate() {
        String rateName = doViewRateDetail();
        System.out.println("Select detail to change (1-6): ");
        Scanner sc = new Scanner(System.in);
        int response = sc.nextInt(); 
        if (response == 1) { 
            System.out.println("Enter new rate name: ");
            String newName = sc.nextLine(); 
            salesManagerSessionBeanRemote.updateRateName(rateName, newName);
            
        } else if (response == 2) {
            System.out.println("Select new room type: ");
            RoomType newRoomType = selectRoomType();
            salesManagerSessionBeanRemote.updateRoomType(rateName, newRoomType);
            
        } else if (response == 3) {
            RoomRateTypeEnum rrtEnum = selectEnum(RoomRateTypeEnum.class); 
            salesManagerSessionBeanRemote.updateRateType(rateName, rrtEnum);
            
        } else if (response == 4) {
            System.out.println("Enter new nightly rate: ");
            BigDecimal nightlyRate = sc.nextBigDecimal(); 
            salesManagerSessionBeanRemote.updateRateAmount(rateName, nightlyRate);
                                   
        } else if (response == 5) {
            System.out.println("Enter new start date (yyyy-MM-dd): ");
            LocalDate newDate = getInputDate(); 
            salesManagerSessionBeanRemote.updateStartDate(rateName, newDate);
            
        } else if (response == 6) {
            System.out.println("Enter new end date (yyyy-MM-dd): ");
            LocalDate newDate = getInputDate(); 
            salesManagerSessionBeanRemote.updateEndDate(rateName, newDate);
        }
    
    }
    
    public void doDeleteRoomRate() {
        String rateName = doViewRateDetail();
        salesManagerSessionBeanRemote.deleteRoomRate(rateName);
    }
    
    public void doViewAllRoomRates() {
        List<RoomRate> roomRates = salesManagerSessionBeanRemote.retrieveAllRoomRates(); 
        int count = 0; 
        for (RoomRate r : roomRates) {
            System.out.println(count + ": " + r.getRoomRateName());
            count++; 
        }
    }
    
    public RoomType selectRoomType() {
        List<RoomType> roomTypes = salesManagerSessionBeanRemote.retrieveAllRoomTypes(); 
        for (int i = 0; i < roomTypes.size(); i++) {
            System.out.println((i + 1) + ". " + roomTypes.get(i).getTypeName()); // Display name or other relevant information
        }

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice < 1 || choice > roomTypes.size()) {
            System.out.print("Please select a room type by entering the corresponding number: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number between 1 and " + roomTypes.size() + ": ");
                scanner.next(); // Consume invalid input
            }
            choice = scanner.nextInt();

            if (choice < 1 || choice > roomTypes.size()) {
                System.out.println("Invalid choice. Please enter a number between 1 and " + roomTypes.size() + ".");
            }
        }

        return roomTypes.get(choice - 1);
    }    

    
}
