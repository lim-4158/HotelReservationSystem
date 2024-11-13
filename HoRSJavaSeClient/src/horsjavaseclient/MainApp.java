/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsjavaseclient;

import java.util.Scanner;
import ws.partner.Partner;

/**
 *
 * @author kevinlim
 */
public class MainApp {

    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response;
        
        while(true) {
            
            System.out.println("Welcome to the Partner Portal of HoRS");
            //login method
            
            
        
            while(true)
            {
                System.out.println("*** Welcome to IS2103 Lecture 10 Java SE Client ***\n");
                System.out.println("1: Demo Consume SOAP Web Service - retrieveAllProducts");
                System.out.println("2: Demo Consume SOAP Web Service - retrieveProductByProductId");
                System.out.println("3: Demo Cyclic Reference");
                System.out.println("4: Exit\n");
                response = 0;

                while(response < 1 || response > 4)
                {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if(response == 1)
                    {

                    }
                    else if (response == 2)
                    {
                        
                    }
                    else if (response == 3)
                    {

                    }
                    else if (response == 4)
                    {
                        break;
                    }
                    else
                    {
                        System.out.print("Invalid option, please try again!\n");                
                    }
                }

                if(response == 4)
                {
                    break;
                }
            }
        }
    }
    
    private Partner partnerLogIn() {
        return new Partner(); 
    }
    
}
