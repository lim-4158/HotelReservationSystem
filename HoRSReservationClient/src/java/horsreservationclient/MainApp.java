/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import ejb.session.stateless.GuestRelationOfficerSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.SalesManagerSessionBeanRemote;
import entity.Guest;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 *
 * @author jamiewee
 */
import java.util.Scanner;
import util.ReservationTypeEnum;
import util.exceptions.GuestNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

public class MainApp {

    private boolean isLoggedIn = false;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private SalesManagerSessionBeanRemote salesManagerSessionBeanRemote;
    private GuestRelationOfficerSessionBeanRemote guestRelationOfficerSessionBeanRemote;

    public MainApp() {
    }
    
    public MainApp(GuestSessionBeanRemote guestSessionBeanRemote, SalesManagerSessionBeanRemote salesManagerSessionBeanRemote, GuestRelationOfficerSessionBeanRemote guestRelationOfficerSessionBeanRemote) {
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.salesManagerSessionBeanRemote = salesManagerSessionBeanRemote;
        this.guestRelationOfficerSessionBeanRemote = guestRelationOfficerSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        int response;

        while (true) {
            System.out.println("*** Welcome to HoRS Reservation Client ***\n");
            System.out.println("1: Guest Login");
            System.out.println("2: Register as Guest");
            System.out.println("3: Exit");
            System.out.print("> ");

            response = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (response == 1) {
                
                Guest guest = doGuestLogin();
                if (guest != null & isLoggedIn) { // Check if login was successful
                    guestMenu(guest);
                } else {
                    System.out.println("Login failed. Please try again.\n");
                }

            } else if (response == 2) {
                doRegisterGuest();
            } else if (response == 3) {
                System.out.println("Thank you for using HoRS Reservation Client!");
                break;
            } else {
                System.out.println("Invalid option, please try again.\n"); // replace with implementing exception
            }
        }
    }

    // ---------- Visitor Functionalities ----------
    
    public Guest doGuestLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Guest Login ***\n");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            Guest currentGuest = guestSessionBeanRemote.doLogin(username, password);
            isLoggedIn = true;
            System.out.println("Login successful! Welcome, " + currentGuest.getFirstName() + " " + currentGuest.getLastName() + "\n");
            return currentGuest;

        } catch (GuestNotFoundException e) {
            System.out.println("Login failed: " + e.getMessage());
            isLoggedIn = false;
            return null; // Return null if login fails
        }
    }


    public void doRegisterGuest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Register as Guest ***\n");

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Passport Number: ");
        String passportnum = scanner.nextLine().trim();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        Guest guest = new Guest(firstName,lastName, email, phone, passportnum, username, password);
        
        guestSessionBeanRemote.createGuest(guest);

        System.out.println("Registration successful!\n");
    }

    public void guestMenu(Guest guest) {
        Scanner scanner = new Scanner(System.in);
        int response;

        while (true) {
            System.out.println("*** Welcome to HoRS Guest Menu ***\n");
            System.out.println("1: Search Hotel Room");
            System.out.println("2: Reserve Hotel Room");
            System.out.println("3: View My Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Logout");
            System.out.print("> ");

            response = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (response == 1) {
                doSearchHotelRoom();
            } else if (response == 2) {
                doReserveHotelRoom(guest);
            } else if (response == 3) {
//                doViewReservationDetails();
            } else if (response == 4) {
                doViewAllReservations();
            } else if (response == 5) {
                isLoggedIn = false;
                System.out.println("Logged out successfully.\n");
                break;
            } else {
                System.out.println("Invalid option, please try again.\n");
            }
        }
    }

    // ---------- Logged In Guest Functionalities ----------
    
    private void doSearchHotelRoom() {
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
                System.out.println("Total Amount for Stay: " + guestSessionBeanRemote.calculateTotalAmountForStay(roomType.getTypeName(), checkInDate, checkOutDate, requiredRooms) + "\n");
            }
        }
    }
    

    private void doReserveHotelRoom(Guest guest) {
        
        Scanner sc = new Scanner(System.in);
        
        try {
            System.out.println("*** Reserve Hotel Room ***");

            // Step 1: Get check-in, check-out dates and required room count
            System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
            LocalDate checkInDate = getInputDate();

            System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
            LocalDate checkOutDate = getInputDate();

            System.out.print("Enter required room count: ");
            int requiredRooms = sc.nextInt();
            sc.nextLine();

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
                        guestSessionBeanRemote.calculateTotalAmountForStay(roomType.getTypeName(), checkInDate, checkOutDate, requiredRooms));
                    System.out.println();
                }

                // Step 4: Allow the user to select a room type
                System.out.print("Select a room type by entering the number: ");
                int choice = sc.nextInt();
                sc.nextLine();
                System.out.println("SELECTED CHOICE " + choice);

                // Validate the choice
                if (choice < 1 || choice > availableRooms.size()) {
                    System.out.println("Invalid choice. Reservation cancelled.");
                    return;
                }
                
                // Assume guest is a managed entity, and we have created a new Reservation instance
                RoomType selectedRoomType = availableRooms.get(choice - 1);
                String roomTypeName = selectedRoomType.getTypeName();
                BigDecimal totalAmount = guestSessionBeanRemote.calculateTotalAmountForStay(roomTypeName, checkInDate, checkOutDate, requiredRooms);
                RoomType rt = guestSessionBeanRemote.retrieveRoomTypeByName(roomTypeName);

                Reservation newReservation = new Reservation(LocalDate.now(), checkInDate, checkOutDate, totalAmount, ReservationTypeEnum.WALKIN, requiredRooms, null, rt);
                Long guestId = guest.getGuestID();
                        
                // Call the updated createReservation method
                
                Long reservationId = guestSessionBeanRemote.createReservation(guestId, newReservation);

                System.out.println("Room reserved successfully! Reservation ID: " + reservationId + ", Total amount: " + totalAmount);

                
                System.out.println("NEW RESERVATION PERSISTED, ID IS " + newReservation.getReservationID());
                

                System.out.println("Room reserved successfully! Total amount: " + totalAmount);
            }

        } catch (RoomTypeNotFoundException e) {
            System.out.println("Error updating Room Type: " + e.getMessage());
        } catch (GuestNotFoundException e) {
            System.out.println("Error retrieving guest: " + e.getMessage());
        }
    }

//    private void doViewReservationDetails() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("*** View Reservation Details ***\n");
//
//        System.out.print("Enter Reservation ID: ");
//        Long reservationID = scanner.nextLong();
//        scanner.nextLine(); // consume newline
//
//        try {
//            Reservation reservation = guestSessionBeanRemote.retrieveReservationById(reservationID);
//            System.out.println("Reservation Details:\n" + reservation.toString() + "\n");
//        } catch (ReservationNotFoundException e) {
//            System.out.println("Error: " + e.getMessage() + "\n");
//        }
//    }

    private void doViewAllReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** View All Reservations ***\n");

        System.out.print("Enter Guest Email: ");
        String email = scanner.nextLine().trim();

        try {
            List<Reservation> reservations = guestSessionBeanRemote.viewAllReservations(email);
            if (reservations.isEmpty()) {
                System.out.println("No reservations found for the specified email.\n");
            } else {
                System.out.println("Reservations:");
                for (Reservation reservation : reservations) {
                    System.out.println("Reservation ID: " + reservation.getReservationID());
                    System.out.println("Reservation Date: " + reservation.getReservationDate());
                    System.out.println("Check-In Date: " + reservation.getCheckInDate());
                    System.out.println("Check-Out Date: " + reservation.getCheckOutDate());
                    System.out.println("Room Type: " + reservation.getRoomType().getTypeName());
                    System.out.println("Number of Rooms: " + reservation.getNumberOfRooms());
                    System.out.println("Total Amount: $" + reservation.getTotalAmount());
                    System.out.println("Reservation Type: " + reservation.getReservationType());
                    System.out.println("-----------------------------------------");
                }
                System.out.println();
            }
        } catch (GuestNotFoundException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
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


    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.runApp();
    }
}

