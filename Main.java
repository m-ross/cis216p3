package lab03;

import java.util.Scanner;
import java.io.*;

/*  Program Name:   Lab 03 Salesperson Travel Report and Log
 *  Programmer:     Marcus Ross
 *  Date Due:       27 September 2013
 *  Description:	This program asks the salesperson user their name, then gives them with a choice between adding a trip to the log, generating a monthly report based on the log, starting a new monthly log, switching to a different salesperson (and therefore different sales log file name), or quitting.
 */

public class Main {
    public static void main(String[] args) throws IOException {

		//declare vars
		String user, cust, tripDate, temp;
		double miles, fuel, miscExpense, milesTotal, fuelTotal, miscExpenseTotal;
		char choice = '0';
		Scanner inFile, kbd = new Scanner(System.in);
		FileWriter outFileW;
		File inFileR;
		PrintWriter outFile;

		do { //Get user name, only once before first loop
			System.out.print("\nSalesperson: ");
			user = kbd.nextLine();
			if (user.isEmpty())
				System.out.println("Invalid entry");
		} while (user.isEmpty());
		
		do { //program returns here to loop unless user chooses to quit
			
			while (choice=='c') { //Get user name again if user chooses
				System.out.print("\nSalesperson: ");
				user = kbd.nextLine();
				while (user.isEmpty()) { //ask again if no input
					System.out.println("Invalid entry");
					System.out.print("\nSalesperson: ");
					user = kbd.nextLine();
				}
				break; //go to menu after successful name get
			}

			do { //Get user choice
				System.gc(); //I don't know how you tested it, but gc unambiguously works for me! With it in the loop like so, memory usage drops every time it loops and climbs otherwise
				System.out.println("\n(A)dd a trip to the sales log");
				System.out.println("(G)enerate a monthly report");
				System.out.println("(S)tart a new month");
				System.out.println("(C)hange user");
				System.out.println("(Q)uit");
				System.out.print("Enter choice: ");
				temp = kbd.nextLine();
				if (!temp.isEmpty()) {
					choice = temp.toLowerCase().charAt(0); //take only lower case of first char
					if (choice=='a' || choice=='g' || choice=='s' || choice=='c' || choice=='q');
						break; //if valid choice found, exit menu
				}
				System.out.println("Invalid entry");
			} while (choice!='a' && choice!='g' && choice!='s' && choice!='c' && choice!='q');

			//Perform choice
			if (choice=='a') { //Add a trip to sales log
				do {
					System.out.print("\nCustomer: ");
					cust = kbd.nextLine();
					if (cust.isEmpty())
						System.out.println("Invalid entry");
				} while (cust.isEmpty())

				System.out.print("Trip Date: ");
				tripDate = kbd.nextLine();
				while (tripDate.isEmpty()) { //ask again if bad input
					System.out.println("Invalid entry");
					System.out.print("Trip Date: ");
					tripDate = kbd.nextLine();
				}

				while (true) {
					try {
						System.out.print("Miles travelled: ");
						temp = kbd.nextLine();
						miles = Double.parseDouble(temp);
						break;
					}
					catch (Exception e) {
						System.out.println("Invalid entry");
					}
				}

				System.out.print("Gas used: ");
				temp = kbd.nextLine();
				while (temp.isEmpty() || !temp.matches("\\d+|\\.\\d+|\\d+\\.\\d+|\\d+\\.")) { //think I could shorten this string (in matches)?
					System.out.println("Invalid entry");
					System.out.print("Gas used: ");
					temp = kbd.nextLine();
				}
				fuel = Double.parseDouble(temp);

				do {
					System.out.print("Misc. expenses: ");
					temp = kbd.nextLine();
					if (temp.matches("\\d+|\\.\\d+|\\d+\\.\\d+|\\d+\\."))
						break;
					System.out.println("Invalid entry");
				} while (true);
				miscExpense = Double.parseDouble(temp);

				//Display trip info to sales log file
				outFileW = new FileWriter("lab03\\" + user + ".txt",true);
				outFile = new PrintWriter(outFileW);
				outFile.println(cust);
				outFile.println(tripDate);
				outFile.printf("%1.2f\n",miles);
				outFile.printf("%1.2f\n",fuel);
				outFile.printf("%1.2f\n",miscExpense);
				outFile.close();
				choice = '0'; //return to menu
			}
			else
				if (choice=='g') { //Generate a monthly report
					milesTotal = 0;// reset totals in case of repeated use
					fuelTotal = 0;
					miscExpenseTotal = 0;
					inFileR = new File("lab03\\" + user + ".txt");
					if (!inFileR.exists()) { //return to menu if no file found
						System.out.printf("\nNo sales log found for user %s\n",user);
						continue;
					}
					System.out.printf("\n┬──────────────────────────────┬\n");
					System.out.printf("│%25s%5s│\n","Culver City Meat Co.","");
					System.out.printf("│%26s%4s│\n│%30s│\n","You can't beat our meat","","");
					System.out.printf("│%s%-17.17s│\n│%30s│\n","Salesperson: ",user,"");

					//Process each trip
					inFile = new Scanner(inFileR);
					while (inFile.hasNext()) {
						//Get trip data from sales log file
						cust = inFile.nextLine();
						tripDate = inFile.nextLine();
						miles = inFile.nextDouble();
						fuel = inFile.nextDouble();
						miscExpense = inFile.nextDouble();
						inFile.nextLine();

						//Calculate trip totals
						milesTotal += miles;
						fuelTotal += fuel;
						miscExpenseTotal += miscExpense;

						//Display trip report
						System.out.printf("│%s%20s│\n","Customer: ",cust);
						System.out.printf("│%s%16s│\n","Date of Trip: ",tripDate);
						System.out.printf("│%-17s%13.2f│\n","Miles travelled: ",miles);
						System.out.printf("│%-17s%9.2f %s│\n","Gas used: ",fuel,"gal");
						System.out.printf("│%-17s$%12.2f│\n│%30s│\n","Misc. expenses: ",miscExpense,"");
					}
					inFile.close();

					//Display trip totals
					System.out.printf("│%-17s%13.2f│\n","Total miles: ",milesTotal);
					System.out.printf("│%-17s%9.2f %s│\n","Total gas: ",fuelTotal,"gal");
					System.out.printf("│%-17s$%12.2f│\n","Total expenses: ",miscExpenseTotal);
					System.out.printf("┴──────────────────────────────┴\n");

					choice = '0'; //return to menu
				}
				else
					if (choice=='s') { //Start a new month
						System.out.print("\nAll data in existing log will be lost\nAre you sure? Y/N: ");
						temp = kbd.nextLine();
						while (temp.isEmpty()) { //ask again if no input
							System.out.println("Invalid entry.");
							temp = kbd.nextLine();
						}
						choice = temp.toLowerCase().charAt(0); //take only lower case of first char
						if (choice == 'y') {
							outFile = new PrintWriter("lab03\\" + user + ".txt");
							outFile.close();
							System.out.println("Task completed.");
						}
						else
							System.out.println("Task aborted.");
						choice = '0'; //return to menu
					}
		} while (choice!='q');
	}
}