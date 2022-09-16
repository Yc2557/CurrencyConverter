/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package currency.converter;

import java.util.Scanner;
import java.util.List;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

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
                    // Converts one currency to the other, and prints the result
                    if (input_list.length == 4) {
                        String currCurrency = input_list[1];
                        String newCurrency = input_list[2];
                        float amount = Float.parseFloat(input_list[3]);
                        float currency = handler.convertCurrency(amount, currCurrency, newCurrency);
                        System.out.println(
                                "The conversion of " + amount + currCurrency + " is: " + currency + " " + newCurrency);
                    } else {
                        System.out.println("Invalid number of arguments");
                    }
                case "display":
                    // Displays popular results
                    String[][] values = handler.displayPopular();
                    List<String> popularCurrencies = handler.getPopularCurrencies();
                    DisplayTool.displayPopular(values, popularCurrencies);

                case "update":
                    // Updates dataset
                    if (input_list.length == 4) {
                        String currCurrency = input_list[1];
                        String newCurrency = input_list[2];
                        float newRate = Float.parseFloat(input_list[3]);
                        handler.updateCurrency(currCurrency, newCurrency, newRate, LocalDate.now());
                    } else {
                        System.out.println("Invalid number of arguments");
                    }

                case "update-popular":
                    // Updates popular currencies
                    if (input_list.length == 5) {
                        String curr1 = input_list[1];
                        String curr2 = input_list[2];
                        String curr3 = input_list[3];
                        String curr4 = input_list[4];
                        handler.updatePopular(curr1, curr2, curr3, curr4);
                    } else {
                        System.out.println("Invalid number of arguments");
                    }
                case "add":
                    // Add exchange rate
                    if (input_list.length == 2) {
                        String currCurrency = input_list[1];
                        handler.addCurrency(currCurrency);
                    } else {
                        System.out.println("Invalid number of arguments");
                    }
                case "summary":
                    // Print out conversion history of two currencies
                    if (input_list.length == 3) {
                        String currCurrency = input_list[1];
                        String newCurrency = input_list[2];
                        String start = input_list[3];
                        String end = input_list[4];

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
                        LocalDate startDate = LocalDate.parse(start, formatter);
                        LocalDate endDate = LocalDate.parse(end, formatter);
                        handler.printConversionHistory(currCurrency, newCurrency, startDate, endDate);
                    } else {
                        System.out.println("Invalid number of arguments");
                    }
                case "exit":
                    System.out.println("Have a good day!");
                    exitFlag = true;
                    break;
                case "help":
                    DisplayTool.displayHelp();
                default:
                    System.out.println("The command you've entered is invalid.");
            }

        } while (!exitFlag);

        sc.close();
    }
}
