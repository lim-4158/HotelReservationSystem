/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.BatchAllocationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestRelationOfficerSessionBeanRemote;
import ejb.session.stateless.OperationManagerSessionBeanRemote;
import ejb.session.stateless.SalesManagerSessionBeanRemote;
import ejb.session.stateless.SystemAdminSessionBeanRemote;
import entity.Employee;
import entity.ExceptionReport;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomReservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import util.EmployeeRoleEnum;
import util.ReservationTypeEnum;
import util.RoomRateTypeEnum;
import util.RoomStatusEnum;
import util.RoomTypeStatusEnum;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

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
    private BatchAllocationSessionBeanRemote batchAllocationSessionBeanRemote; 

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, GuestRelationOfficerSessionBeanRemote guestRelationOfficerSessionBeanRemote, OperationManagerSessionBeanRemote operationManagerSessionBeanRemote, SalesManagerSessionBeanRemote salesManagerSessionBeanRemote, SystemAdminSessionBeanRemote systemAdminSessionBeanRemote, BatchAllocationSessionBeanRemote batchAllocationSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.guestRelationOfficerSessionBeanRemote = guestRelationOfficerSessionBeanRemote;
        this.operationManagerSessionBeanRemote = operationManagerSessionBeanRemote;
        this.salesManagerSessionBeanRemote = salesManagerSessionBeanRemote;
        this.systemAdminSessionBeanRemote = systemAdminSessionBeanRemote;
        this.batchAllocationSessionBeanRemote = batchAllocationSessionBeanRemote;
    }
    
    public void runApp() {
        
        Scanner scanner = new Scanner(System.in);
        
        while(true) {
            System.out.println("*** Welcome to HoRS Management Client ***\n");
            Integer choice; 
            Scanner sc = new Scanner(System.in); 
            
            
            System.out.println("1 : Log In Portal");
            System.out.println("2 : Run Batch Allocation");
            
            choice = sc.nextInt(); 
            if (choice == 1 ) {
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
            } else if (choice == 2) {
                System.out.println("Enter date (yyyy-MM-dd): ");
                LocalDate date = getInputDate();
                batchAllocationSessionBeanRemote.allocateRooms(date);
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
            System.out.print(">");
            response = sc.nextInt(); 
            sc.nextLine(); // Consume newline
            
            if (response == 1){
                doWalkInSearchRoom();
            } else if (response == 2) {
                doWalkInReserveRoom();
            } else if (response == 3) {
                doCheckInGuest();
            } else if (response == 4) {
                doCheckOutGuest();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
                
    }
    
    // ---------- Guest Relations Officer View ----------
    
    // 1. Walk In Search Room - Searches for All Available Room Types with sufficient inventory for the 
    // number of rooms needed, and returns total amount 
    
    private void doWalkInSearchRoom() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** Walk-in Room Search ***");

        System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
        LocalDate checkInDate = getInputDate();

        System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
        LocalDate checkOutDate = getInputDate();

        System.out.print("Enter required room count: ");
        int requiredRooms = sc.nextInt();
        sc.nextLine(); // Consume newline

        List<RoomType> availableRooms = guestRelationOfficerSessionBeanRemote.searchAvailableRooms(checkInDate, checkOutDate, requiredRooms);

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for the specified dates and required inventory.");
        } else {
            System.out.println("Available Room Types:");
            for (RoomType roomType : availableRooms) {
                System.out.println("Room Type: " + roomType.getTypeName());
                System.out.println("Total Amount for Stay: " + guestRelationOfficerSessionBeanRemote.calculateTotalAmountForStay(roomType.getTypeName(), checkInDate, checkOutDate, requiredRooms) + "\n");
            }
        }
    }
    
    //  2. Walk In Reserve Room
    
    private void doWalkInReserveRoom() {
        
        Scanner sc = new Scanner(System.in);
        
        try {
            System.out.println("*** Walk-in Room Reservation ***");

            // Step 1: Get check-in, check-out dates and required room count
            System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
            LocalDate checkInDate = getInputDate();

            System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
            LocalDate checkOutDate = getInputDate();

            System.out.print("Enter required room count: ");
            int requiredRooms = sc.nextInt();
            sc.nextLine(); // Consume newline
            
            // Step 2: Search for available rooms
            List<RoomType> availableRooms = guestRelationOfficerSessionBeanRemote.searchAvailableRooms(checkInDate, checkOutDate, requiredRooms);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for the specified dates and required inventory.");
                return; // Exit the method if no rooms are available
            } else {
                // Step 3: Display available rooms with indices for selection
                System.out.println("Available Room Types:");
                for (int i = 0; i < availableRooms.size(); i++) {
                    RoomType roomType = availableRooms.get(i);
                    System.out.println((i + 1) + ". Room Type: " + roomType.getTypeName());
                    System.out.println("   Total Amount for Stay: " + 
                        guestRelationOfficerSessionBeanRemote.calculateTotalAmountForStay(roomType.getTypeName(), checkInDate, checkOutDate, requiredRooms));
                    System.out.println();
                }

                // Step 4: Allow the user to select a room type
                System.out.print("Select a room type by entering the number: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                // Validate the choice
                if (choice < 1 || choice > availableRooms.size()) {
                    System.out.println("Invalid choice. Reservation cancelled.");
                    return;
                }

                RoomType selectedRoomType = availableRooms.get(choice - 1);
                String roomTypeName = selectedRoomType.getTypeName();

                // Step 5: Calculate total amount for the selected room type
                BigDecimal totalAmount = guestRelationOfficerSessionBeanRemote.calculateTotalAmountForStay(roomTypeName, checkInDate, checkOutDate, requiredRooms);

                // Step 6: Create the reservation
                RoomType rt = operationManagerSessionBeanRemote.retrieveRoomTypeByName(roomTypeName);
                
                System.out.print("Enter First Name: ");
                String firstName = sc.nextLine().trim();

                System.out.print("Enter Email: ");
                String email = sc.nextLine().trim();
                
                Guest guest = new Guest(firstName, email);
                Reservation newReservation = new Reservation(LocalDate.now(), checkInDate, checkOutDate, ReservationTypeEnum.WALKIN, guest, rt, requiredRooms);
                guestRelationOfficerSessionBeanRemote.createReservation(newReservation);

                System.out.println("Room reserved successfully! Total amount: " + totalAmount);
            }
        
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Error updating Room Type: " + e.getMessage());
            e.printStackTrace();
        }  
    }

    
    // 3. Check In Guest

    private void doCheckInGuest() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** Guest Check-In ***");

        // Step 1: Get the guest's email
        System.out.print("Enter Guest Email: ");
        String email = sc.nextLine().trim();

        // Step 2: Find the guest by email
        Guest guest = guestRelationOfficerSessionBeanRemote.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("No guest found with the provided email.");
            return; // Exit if no guest is found
        }

        // Step 3: Retrieve all reservations associated with this guest's ID
        List<Reservation> reservations = guestRelationOfficerSessionBeanRemote.findReservationsByGuest(guest.getGuestID());

        if (reservations.isEmpty()) {
            System.out.println("No reservations found for the provided guest.");
            return; // Exit if no reservations are found
        }

        // Step 4: Display the reservations for selection
        System.out.println("Reservations for " + guest.getFirstName() + " " + guest.getLastName() + ":");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            System.out.println((i + 1) + ". Reservation ID: " + reservation.getReservationID() +
                               ", Room Type: " + reservation.getRoomType().getTypeName() +
                               ", Check-in Date: " + reservation.getCheckInDate() +
                               ", Check-out Date: " + reservation.getCheckOutDate());
        }

        // Step 5: Prompt the user to select a reservation
        System.out.print("Select a reservation to check in (enter number): ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (choice < 1 || choice > reservations.size()) {
            System.out.println("Invalid selection. Check-in process cancelled.");
            return;
        }

        // Step 6: Get the selected reservation ID
        Reservation selectedReservation = reservations.get(choice - 1);
        Long reservationId = selectedReservation.getReservationID();

        // Step 7: Check in the guest
        List<String> roomNumbers = guestRelationOfficerSessionBeanRemote.checkInGuest(reservationId, selectedReservation.getCheckInDate());

        if (roomNumbers.isEmpty()) {
            System.out.println("Check-in failed. Reservation not found or already checked in.");
        } else {
            System.out.println("Guest checked in successfully. Room numbers: " + roomNumbers);
        }
    }
    
    // 4. Check Out Guest
        
    private void doCheckOutGuest() {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Guest Check-Out ***");

        // Step 1: Get the guest's email
        System.out.print("Enter Guest Email: ");
        String email = sc.nextLine().trim();

        // Step 2: Find the guest by email
        Guest guest = guestRelationOfficerSessionBeanRemote.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("No guest found with the provided email.");
            return;
        }

        // Step 3: Retrieve all reservations associated with this guest
        List<Reservation> reservations = guestRelationOfficerSessionBeanRemote.findReservationsByGuest(guest.getGuestID());

        if (reservations.isEmpty()) {
            System.out.println("No reservations found for the provided guest.");
            return;
        }

        // Step 4: Display the reservations for selection
        System.out.println("Reservations for " + guest.getFirstName() + " " + guest.getLastName() + ":");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            System.out.println((i + 1) + ". Reservation ID: " + reservation.getReservationID() +
                               ", Room Type: " + reservation.getRoomType().getTypeName() +
                               ", Check-in Date: " + reservation.getCheckInDate() +
                               ", Check-out Date: " + reservation.getCheckOutDate());
        }

        // Step 5: Prompt the user to select a reservation
        System.out.print("Select a reservation to check out (enter number): ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (choice < 1 || choice > reservations.size()) {
            System.out.println("Invalid selection. Check-out process cancelled.");
            return;
        }

        Reservation selectedReservation = reservations.get(choice - 1);

        // Step 7: Check out the guest by setting the check-out date
        guestRelationOfficerSessionBeanRemote.checkOutGuest(selectedReservation.getReservationID(), LocalDate.now());

        System.out.println("Guest checked out successfully. Check-out date set to " + LocalDate.now());
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
            System.out.print("> ");
            
            response = sc.nextInt(); 
            sc.nextLine(); // Consume newline
            
            if (response == 1){
                createNewRoomType();
            } else if (response == 2) {
                doViewRoomTypeDetails();
            } else if (response == 3) {
                doUpdateRoomType();
            } else if (response == 4) {
                doDeleteRoomType();
            } else if (response == 5) {
                doViewAllRoomTypes();
            } else if (response == 6) {
                doCreateNewRoom();
            } else if (response == 7) {
                doUpdateRoom();
            } else if (response == 8) {
                doDeleteRoom();
            } else if (response == 9) {
                doViewAllRooms();
            } else if (response == 10) {
                doViewExceptionReports();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // ---------- Operations Manager Functionalities ----------
    
    // 1. Create New Room Type
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
    
    // 2. View Room Type Details
    public void doViewRoomTypeDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- View Room Type Details ---");

        System.out.print("Enter Room Type Name: ");
        String input = sc.nextLine();

        RoomType roomType;
        try {
            // Assume there's a method to find a room type by name or ID
            roomType = operationManagerSessionBeanRemote.retrieveRoomTypeByName(input);

            // Display room type details
            System.out.println("\n--- Room Type Details ---");
            System.out.println("Type Name: " + roomType.getTypeName());
            System.out.println("Description: " + roomType.getDescription());
            System.out.println("Size: " + roomType.getSize() + " square meters");
            System.out.println("Bed Type: " + roomType.getBed());
            System.out.println("Capacity: " + roomType.getCapacity() + " guests");
            System.out.println("Amenities: " + roomType.getAmenities());
            System.out.println("Room Type Status: " + roomType.getRoomTypeStatus());
            System.out.println("Tier Number: " + roomType.getTierNumber());
            System.out.println("Inventory: " + roomType.getInventory() + " rooms available");

        } catch (RoomTypeNotFoundException e) {
            System.out.println("Room Type not found: " + e.getMessage());
        }
    }

    // 3. Update Room Type
    private void doUpdateRoomType() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Update Room Type ---");
        
        System.out.print("Enter Room Type Name to Update: ");
        String typeName = sc.nextLine().trim();
        
        try {
            RoomType roomType = operationManagerSessionBeanRemote.retrieveRoomTypeByName(typeName);
            Long roomTypeID = roomType.getRoomTypeID();
            System.out.println("roomtype id is " + roomTypeID);
            
            if (roomTypeID != null) {
                System.out.println("Current Details:");
                displayRoomType(roomType);
                
                System.out.println("\nEnter new details (leave blank to keep unchanged):");
                
                System.out.print("New Type Name: ");
                String newTypeName = sc.nextLine().trim();
                if (!newTypeName.isEmpty()) {
                    System.out.println("Attempting to update Type Name to: " + newTypeName);
                    operationManagerSessionBeanRemote.updateTypeName(roomTypeID, newTypeName);
                    roomType.setTypeName(newTypeName);
                }
                
                System.out.print("New Description: ");
                String newDescription = sc.nextLine().trim();
                if (!newDescription.isEmpty()) {
                    operationManagerSessionBeanRemote.updateDescription(roomTypeID, newDescription);
                    roomType.setDescription(newDescription);
                }
                
                System.out.print("New Size (in square meters): ");
                String sizeInput = sc.nextLine().trim();
                if (!sizeInput.isEmpty()) {
                    try {
                        BigDecimal newSize = new BigDecimal(sizeInput);
                        operationManagerSessionBeanRemote.updateSize(roomTypeID, newSize);
                        roomType.setSize(newSize);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid size input. Size not updated.");
                    }
                }
                
                System.out.print("New Bed Type: ");
                String newBed = sc.nextLine().trim();
                if (!newBed.isEmpty()) {
                    operationManagerSessionBeanRemote.updateBed(roomTypeID, newBed);
                    roomType.setBed(newBed);
                }
                
                System.out.print("New Capacity (number of guests): ");
                String capacityInput = sc.nextLine().trim();
                if (!capacityInput.isEmpty()) {
                    try {
                        Long newCapacity = Long.parseLong(capacityInput);
                        operationManagerSessionBeanRemote.updateCapacity(roomTypeID, newCapacity);
                        roomType.setCapacity(newCapacity);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid capacity input. Capacity not updated.");
                    }
                }
                
                System.out.print("New Amenities (comma-separated): ");
                String newAmenities = sc.nextLine().trim();
                if (!newAmenities.isEmpty()) {
                    operationManagerSessionBeanRemote.updateAmenities(roomTypeID, newAmenities);
                    roomType.setAmenities(newAmenities);
                }
                
                System.out.println("Select new Room Type Status (enter 0 to keep unchanged): ");
                RoomTypeStatusEnum newStatus = selectEnum(RoomTypeStatusEnum.class);
                if (newStatus != null) {
                    operationManagerSessionBeanRemote.updateRoomTypeStatus(roomTypeID, newStatus);
                    roomType.setRoomTypeStatus(newStatus);
                }
                
                System.out.print("New Tier Number: ");
                String tierInput = sc.nextLine().trim();
                if (!tierInput.isEmpty()) {
                    try {
                        Integer newTierNumber = Integer.parseInt(tierInput);
                        operationManagerSessionBeanRemote.updateTierNumber(roomTypeID, newTierNumber);
                        roomType.setTierNumber(newTierNumber);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid tier number input. Tier number not updated.");
                    }
                }
                
                System.out.print("New Inventory (number of rooms): ");
                String inventoryInput = sc.nextLine().trim();
                if (!inventoryInput.isEmpty()) {
                    try {
                        Long newInventory = Long.parseLong(inventoryInput);
                        operationManagerSessionBeanRemote.updateInventory(roomTypeID, newInventory);
                        roomType.setInventory(newInventory);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid inventory input. Inventory not updated.");
                    }
                }
                
                System.out.println("Room Type updated successfully.");
            } else {
                System.out.println("Room Type '" + typeName + "' not found.");
            }
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Error updating Room Type: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // 4. Delete Room Type
    private void doDeleteRoomType() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Delete Room Type ---");
        
        System.out.print("Enter Room Type Name to Delete: ");
        String typeName = sc.nextLine().trim();
        
        try {
            RoomType roomType = operationManagerSessionBeanRemote.retrieveRoomTypeByName(typeName);
            Long roomTypeID = roomType.getRoomTypeID();
            System.out.println("roomtype id is " + roomTypeID);
            
            if (roomTypeID != null) {
                System.out.print("Are you sure you want to delete Room Type '" + typeName + "'? (yes/no): ");
                String confirmation = sc.nextLine().trim();
                if (confirmation.equalsIgnoreCase("yes")) {
                    operationManagerSessionBeanRemote.deleteRoomType(roomTypeID);
                    System.out.println("Room Type '" + typeName + "' deleted successfully.");
                    
                } else {
                    System.out.println("Deletion cancelled.");
                }
            } else {
                System.out.println("Room Type '" + typeName + "' not found.");
            }
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Error deleting Room Type: " + e.getMessage());
        }
    }
     
    // 5. View All Room Types
    private void doViewAllRoomTypes() {
        System.out.println("\n--- All Room Types ---");
        try {
            List<RoomType> roomTypes = operationManagerSessionBeanRemote.retrieveAllRoomTypes(); 
            
            if (!roomTypes.isEmpty()) {
                for (RoomType rt : roomTypes) {
                    displayRoomType(rt);
                    System.out.println("----------------------------");
                }
            } else {
                System.out.println("No Room Types found.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving Room Types: " + e.getMessage());
        }
    }
    
    // 6. Create New Room
    private void doCreateNewRoom() { 
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Create New Room ---");
        
        try {        
            System.out.print("Enter Room Number: ");
            String roomNumber = sc.nextLine().trim();
            
            System.out.println("Select Room Status: ");
            RoomStatusEnum roomStatus = selectEnum(RoomStatusEnum.class);
            
            System.out.println("Select Room Type: ");
            RoomType roomType = selectRoomType();
            
            // Create Room object
            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setRoomStatus(roomStatus);
            room.setRoomType(roomType);
            
            // Assuming a session bean method to create room
            Long roomID = operationManagerSessionBeanRemote.createNewRoom(room);
            System.out.println("New Room created with ID: " + roomID);
        
        } catch (Exception e) {
            System.out.println("Error creating Room: " + e.getMessage());
        }
    }
    
    // 7. Update Room
    private void doUpdateRoom() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Update Room ---");
        
        System.out.print("Enter Room Number to Update: ");
        String roomNumber = sc.nextLine().trim();
        
        try {
            Room room = operationManagerSessionBeanRemote.retrieveRoomByNumber(roomNumber);
            Long roomID = room.getRoomID();
            System.out.println("room id is " + roomID);
            
            
            if (roomID != null) {
                System.out.println("Current Details:");
                displayRoom(room);
                
                System.out.println("\nEnter new details (leave blank to keep unchanged):");
              
                
                System.out.print("New Room Number: ");
                String newRoomNumber = sc.nextLine().trim();
                if (!newRoomNumber.isEmpty()) {
                    operationManagerSessionBeanRemote.updateRoomNumber(roomID, newRoomNumber);
                    room.setRoomNumber(newRoomNumber);
                }
                
                System.out.println("Select new Room Status (enter 0 to keep unchanged): ");
                RoomStatusEnum newStatus = selectEnum(RoomStatusEnum.class);
                if (newStatus != null) {
                    operationManagerSessionBeanRemote.updateRoomStatus(roomID, newStatus);
                    room.setRoomStatus(newStatus);
                }
                
                System.out.println("Select new Room Type (enter 0 to keep unchanged): ");
                RoomType newRoomType = selectRoomType();
                if (newRoomType != null) {
                    operationManagerSessionBeanRemote.updateRoomType(roomID, newRoomType);
                    // Update in database if necessary
                }
                
                // Persist changes if required by session bean
                // e.g., operationManagerSessionBeanRemote.updateRoom(room);
                
                System.out.println("Room updated successfully.");
            } else {
                System.out.println("Room '" + roomNumber + "' not found.");
            }
        } catch (RoomNotFoundException e) {
            System.out.println("Error updating Room: " + e.getMessage());
        }
    }
    
    // 8. Delete Room
    private void doDeleteRoom() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Delete Room ---");
        
        System.out.print("Enter Room Number to Delete: ");
        String roomNumber = sc.nextLine().trim();
        
        try {
            Room room = operationManagerSessionBeanRemote.retrieveRoomByNumber(roomNumber);
            Long roomID = room.getRoomID();
            System.out.println("room id is " + roomID);

            if (roomID != null) {
                System.out.print("Are you sure you want to delete Room '" + roomNumber + "'? (yes/no): ");
                String confirmation = sc.nextLine().trim();
                if (confirmation.equalsIgnoreCase("yes")) {
                    operationManagerSessionBeanRemote.deleteRoom(roomID);
                    System.out.println("Room '" + roomNumber + "' deleted successfully.");
                } else {
                    System.out.println("Deletion cancelled.");
                }
            } else {
                System.out.println("Room '" + roomNumber + "' not found.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting Room: " + e.getMessage());
        }
    }
    
    // 9. View All Rooms
    private void doViewAllRooms() {
        System.out.println("\n--- All Rooms ---");
        try {
            List<Room> rooms = operationManagerSessionBeanRemote.retrieveAllRooms(); 
            
            if (!rooms.isEmpty()) {
                for (Room room : rooms) {
                    displayRoom(room);
                    System.out.println("----------------------------");
                }
            } else {
                System.out.println("No Rooms found.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving Rooms: " + e.getMessage());
        }
    }
    
    // 10. View Room Allocation Exception Reports
    private void doViewExceptionReports() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- View Room Allocation Exception Reports ---");
        
        System.out.print("Enter date to filter reports (yyyy-MM-dd): ");
        LocalDate date = getInputDate();
        
        try {
            List<ExceptionReport> reports = operationManagerSessionBeanRemote.retrieveExceptionReportsByDate(date);
            
            if (!reports.isEmpty()) {
                for (ExceptionReport report : reports) {
                    displayExceptionReport(report);
                    System.out.println("----------------------------");
                }
            } else {
                System.out.println("No Exception Reports found for " + date + ".");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving Exception Reports: " + e.getMessage());
        }
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
            System.out.print("> ");
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
        RoomType roomType = selectRoomType();
        if (roomType == null) {
            System.out.println("Invalid Room Type selection. Room Rate creation aborted.");
            return;
        }
        rate.setRoomType(roomType);
        
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

        try {
            RoomRate rate = salesManagerSessionBeanRemote.retrieveRoomRateByName(rateName);
            Long rateID = rate.getRoomRateID();

            System.out.println("Current Details:");
            displayRoomRate(rate);

            System.out.println("\nEnter new details (leave blank to keep unchanged):");

            System.out.print("New Rate Name: ");
            String newRateName = sc.nextLine();
            if (!newRateName.isEmpty()) {
                salesManagerSessionBeanRemote.updateRateName(rateID, newRateName);
                rate.setRoomRateName(newRateName);
            }

            System.out.println("Select new Rate Type: ");
            RoomRateTypeEnum newRateType = selectEnum(RoomRateTypeEnum.class);
            if (newRateType != null) {
                salesManagerSessionBeanRemote.updateRateType(rateID, newRateType);
                rate.setRateType(newRateType);
            }

            System.out.print("Enter new Nightly Rate Amount (enter 0 to keep unchanged): ");
            BigDecimal newNightlyRate = sc.nextBigDecimal();
            sc.nextLine(); // Consume newline
            if (newNightlyRate.compareTo(BigDecimal.ZERO) > 0) {
                salesManagerSessionBeanRemote.updateRateAmount(rateID, newNightlyRate);
                rate.setNightlyRateAmount(newNightlyRate);
            }

            System.out.print("Enter new Start Date (yyyy-MM-dd) (leave blank to keep unchanged): ");
            String startDateInput = sc.nextLine();
            if (!startDateInput.isEmpty()) {
                LocalDate newStartDate = LocalDate.parse(startDateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                salesManagerSessionBeanRemote.updateStartDate(rateID, newStartDate);
                rate.setStartDate(newStartDate);
            }

            System.out.print("Enter new End Date (yyyy-MM-dd) (leave blank to keep unchanged): ");
            String endDateInput = sc.nextLine();
            if (!endDateInput.isEmpty()) {
                LocalDate newEndDate = LocalDate.parse(endDateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                salesManagerSessionBeanRemote.updateEndDate(rateID, newEndDate);
                rate.setEndDate(newEndDate);
            }
            
            // New section to update isDisabled field
            System.out.print("Enter new Disabled Status (true/false, leave blank to keep unchanged): ");
            String isDisabledInput = sc.nextLine();
            if (!isDisabledInput.isEmpty()) {
                boolean isDisabled = Boolean.parseBoolean(isDisabledInput);
                salesManagerSessionBeanRemote.updateIsDisabled(rateID, isDisabled);
                rate.setIsDisabled(isDisabled);
            }

            System.out.println("Room Rate updated successfully.");
        } catch (RoomRateNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    
    private void deleteRoomRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Delete Room Rate ---");
        
        System.out.print("Enter Room Rate Name to Delete: ");
        String rateName = sc.nextLine(); 
        
        RoomRate rate = salesManagerSessionBeanRemote.retrieveRoomRateByName(rateName); 
        Long rateID = rate.getRoomRateID();
        
        try {
            if (rateID != null) {
                System.out.print("Are you sure you want to delete this Room Rate? (yes/no): ");
                String confirmation = sc.nextLine();
                if (confirmation.equalsIgnoreCase("yes")) {
                    salesManagerSessionBeanRemote.deleteRoomRate(rateID);
                    System.out.println("Room Rate deleted successfully.");
                } else {
                    System.out.println("Deletion cancelled.");
                }
            } else {
                System.out.println("Room Rate not found.");
            }
        } catch (RoomRateNotFoundException e) {
            System.out.println("No Room Rate Found");
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
    
    // ---------- System Admin View ----------
    
    public void systemAdminView() {
        System.out.println("Your role : System Administrator \n");

        Scanner sc = new Scanner(System.in); 
        int response; 
        
        while(true) {
     
            System.out.println("1: Create new employee");
            System.out.println("2: Create new partner");
            System.out.println("3 : View all employees");
            System.out.println("4: View all partners");
            System.out.println("> ");
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
    
    // ---------- Helper Methods ----------
    
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
        System.out.println("Is Disabled: " + rate.isIsDisabled());
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
        private void displayRoom(Room room) {
        System.out.println("Room ID: " + room.getRoomID());
        System.out.println("Room Number: " + room.getRoomNumber());
        System.out.println("Room Status: " + room.getRoomStatus());
        if (room.getRoomType() != null) {
            System.out.println("Room Type: " + room.getRoomType().getTypeName());
        } else {
            System.out.println("Room Type: Not Assigned");
        }
    }    
    
    private void displayExceptionReport(ExceptionReport report) {
        System.out.println("Report ID: " + report.getReportID());
        System.out.println("Report Type: " + report.getReportType());
        System.out.println("Creation Date: " + report.getCreationDate());
        if (report.getRoomReservation() != null) {
            RoomReservation rr = report.getRoomReservation();
            System.out.println("Associated Room ID: " + rr.getRoom().getRoomID());
            System.out.println("Associated Reservation ID: " + rr.getReservation().getReservationID());
        } else {
            System.out.println("Associated Room Reservation: Not Assigned");
        }
    }
    
    private void displayRoomType(RoomType roomType) {
        System.out.println("Room Type ID: " + roomType.getRoomTypeID());
        System.out.println("Type Name: " + roomType.getTypeName());
        System.out.println("Description: " + roomType.getDescription());
        System.out.println("Size: " + roomType.getSize() + " sqm");
        System.out.println("Bed Type: " + roomType.getBed());
        System.out.println("Capacity: " + roomType.getCapacity() + " guests");
        System.out.println("Amenities: " + roomType.getAmenities());
        System.out.println("Status: " + roomType.getRoomTypeStatus());
        System.out.println("Tier Number: " + roomType.getTierNumber());
        System.out.println("Inventory: " + roomType.getInventory() + " rooms");
    }    
}


