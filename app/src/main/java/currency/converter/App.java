/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package currency.converter;

import java.time.LocalDate;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Check admin

        // Input Loop from user
        CurrencyHandler handler = new CurrencyHandler();
        Scanner sc = new Scanner(System.in);

        String input = "";

        while (sc.hasNextLine()) {
            input = sc.nextLine();

            // Break up input
            String[] input_list = input.split(" ");
            String command = input_list[0];

            switch (command) {
                case "convert":
                    System.out.println("Prints");
                case "exit":
                    System.out.println("Have a good day!");
                    sc.close();
                    break;
            }

        }
    }
}
