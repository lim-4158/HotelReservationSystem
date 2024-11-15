/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsjavaseclient;


import ejb.session.ws.Guest;
import ejb.session.ws.Partner;
import ejb.session.ws.PartnerWebService;
import ejb.session.ws.PartnerWebService_Service;
import ejb.session.ws.Reservation;
import ejb.session.ws.RoomType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
//import ejb.session.ws.LocalDate;

/**
 *
 * @author kevinlim
 */
public class MainApp {

        public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response;

        while (true) {

            System.out.println("Welcome to the Partner Portal of HoRS");

            PartnerWebService_Service service = new PartnerWebService_Service();
            PartnerWebService port = service.getPartnerWebServicePort();  
            System.out.print("input username (not name) : ");
            String username = scanner.nextLine(); 
            System.out.print("input password : ");
            String password = scanner.nextLine();             
            Partner partner = port.partnerLogIn(username, password);            
//            Partner partner = port.partnerLogIn("travela", "travelpassA");
//        
            
            if (partner != null) {
                
                while (true) {
                    System.out.println("");
                    System.out.println("1: Search for rooms");
                    System.out.println("2: Reserve rooms");
                    System.out.println("3: View reservation details");
                    System.out.println("4: View all reservations");
                    System.out.println("5: Exit\n");
                    response = 0;

                    while (response < 1 || response > 5) {
                        System.out.print("> ");

                        if (scanner.hasNextInt()) {
                            response = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            if (response == 1) {
                                searchRoom();
//                                Scanner sc = new Scanner(System.in);

//                                System.out.println("*** Partner Room Search ***");
//
//                                System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
//                                String checkInDate = "2024-12-12";
//
//                                System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
//                                String checkOutDate = "2024-12-13";
//
//                                System.out.print("Enter required room count: ");
//                                int requiredRooms = 1;
//        
//                                // Call the web service to search for available rooms
//                                List<RoomType> availableRooms = port.searchAvailableRoom(checkInDate, checkOutDate, requiredRooms);
//
//                                if (availableRooms == null || availableRooms.isEmpty()) {
//                                    System.out.println("No rooms available for the specified dates and required inventory.");
//                                } else {
//                                    System.out.println("\nAvailable Room Types:");
//                                    for (int i = 0; i < availableRooms.size(); i++) {
//                                        RoomType roomType = availableRooms.get(i);
//                                        System.out.println((i + 1) + ". Room Type: " + roomType.getTypeName());
//                                        // Assuming the web service provides a method to calculate total amount
//                                        BigDecimal totalAmount = port.calculateTotalAmountForStay( roomType.getTypeName(), checkInDate, checkOutDate, requiredRooms);
//                                        System.out.println("   Total Amount for Stay: $" + totalAmount + "\n");
//                                    }
//                                }                                
    
                            } else if (response == 2) {
                                reserveRoom(partner);
                            } else if (response == 3) {
                                viewReservationDetails(partner.getPartnerID());
                            } else if (response == 4) {
                                List<Reservation> reservations = viewAllReservations(partner.getPartnerID());                               
                            } else if (response == 5) {
                                System.out.println("Logging out...");
                                break;
                            } else {
                                System.out.print("Invalid option, please try again!\n");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number between 1 and 5.");
                            scanner.next(); // Consume invalid input
                        }
                    }

                    if (response == 5) {
                        break;
                    }
                }
            } else {
                System.out.println("Invalid username or password. Please try again.\n");
            }
        }
    }
    

    private Partner partnerLogIn() {
        PartnerWebService_Service service = new PartnerWebService_Service();
        PartnerWebService port = service.getPartnerWebServicePort();

        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Partner partner = port.partnerLogIn(username, password);
        if (partner != null) {
            System.out.println("Login successful! Welcome, " + partner.getUsername() + ".");
        } 
        return partner;
    }
    
    private void searchRoom() {
        PartnerWebService_Service service = new PartnerWebService_Service();        
        PartnerWebService port = service.getPartnerWebServicePort();
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Partner Room Search ***");

        System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
        String checkInDate = getInputDate();

        System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
        String checkOutDate = getInputDate();

        System.out.print("Enter required room count: ");
        int requiredRooms = 0;
        while (true) {
            try {
                requiredRooms = Integer.parseInt(sc.nextLine().trim());
                if (requiredRooms <= 0) {
                    System.out.print("Please enter a positive number for required rooms: ");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number for required rooms: ");
            }
        }

        // Call the web service to search for available rooms
        List<RoomType> availableRooms = port.searchAvailableRoom(checkInDate, checkOutDate, requiredRooms);

        if (availableRooms == null || availableRooms.isEmpty()) {
            System.out.println("No rooms available for the specified dates and required inventory.");
        } else {
            System.out.println("\nAvailable Room Types:");
            for (int i = 0; i < availableRooms.size(); i++) {
                RoomType roomType = availableRooms.get(i);
                System.out.println((i + 1) + ". Room Type: " + roomType.getTypeName());
                // Assuming the web service provides a method to calculate total amount
                BigDecimal totalAmount = port.calculateTotalAmountForStay( roomType.getTypeName(), checkInDate, checkOutDate, requiredRooms);
                System.out.println("   Total Amount for Stay: $" + totalAmount + "\n");
            }
        }
    }
    
    private void reserveRoom(Partner partner) {
        Scanner sc = new Scanner(System.in);
        PartnerWebService_Service service = new PartnerWebService_Service(); 
        PartnerWebService port = service.getPartnerWebServicePort();

        System.out.println("*** Partner Room Reservation ***");

        // Step 1: Get check-in, check-out dates and required room count
        
        System.out.print("Enter today's Date (YYYY-MM-DD): ");
        String todayDate = getInputDate();
        
        System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
        String checkInDate = getInputDate();

        System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
        String checkOutDate = getInputDate();

        System.out.print("Enter required room count: ");
        int requiredRooms = 0;
        while (true) {
            try {
                requiredRooms = Integer.parseInt(sc.nextLine().trim());
                if (requiredRooms <= 0) {
                    System.out.print("Please enter a positive number for required rooms: ");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number for required rooms: ");
            }
        }

        // Step 2: Search for available rooms
        List<RoomType> availableRooms = port.searchAvailableRoom(checkInDate, checkOutDate, requiredRooms);

        if (availableRooms == null || availableRooms.isEmpty()) {
            System.out.println("No rooms available for the specified dates and required inventory.");
            return; // Exit the method if no rooms are available
        } else {
            // Step 3: Display available rooms with indices for selection
            System.out.println("\nAvailable Room Types:");
            for (int i = 0; i < availableRooms.size(); i++) {
                RoomType roomType = availableRooms.get(i);
                System.out.println((i + 1) + ". Room Type: " + roomType.getTypeName());
                BigDecimal totalAmount = port.calculateTotalAmountForStay(
                        roomType.getTypeName(), checkInDate, checkOutDate, requiredRooms);
                System.out.println("   Total Amount for Stay: $" + totalAmount + "\n");
            }

            // Step 4: Allow the user to select a room type
            int choice = 0;
            while (true) {
                System.out.print("Select a room type by entering the number: ");
                try {
                    choice = Integer.parseInt(sc.nextLine().trim());
                    if (choice < 1 || choice > availableRooms.size()) {
                        System.out.println("Invalid choice. Please select a number between 1 and " + availableRooms.size() + ".");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            RoomType selectedRoomType = availableRooms.get(choice - 1);
            String roomTypeName = selectedRoomType.getTypeName();

            // Step 5: Calculate total amount for the selected room type
            BigDecimal totalAmount = port.calculateTotalAmountForStay(
                    roomTypeName, checkInDate, checkOutDate, requiredRooms);

            // Step 6: Retrieve or create the Guest
            System.out.print("Enter First Name: ");
            String firstName = sc.nextLine().trim();

            System.out.print("Enter Last Name: ");
            String lastName = sc.nextLine().trim();

            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();

            System.out.print("Enter Phone Number: ");
            String phoneNumber = sc.nextLine().trim();

            // Check if guest already exists
            Guest existingGuest = port.findGuestByEmail(email);
            Guest guest;
            if (existingGuest == null) {
                guest = new Guest();
                guest.setFirstName(firstName);
                guest.setLastName(lastName);
                guest.setEmail(email);
                guest.setPhoneNumber(phoneNumber);
                System.out.println("New guest created for the reservation.");
            } else {
                guest = existingGuest;
                System.out.println("Existing guest found with email: " + email);
            }

            // Persist the reservation via web service
            port.reserveRoom(
                    todayDate, 
                    checkInDate,
                    checkOutDate,
                    totalAmount,
                    requiredRooms,
                    guest,
                    selectedRoomType,
                    partner
            );

            System.out.println("Room reserved successfully! Total amount: $" + totalAmount);
        }
    }
    
    public List<Reservation> viewAllReservations(Long partnerId) {
        PartnerWebService_Service service = new PartnerWebService_Service(); 
        PartnerWebService port = service.getPartnerWebServicePort();
        List<Reservation> reservations = port.viewAllReservations(partnerId);
        displayReservations(reservations);
        return reservations;
    }
    
    public void viewReservationDetails(Long partnerId) {

        List<Reservation> reservations = viewAllReservations(partnerId); 
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Select a reservation: ");
        int choice = sc.nextInt(); 
        
        Reservation reservation = reservations.get(choice - 1); 
        
        PartnerWebService_Service service = new PartnerWebService_Service(); 
        PartnerWebService port = service.getPartnerWebServicePort();
        
        Reservation r = port.viewReservation(reservation.getReservationID());
        
        displayReservation(r);
        
    }
    
    public static String getInputDate() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Ensure strict parsing

        while (true) {
            String input = scanner.nextLine();
            try {
                // Parse to validate the date format
                dateFormat.parse(input);
                // Return the input string directly if it's valid
                return input;
            } catch (ParseException e) {
                System.out.print("Invalid date format. Please enter the date in YYYY-MM-DD format: ");
            }
        }
    }

    private void displayReservations(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        System.out.println("\n--- Your Reservations ---");
        int i = 1; 
        for (Reservation r : reservations) {
            System.out.println(i + " : " + r.getCheckInDate().toString() + " --- " + 
                    r.getCheckOutDate() + " " + r.getGuest().getFirstName()+ " " + r.getGuest().getEmail());
            i++; 
            System.out.println("----------------------------");
        }
    }    
    
    private void displayReservation(Reservation reservation) {
//        System.out.println("Reservation ID: " + reservation.getReservationID());
        System.out.println("Reservation Date: " + reservation.getReservationDate().toString());
        System.out.println("Check-in Date: " + reservation.getCheckInDate().toString());
        System.out.println("Check-out Date: " + reservation.getCheckOutDate().toString());
//        System.out.println("Reservation Type: " + reservation.getReservationType());
        System.out.println("Guest Name: " + reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName());
        System.out.println("Room Type: " + reservation.getRoomType().getTypeName());
        System.out.println("Total Amount: $" + reservation.getTotalAmount());
    }    
    
}
