/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import entity.Guest;
import java.util.Scanner;

/**
 *
 * @author jamiewee
 */
import java.util.Scanner;

public class MainApp {

    private boolean isLoggedIn = false;

    public MainApp() {
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
                doGuestLogin();
                if (isLoggedIn) {
                    guestMenu();
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

    public void doGuestLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Guest Login ***\n");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // Implement login logic with guestSessionBeanRemote
        // Example:
        // Guest guest = guestSessionBeanRemote.guestLogin(username, password);
        // if (guest != null) {
        //     System.out.println("Login successful!");
        //     isLoggedIn = true;
        // } else {
        //     System.out.println("Invalid credentials.");
        // }

        // For demonstration, we assume login is successful
        isLoggedIn = true;
        System.out.println("Login successful!\n");
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

        // Implement registration logic here with guestSessionBeanRemote
        System.out.println("Registration successful!\n");
    }

    public void guestMenu() {
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
                doReserveHotelRoom();
            } else if (response == 3) {
                doViewReservationDetails();
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

    private void doSearchHotelRoom() {
        System.out.println("Searching hotel room...\n");
        // Implement search logic
    }

    private void doReserveHotelRoom() {
        System.out.println("Reserving hotel room...\n");
        // Implement reservation logic
    }

    private void doViewReservationDetails() {
        System.out.println("Viewing reservation details...\n");
        // Implement logic to view specific reservation details
    }

    private void doViewAllReservations() {
        System.out.println("Viewing all reservations...\n");
        // Implement logic to view all reservations for the guest
    }

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.runApp();
    }
}

