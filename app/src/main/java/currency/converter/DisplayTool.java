package currency.converter;

import java.util.List;

public class DisplayTool {
    static void displayTitle() {
        System.out.println("---------------------------");
        System.out.println("-----CURRENCY CONVERTER----");
        System.out.println("---------------------------");
        System.out.println("Welcome to the SOFT2412 currency converter!");
        System.out.println("Refer to the readme file for help, or enter the command 'help'.");
    }

    static void displayPopular(String[][] values, List<String> popularCurrencies) {
        System.out.println("From/To\t" + popularCurrencies.get(0) + "      " + popularCurrencies.get(1) + "      "
                + popularCurrencies.get(2) + "      " + popularCurrencies.get(3));
        for (int i = 0; i < popularCurrencies.size(); i++) {
            System.out.print(popularCurrencies.get(i) + " \t");
            for (int j = 0; j < popularCurrencies.size(); j++) {
                if (values[i][j].equals("-")) {
                    System.out.print(values[i][j] + "        ");
                } else {
                    System.out.print(values[i][j] + " ");
                }

            }
            System.out.println();
        }
    }

    static void displayHelp() {
        System.out.println("The following commands are available:");
        System.out.println("convert [curr1] [curr2] [amount] - converts currency amount from curr1 to curr2");
        System.out.println("display - displays the most popular conversions");
        System.out.println(
                "update [curr1] [curr2] [new-rate] - updates the currency conversion from curr1 to curr2 with new-rate");
        System.out.println(
                "update-popular [curr1] [curr2] [curr3] [curr4] - updates the current popular currencies to the four user inputs");
        System.out.println("add [curr1] - adds a new exchange rate");
        System.out.println(
                "summary [curr1] [curr2] [start-date] [end-date] - displays the conversion history of curr1 to curr2, with date given in dd-mm-yyyy format");
        System.out.println("exit - exits the program");
        System.out.println("help - displays this help menu");
    }
}
