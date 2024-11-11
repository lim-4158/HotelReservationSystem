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
import util.RoomTypeStatusEnum;

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
        
        Scanner sc = new Scanner(System.in); 
        int response;
        
        while (true) {
            System.out.println("Your role : Guest Relation Officer \n");
            System.out.println("1: Walk-in search room");
            System.out.println("2: Walk-in reserve room");
            System.out.println("3: Check-in guest");
            System.out.println("4: Check-out guest");
            System.out.print("Enter choice: ");
            response = sc.nextInt(); 
            sc.nextLine(); // Consume newline
            
            if (response == 1){
                // do walk in search room
            } else if (response == 2) {
               // do walk in reserve room
            } else if (response == 3) {
                // do check in guest
            } else if (response == 4) {
                // do check out guest
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
                
    }
    
    // ---------- Operations Manager View ----------
    
    public void operationsManagerView() {
        Scanner sc = new Scanner(System.in); 
        int response;
        
        while (true) {
            System.out.println("Your role : Operations Manager \n");
            System.out.println("1: Create new room type");
            System.out.println("2: View room type details");
            System.out.println("3: Update room type");
            System.out.println("4: Delete room type");
            System.out.println("5: View all room types");
            System.out.println("6: Create new room");
            System.out.println("7: Update room");
            System.out.println("8: Delete room");
            System.out.println("9: View all rooms");
            System.out.println("10: View room allocation exception report");        
            
            response = sc.nextInt(); 
            sc.nextLine(); // Consume newline
            
            if (response == 1){
                createNewRoomType();
            } else if (response == 2) {
                // view room type details
            } else if (response == 3) {
                // update room type
            } else if (response == 4) {
                // delete room type
            } else if (response == 5) {
                // view all room types
            } else if (response == 6) {
                // create new room
            } else if (response == 7) {
                // update room
            } else if (response == 8) {
            } else if (response == 9) {
            } else if (response == 10) {
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // ---------- Operations Manager Functionalities ----------
    
    public void createNewRoomType() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Create New Room Type ---");

        System.out.print("Enter Type Name: ");
        String typeName = sc.nextLine();

        System.out.print("Enter Description: ");
        String description = sc.nextLine();

        System.out.print("Enter Size (in square meters): ");
        BigDecimal size = sc.nextBigDecimal();
        sc.nextLine(); // Consume newline

        System.out.print("Enter Bed Type: ");
        String bed = sc.nextLine();

        System.out.print("Enter Capacity (number of guests): ");
        Long capacity = sc.nextLong();
        sc.nextLine(); // Consume newline

        System.out.print("Enter Amenities (comma-separated): ");
        String amenities = sc.nextLine();

        System.out.println("Select Room Type Status: ");
        RoomTypeStatusEnum roomTypeStatus = selectEnum(RoomTypeStatusEnum.class);

        System.out.print("Enter Tier Number: ");
        Integer tierNumber = sc.nextInt();
        sc.nextLine(); // Consume newline

        System.out.print("Enter Inventory (number of rooms): ");
        Long inventory = sc.nextLong();
        sc.nextLine(); // Consume newline

        // Create RoomType object
        RoomType roomType = new RoomType();
        roomType.setTypeName(typeName);
        roomType.setDescription(description);
        roomType.setSize(size);
        roomType.setBed(bed);
        roomType.setCapacity(capacity);
        roomType.setAmenities(amenities);
        roomType.setRoomTypeStatus(roomTypeStatus);
        roomType.setTierNumber(tierNumber);
        roomType.setInventory(inventory);

        // Assuming a session bean method to create room type
        Long roomTypeID = operationManagerSessionBeanRemote.createNewRoomType(roomType); 
        System.out.println("New Room Type created with ID: " + roomTypeID);
    
    }
    
    
    // ---------- Sales Manager View ----------
    
    public void salesManagerView() {
        Scanner sc = new Scanner(System.in); 
        int response;
        
        while (true) {
            System.out.println("\n*** Sales Manager Menu ***\n");
            System.out.println("1: Create new room rate");
            System.out.println("2: View room rate details");
            System.out.println("3: Update room rate");
            System.out.println("4: Delete room rate");
            System.out.println("5: View all room rates");
            System.out.println("6: Return to Main Menu");
            System.out.println("0: Logout");
            
            System.out.print("Enter choice: ");
            response = sc.nextInt(); 
            sc.nextLine(); // Consume newline
            
            if (response == 1){
                createNewRoomRate();
            } else if (response == 2) {
                viewRoomRateDetails();
            } else if (response == 3) {
                updateRoomRate();
            } else if (response == 4) {
                deleteRoomRate();
            } else if (response == 5) {
                viewAllRoomRates();
            } else if (response == 6) {
                System.out.println("Returning to Main Menu...");
                break; // Exit the Sales Manager view loop
            } else if (response == 0) {
                System.out.println("Logging out...");
                System.exit(0); // Terminate the application
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // ---------- Sales Manager Functionalities ----------
    
    private void createNewRoomRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Create New Room Rate ---");
        
        System.out.print("Enter Rate Name: ");
        String rateName = sc.nextLine(); 
        
        System.out.println("Select Rate Type: ");
        RoomRateTypeEnum rateType = selectEnum(RoomRateTypeEnum.class); 
        
        System.out.print("Enter Nightly Rate Amount: ");
        BigDecimal nightlyRate = sc.nextBigDecimal(); 
        sc.nextLine(); // Consume newline
        
        System.out.print("Enter Start Date (yyyy-MM-dd): ");
        LocalDate startDate = getInputDate();                  
        System.out.print("Enter End Date (yyyy-MM-dd): ");
        LocalDate endDate = getInputDate();      
                        
        RoomRate rate = new RoomRate();
        rate.setRoomRateName(rateName);
        rate.setRateType(rateType);
        rate.setNightlyRateAmount(nightlyRate);
        rate.setStartDate(startDate);
        rate.setEndDate(endDate);
        // Assuming RoomType association is handled elsewhere or optional
        // If necessary, prompt for RoomType and set it here
        
        Long roomRateID = salesManagerSessionBeanRemote.createNewRoomRate(rate); 
        System.out.println("New Room Rate created with ID: " + roomRateID);
    }
    
    private void viewRoomRateDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- View Room Rate Details ---");
        
        System.out.print("Enter Room Rate Name: ");
        String rateName = sc.nextLine(); 
        
        RoomRate rate = salesManagerSessionBeanRemote.retrieveRoomRateByName(rateName); 
        
        if (rate != null) {
            displayRoomRate(rate);
        } else {
            System.out.println("Room Rate not found.");
        }
    }
    
    private void updateRoomRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Update Room Rate ---");
        
        System.out.print("Enter Room Rate Name to Update: ");
        String rateName = sc.nextLine(); 
        
        RoomRate rate = salesManagerSessionBeanRemote.retrieveRoomRateByName(rateName); 
        
        if (rate != null) {
            System.out.println("Current Details:");
            displayRoomRate(rate);
            
            System.out.println("\nEnter new details (leave blank to keep unchanged):");
            
            System.out.print("New Rate Name: ");
            String newRateName = sc.nextLine(); 
            if (!newRateName.isEmpty()) {
                salesManagerSessionBeanRemote.updateRateName(rateName, newRateName);
                rate.setRoomRateName(newRateName);
            }
            
            System.out.println("Select new Rate Type (enter 0 to keep unchanged): ");
            RoomRateTypeEnum newRateType = selectEnum(RoomRateTypeEnum.class); 
            if (newRateType != null) {
                salesManagerSessionBeanRemote.updateRateType(rateName, newRateType);
                rate.setRateType(newRateType);
            }
            
            System.out.print("Enter new Nightly Rate Amount (enter 0 to keep unchanged): ");
            BigDecimal newNightlyRate = sc.nextBigDecimal(); 
            sc.nextLine(); // Consume newline
            if (newNightlyRate.compareTo(BigDecimal.ZERO) > 0) {
                salesManagerSessionBeanRemote.updateRateAmount(rateName, newNightlyRate);
                rate.setNightlyRateAmount(newNightlyRate);
            }
            
            System.out.print("Enter new Start Date (yyyy-MM-dd) (leave blank to keep unchanged): ");
            String startDateInput = sc.nextLine();
            if (!startDateInput.isEmpty()) {
                LocalDate newStartDate = LocalDate.parse(startDateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                salesManagerSessionBeanRemote.updateStartDate(rateName, newStartDate);
                rate.setStartDate(newStartDate);
            }
            
            System.out.print("Enter new End Date (yyyy-MM-dd) (leave blank to keep unchanged): ");
            String endDateInput = sc.nextLine();
            if (!endDateInput.isEmpty()) {
                LocalDate newEndDate = LocalDate.parse(endDateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                salesManagerSessionBeanRemote.updateEndDate(rateName, newEndDate);
                rate.setEndDate(newEndDate);
            }
            
            System.out.println("Room Rate updated successfully.");
        } else {
            System.out.println("Room Rate not found.");
        }
    }
    
    private void deleteRoomRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Delete Room Rate ---");
        
        System.out.print("Enter Room Rate Name to Delete: ");
        String rateName = sc.nextLine(); 
        
        RoomRate rate = salesManagerSessionBeanRemote.retrieveRoomRateByName(rateName); 
        
        if (rate != null) {
            System.out.print("Are you sure you want to delete this Room Rate? (yes/no): ");
            String confirmation = sc.nextLine();
            if (confirmation.equalsIgnoreCase("yes")) {
                salesManagerSessionBeanRemote.deleteRoomRate(rateName);
                System.out.println("Room Rate deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("Room Rate not found.");
        }
    }
    
    private void viewAllRoomRates() {
        System.out.println("\n--- All Room Rates ---");
        List<RoomRate> roomRates = salesManagerSessionBeanRemote.retrieveAllRoomRates(); 
        
        if (!roomRates.isEmpty()) {
            for (RoomRate r : roomRates) {
                displayRoomRate(r);
                System.out.println("----------------------------");
            }
        } else {
            System.out.println("No Room Rates found.");
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

    /**
     * Displays the details of a RoomRate entity.
     *
     * @param rate The RoomRate entity to display.
     */
    private void displayRoomRate(RoomRate rate) {
        System.out.println("Room Rate ID: " + rate.getRoomRateID());
        System.out.println("Rate Name: " + rate.getRoomRateName());
        System.out.println("Rate Type: " + rate.getRateType());
        System.out.println("Nightly Rate Amount: $" + rate.getNightlyRateAmount());
        System.out.println("Start Date: " + rate.getStartDate());
        System.out.println("End Date: " + rate.getEndDate());
        // If associated with RoomType, display it
        if (rate.getRoomType() != null) {
            System.out.println("Associated Room Type: " + rate.getRoomType().getTypeName());
        }
    }
    
    /**
     * Displays the details of an Employee entity.
     *
     * @param employee The Employee entity to display.
     */
    private void displayEmployee(Employee employee) {
        System.out.println("Employee ID: " + employee.getEmployeeID());
        System.out.println("Username: " + employee.getUsername());
        System.out.println("Role: " + employee.getRole());
        // Avoid displaying passwords for security reasons
    }
    
    /**
     * Displays the details of a Partner entity.
     *
     * @param partner The Partner entity to display.
     */
    private void displayPartner(Partner partner) {
        System.out.println("Partner ID: " + partner.getPartnerID());
        System.out.println("Partner Name: " + partner.getPartnerName());
        System.out.println("Username: " + partner.getUsername());
        // Avoid displaying passwords for security reasons
   
    }
}


