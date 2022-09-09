/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package currency.converter;

import java.time.LocalDate;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Initialise variables
        Scanner sc = new Scanner(System.in);
        String input = "";
        Boolean exitFlag = false;
        Boolean isAdmin = false;

        // Set user to admin if command arg given
        if (args.length == 0) {
            isAdmin = false;

        } else if (args[0] == "admin") {
            isAdmin = true;
        }
        CurrencyHandler handler = new CurrencyHandler(isAdmin);

        // Print out entry message
        DisplayTool.displayTitle();

        // Command Line Loop
        do {
            System.out.println("Please input your command: ");
            input = sc.nextLine();

            // Break up input
            String[] input_list = input.split(" ");
            String command = input_list[0];

            switch (command) {
                case "convert":
                    System.out.println("Prints");
                    break;
                case "display":
                    // Displays popular results
                
                case "update":
                    // Updates dataset
                case "add":
                    // Add exchange rate
                case "summary":
                    // Print out conversion history of two currencies
                case "exit":
                    System.out.println("Have a good day!");
                    exitFlag = true;
                    break;
                case "help":
                    System.out.println("help menu here.");
                default:
                    System.out.println("The command you've entered is invalid.");
            }

        } while (!exitFlag);

        Scanner s = new Scanner(System.in);
    }
}
