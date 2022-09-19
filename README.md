# SOFT2412 Assignment 1 Currency Converter
*Built by Yu Hong Chen, Matan Cohen, Michael Hitchcock, Kevin Hou, Kevin Kong*

Java-based basic command-line currency converter. The converter enables users to convert between currencies, add/update currencies and conversions in a database, and display popular currencies and transaction histories. Data persists between loads, and is stored in a .json file.

## Use Notes
To run the program, use the following (add code inside square brackets if running in admin mode):
```
gradle run [--args=”admin”]
```
To run the GUI use:
```
gradle run --args=”gui”
```
To run test cases on the program, use the following:
```
gradle test
```
Or, to generate jacoco reports use:
```
gradle build jacocoTestReport
```

## Program Commands
The program accepts the following commands:
- convert [curr1] [curr2] [amount] - converts currency amount from curr1 to curr2
- display - displays the most popular conversions
- update [curr1] [curr2] [new-rate] - updates the currency conversion from curr1 to curr2 with new-rate. Note: one of the currencies must be the base currency “AUD”.
- update-popular [curr1] [curr2] [curr3] [curr4] - updates the current popular currencies to the four user inputs
- add [curr1] - adds a new exchange rate
- summary [curr1] [curr2] [start-date] [end-date] - displays the conversion history of curr1 to curr2, with date given in dd-mm-yyyy format.
- exit - exits the program
- help - displays this help menu

## Important Notes
Some things to note regarding our implementation:
- By default, our program has the following six currencies; AUD, USD, EUR, SGD, NZD, CNY. AUD is used as the base currency, and thus all conversions are done via AUD.
- If there is no prior history, the program will show that conversion has decreased.
- If admin user is adding in new conversions, all conversion dates will be set as the current date.
- If the currency has no data (e.g. a new currency is added during program) the conversion to and from the currency to other currencies is 0.
