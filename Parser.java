/*
 * Java Programming Assignment #2  Spring 2024 – SMASM Parser (Strict Provided Grammar Version, Recursive Descent Parser)
 * Description: This program reads a file of tokens, parses the tokens based on grammar rules, and notifies the user if the program is legal or not.
 *              The program reads from a file named "Prog4TMTokens.txt" containing tokens and the program calls multiple functions that act as the
 *              grammar rules to validate and parse the tokens. At the end, it outputs whether the tokens in the file are legal or not based on the grammar provided below.
 *              The program also prints to the terminal and logs to the output file throughout the process for tracking the flow of execution. This is a recursive descent parser.
 * 
 *              Grammar rules (EBNF):
 *              START -> { COMMANDS }
 *              COMMANDS -> COMMAND ‘[‘ (REGISTER | VALUE )  [ ‘,’ REGISTER] ‘]’
 *              COMMAND -> ‘st’ | ‘ld’ | ‘sub’ | ‘add’ | ‘sq’  | ‘rt’ 
 *              REGISTER -> ‘a’ | ‘b’ | ‘c’ | ‘d’ | ‘e’ | ‘f’
 *              VALUE -> DIGITS
 *              DIGIT -> ‘0’ | .. | ‘9’
 *              DIGITS -> DIGIT {DIGIT}
 * 
 * Date Created: 4/26/2024
 * Last Modified: 4/30/2024
 * CS-310 Programming Languages
 * Thomas McLaughlin
*/

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList; // Import the ArrayList class
import java.util.List; // Import the List class
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.PrintWriter; // Import the PrintWriter class to write text files

public class Parser {
    public static void main(String[] args) {
        /*
         * Method: public static void main(String [] args)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method serves as the entry point of the Parser program. It initializes the file streams for reading the input token file as well as the writing output file.
         *              This method reads from a file named "Prog4TMTokens.txt" containing tokens and the program calls multiple functions that act as the grammar rules to validate and parse
         *              the tokens. At the end, it outputs whether the tokens in the file are legal or not based on the grammar provided below. The method prints to the terminal and logs
         *              to the output file throughout the process for tracking the flow of execution.
         * 
         *              Grammar rules (EBNF):
         *              START -> { COMMANDS }
         *              COMMANDS -> COMMAND ‘[‘ (REGISTER | VALUE )  [ ‘,’ REGISTER] ‘]’
         *              COMMAND -> ‘st’ | ‘ld’ | ‘sub’ | ‘add’ | ‘sq’  | ‘rt’ 
         *              REGISTER -> ‘a’ | ‘b’ | ‘c’ | ‘d’ | ‘e’ | ‘f’
         *              VALUE -> DIGITS
         *              DIGIT -> ‘0’ | .. | ‘9’
         *              DIGITS -> DIGIT {DIGIT}
         * 
         *             - No direct left recursion in the grammar rules
         *             - Passes the pairwise disjointness test
         * 
         * 
         * Variables: 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * tokenPath - the path to the input file
         * outputPath - the path to the output file
         * parseIndication - a boolean to indicate if the program derived from the tokens is legal or not
         * Arguments:
         * args - the command line arguments
         * Methods Called:    START() - evaluates the START grammar rule
         *                    close() - closes the input file
         *                    close() - closes the output file
         * Returns:           void
        */
        
        Scanner inStream = null; // initialize read
        PrintWriter outStream=null; // initialize write
        String tokenPath = "Prog4TMTokens.txt"; // path to input file of tokens
        String outputPath = "Prog4TMout.txt"; // path to write output file

        //try/catch for accessing input file to account for errors
        try
        {
            System.out.println("Checking the file " + tokenPath + " to see if it is a syntactically correct SMASM Language program.");
            inStream = new Scanner(new File(tokenPath));
        } // end try
        catch(FileNotFoundException e)
        {
            System.out.println("Error opening the file " + tokenPath);
            System.exit(0);
        }

        //try/catch for accessing output file to account for errors
        try
        {
            System.out.println("Opening the file " + outputPath);
            outStream = new PrintWriter(outputPath);
        } // end try
        catch(FileNotFoundException e)
        {
            System.out.println("Error opening the file " + outputPath);
            System.exit(0);
        } // end catch
        
        Boolean parseIndication = START(inStream, outStream); // call the START method to parse the tokens in the input token file, value is stored in parseIndication

        if(parseIndication){ // if the program is legal | True
            System.out.println("The program in the input file successfully parsed and is a legal program!");
        }
        else{ // if the program is not legal | False
            System.out.println("The program in the input file did not successfully parse and is not a legal program!");
        }

        inStream.close(); // close the input file
        outStream.close(); // close the output file
    } // end main

    public static boolean START(Scanner inStream,PrintWriter outStream) {
        /*
         * Method: public static boolean START(Scanner inStream,PrintWriter outStream)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method evaluates the START grammar rule. This method initiates the first grammar rule which indicates the non-terminal symbol START can have 0 or more COMMANDS.
         *              While there are more lines in the input file, the START method will call the COMMANDS method to evaluate the COMMANDS grammar rule. If the COMMANDS method returns
         *              false, the START method will return false signifying the program in the input file is not a legal program.
         *              The method prints to the terminal and logs to the output file to track the flow of execution.
         * 
         *              START -> { COMMANDS } 
         * 
         * Variables: 
         * instream - for reading from the input file
         * outStream - for writing to the output file
         * Arguments:
         * instream - for reading from the input file
         * outStream - for writing to the output file
         * Methods Called:    COMMANDS() - evaluates the COMMANDS grammar rule
         *                    hasNextLine() - checks if there is another line in the input file
         * Returns:           true if the tokens conforms to the START grammar rule (which calls COMMANDS), otherwise it returns false
        */

        System.out.println("Entering function: START");
        outStream.println("Entering function: START");

        while (inStream.hasNextLine()) { // while there are more lines in the input file
            if (!COMMANDS(inStream, outStream)) { // if the COMMANDS method returns false
                System.out.println("Leaving function: START Unsuccessful");
                outStream.println("Leaving function: START Unsuccessful");
                return false;
            }   
        }

        System.out.println("Leaving function: START Success");
        outStream.println("Leaving function: START Success");
        return true;
    } // end START

    public static boolean COMMANDS(Scanner inStream, PrintWriter outStream) {
        /*
         * Method: public static boolean COMMANDS(Scanner inStream, PrintWriter outStream) {
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method evaluates the COMMANDS grammar rule: which is a non-terminal symbol of COMMAND, followed by a terminal symbol of '[' and then either a terminal symbol of REGISTER or VALUE
         *              followed by an optional portion of a terminal symbol of ',' and then a terminal symbol of REGISTER, and always end by a terminal symbol of ']'.    The  ", REGISTER" is optional.
         *              The method prints to the terminal and logs to the output file to track the flow of execution.
         * 
         *              COMMANDS -> COMMAND ‘[‘ (REGISTER | VALUE )  [ ‘,’ REGISTER] ‘]’
         * 
         * Variables: 
         * instream - for reading from the input file
         * outStream - for writing to the output file
         * command - the command token read from the input file to be evaluated
         * nextLine - the next line in the input file to be evaluated, used in try catches for the (expected) terminals : '[' , ']', and ','
         * registerValueLine - the (expected) register or value token read from the input file to be evaluated
         * Arguments:
         * instream - for reading from the input file
         * outStream - for writing to the output file
         * Methods Called:    COMMAND() - evaluates the COMMAND grammar rule
         *                    REGISTER() - evaluates the REGISTER grammar rule
         *                    VALUE() - evaluates the VALUE grammar rule
         *                    getNextLine() - reads the next line in the input file
         *                    toLower() - converts the input string to lowercase
         * Returns:           true if the tokens conforms to the COMMANDS grammar rule (which calls other grammar rules), otherwise it returns false
        */

        System.out.println("Entering function: COMMANDS");
        outStream.println("Entering function: COMMANDS");

        String command = toLower(getNextLine(inStream)); // get the next line in the input file and convert it to lowercase

        if (COMMAND(inStream, outStream, command)) { // if the COMMAND method returns true given the command token
            try {
                String nextLine = getNextLine(inStream); // get the next line in the input file
                if (!nextLine.equals("[")) { // if the next line is not '[' return false, that is not valid
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                } 
            } catch (NullPointerException e) { // if there is no next line, return false
                System.out.println("Leaving function: COMMANDS Unsuccessful");
                outStream.println("Leaving function: COMMANDS Unsuccessful");
                return false;
            }
                
            String registerValueLine = toLower(getNextLine(inStream)); // get the next line in the input file after the bracket and convert it to lowercase

            if (REGISTER(outStream, registerValueLine) == 1) { // if the REGISTER method returns 1 (Successful) given the registerValueLine
                
                //optional part of the grammar
                try {
                    String nextLine = getNextLine(inStream);
                    if(nextLine.equals("]")){ // check if end early before the optional portion
                        System.out.println("Leaving function: COMMANDS Success");
                        outStream.println("Leaving function: COMMANDS Success");
                        return true;
                    }
                    else if( !nextLine.equals(",")){ // any other character than ',' at this point is invalid | initiating the optional portion
                        System.out.println("Leaving function: COMMANDS Unsuccessful");
                        outStream.println("Leaving function: COMMANDS Unsuccessful");
                        return false;
                    }
                } catch (NullPointerException e) { // if there is no next line, return false
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                }

                if( REGISTER(outStream, toLower(getNextLine(inStream))) == 1) { // if the REGISTER method returns 1 (Successful) given the registerValueLine

                    try {
                        String nextLine = getNextLine(inStream);
                        if (nextLine.equals("]")) { // if the next line is the end bracket, return true
                            System.out.println("Leaving function: COMMANDS Success");
                            outStream.println("Leaving function: COMMANDS Success");
                            return true;
                        } 
                        else { // if the next line is not the end bracket, return false
                            System.out.println("Leaving function: COMMANDS Unsuccessful");
                            outStream.println("Leaving function: COMMANDS Unsuccessful");
                            return false;
                        }
                    } catch (NullPointerException e) { // if there is no next line, return false
                        System.out.println("Leaving function: COMMANDS Unsuccessful");
                        outStream.println("Leaving function: COMMANDS Unsuccessful");
                        return false;
                    }
                }
                else { // if the REGISTER method returns 0 (Unsuccessful) given the registerValueLine, return false
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                }
            }
            else if (VALUE(outStream, registerValueLine) == 2) { // if the VALUE method returns 2 (Successful) given the registerValueLine
                //optional part of the grammar
                try {
                    String nextLine = getNextLine(inStream);
                    if(nextLine.equals("]")){ // check if end early before the optional portion
                        System.out.println("Leaving function: COMMANDS Success");
                        outStream.println("Leaving function: COMMANDS Success");
                        return true;
                    }
                    else if( !nextLine.equals(",")){ // any other character than ',' at this point is invalid | initiating the optional portion
                        System.out.println("Leaving function: COMMANDS Unsuccessful");
                        outStream.println("Leaving function: COMMANDS Unsuccessful");
                        return false;
                    }
                } catch (NullPointerException e) { // if there is no next line, return false
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                }

                if( REGISTER(outStream, toLower(getNextLine(inStream))) == 1) { // if the REGISTER method returns 1 (Successful) given the registerValueLine

                    try {
                        String nextLine = getNextLine(inStream);
                        if (nextLine.equals("]")) { // if the next line is the end bracket, return true
                            System.out.println("Leaving function: COMMANDS Success");
                            outStream.println("Leaving function: COMMANDS Success");
                            return true;
                        } 
                        else { // if the next line is not the end bracket, return false
                            System.out.println("Leaving function: COMMANDS Unsuccessful");
                            outStream.println("Leaving function: COMMANDS Unsuccessful");
                            return false;
                        }
                    } catch (NullPointerException e) { // if there is no next line, return false
                        System.out.println("Leaving function: COMMANDS Unsuccessful");
                        outStream.println("Leaving function: COMMANDS Unsuccessful");
                        return false;
                    }
                }
                else { // if the REGISTER method returns 0 (Unsuccessful) given the registerValueLine, return false
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                }
            }
            else { // if the REGISTER method returns 0 (Unsuccessful) given the registerValueLine, return false
                System.out.println("Leaving function: COMMANDS Unsuccessful");
                outStream.println("Leaving function: COMMANDS Unsuccessful");
                return false;
            }
        }

        System.out.println("Leaving function: COMMANDS Unsuccessful");
        outStream.println("Leaving function: COMMANDS Unsuccessful");
        return false;
    } // end COMMANDS

    public static boolean COMMAND(Scanner inStream, PrintWriter outStream, String command) {
        /*
         * Method: public static boolean COMMAND(Scanner inStream, PrintWriter outStream, String command)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method evaluates the COMMAND grammar rule: which is a terminal symbol of 'st', 'ld', 'sub', 'add', 'sq', or 'rt'.
         *              This method compares the command token to the valid command tokens and returns true if it is a valid command token, otherwise it returns false.
         *              The method prints to the terminal and logs to the output file to track the flow of execution.
         * 
         *              COMMAND -> ‘st’ | ‘ld’ | ‘sub’ | ‘add’ | ‘sq’  | ‘rt’ 
         * 
         * Variables: 
         * instream - for reading from the input file
         * outStream - for writing to the output file
         * command - the command token read from the input file to be evaluated
         * Arguments:
         * instream - for reading from the input file
         * outStream - for writing to the output file
         * command - the command token read from the input file to be evaluated
         * Methods Called:    equals() - compares the command token to the valid command tokens
         * Returns:           true if the command conforms to the COMMAND grammar rule, otherwise it returns false
        */

        System.out.println("Entering function: COMMAND");
        outStream.println("Entering function: COMMAND");

        // checking if command is a valid command token
        if (command.equals("st") || command.equals("ld") || command.equals("sub") || command.equals("add") || command.equals("sq") || command.equals("rt")) {
            System.out.println("Leaving function: COMMAND Success");
            outStream.println("Leaving function: COMMAND Success");
            return true;
        } else { // if command is not a valid command token
            System.out.println("Leaving function: COMMAND Unsuccessful");
            outStream.println("Leaving function: COMMAND Unsuccessful");
            return false;
        }
    } // end COMMAND

    public static int REGISTER(PrintWriter outStream, String registerValueLine) {
        /*
         *public static int REGISTER(PrintWriter outStream, String registerValueLine)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method evaluates the REGISTER grammar rule: which is a terminal symbol of 'a', 'b', 'c', 'd', 'e', or 'f'.
         *              This method compares the registerValueLine to the valid register tokens and returns 1 if it is a valid register token, otherwise it returns 0.
         *              The method prints to the terminal and logs to the output file to track the flow of execution.
         * 
         *              REGISTER -> ‘a’ | ‘b’ | ‘c’ | ‘d’ | ‘e’ | ‘f’
         * 
         * Variables: 
         * outStream - for writing to the output file
         * registerValueLine - the register or value token to be evaluated
         * Arguments:
         * outStream - for writing to the output file
         * registerValueLine - the register or value token to be evaluated
         * Methods Called:    equals() - compares the registerValueLine to the valid register tokens
         * Returns:           1 if the registerValueLine conforms to the REGISTER grammar rule, otherwise it returns 0
        */

        System.out.println("Entering function: REGISTER");
        outStream.println("Entering function: REGISTER");

        // checking if the registerValueLine passed to the REGISTER method is a valid register token
        if (registerValueLine.equals("a") || registerValueLine.equals("b") || registerValueLine.equals("c") || registerValueLine.equals("d") || registerValueLine.equals("e") || registerValueLine.equals("f")) {
            System.out.println("Leaving function: REGISTER Success");
            outStream.println("Leaving function: REGISTER Success");
            return 1;
        }
        else { // if the registerValueLine is not a valid register token
            System.out.println("Leaving function: REGISTER Unsuccessful");
            outStream.println("Leaving function: REGISTER Unsuccessful");
            return 0;
        }
    } // end REGISTER

    public static int VALUE(PrintWriter outStream, String registerValueLine) {
        /*
         * Method: public static int VALUE(PrintWriter outStream, String registerValueLine)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method evaluates the VALUE grammar rule: which is a non-terminal symbol of DIGITS.
         *              This method checks the result of the DIGITS method to determine if the registerValueLine is a valid digit token or not.
         *              The method prints to the terminal and logs to the output file to track the flow of execution.
         * 
         *              VALUE -> DIGITS
         * 
         * Variables:
         * outStream - for writing to the output file
         * registerValueLine - the register or value token to be evaluated (not necessarily a digit, but the method will check if it is a digit or not)
         * Arguments:
         * outStream - for writing to the output file
         * registerValueLine - the register or value token to be evaluated (not necessarily a digit, but the method will check if it is a digit or not)
         * Methods Called:    DIGITS() - evaluates the DIGITS grammar rule
         * Returns:           2 if the registerValueLine conforms to the VALUE grammar rule, otherwise it returns 0
        */

        System.out.println("Entering function: VALUE");
        outStream.println("Entering function: VALUE");
        
        // checking if the registerValueLine passed to the DIGITS method is a valid digit token
        if (DIGITS(registerValueLine, outStream)) {
            System.out.println("Leaving function: VALUE Success");
            outStream.println("Leaving function: VALUE Success");
            return 2;
        } else { // if the registerValueLine is not a valid digit token
            System.out.println("Leaving function: VALUE Unsuccessful");
            outStream.println("Leaving function: VALUE Unsuccessful");
            return 0;
        }
    } // end VALUE

    public static boolean DIGITS(String registerValueLine, PrintWriter outStream) {
        /*
         * Method: public static boolean DIGITS(String registerValueLine, PrintWriter outStream)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method evaluates the DIGITS grammar rule: which is a non-terminal symbol of DIGIT, followed by 0 or more DIGIT non-terminals.
         *              This method creates a list of the digits in the registerValueLine and then evaluates each digit using the DIGIT method. If the DIGIT method returns false, the DIGITS method will return false.
         *              The method prints to the terminal and logs to the output file to track the flow of execution.
         * 
         *              DIGITS -> DIGIT {DIGIT} 
         * 
         * Variables: 
         * registerValueLine - the register or value token to be evaluated (not necessarily a digit, but the method will check if it is a digit or not)
         * outStream - for writing to the output file
         * digitCounter - a counter to determine if the first digit has been evaluated or not
         * digitList - a list of the digits in the registerValueLine to be evaluated
         * Arguments:
         * registerValueLine - the register or value token to be evaluated (not necessarily a digit, but the method will check if it is a digit or not)
         * outStream - for writing to the output file
         * Methods Called:    DIGIT() - evaluates the DIGIT grammar rule
         *                    substring() - gets the substring of the registerValueLine
         *                    add() - adds the digit to the digitList
         *                    get() - gets the digit from the digitList
         *                    size() - gets the size of the digitList
         * Returns:           true if the registerValueLine conforms to the DIGITS grammar rule, otherwise it returns false
        */

        System.out.println("Entering function: DIGITS");
        outStream.println("Entering function: DIGITS");

        int digitCounter = 0; // 0 means first digit, 1 or more indicates it is not first digit

        List<String> digitList = new ArrayList<>(); // creating a list of digits to hold each digit in the registerValueLine
        for(int i = 0; i < registerValueLine.length(); i++) {
            digitList.add(registerValueLine.substring(i, i+1)); // add each digit to the list
        }

        if (DIGIT(digitList.get(0), outStream, digitCounter)) { // if the first digit is a digit
            digitCounter++;
            for (int i = 1; i < digitList.size(); i++) {
                if (DIGIT(digitList.get(i), outStream, digitCounter)) { // checking each digit in the list to see if it is a digit
                    digitCounter++;
                } else { // if current digit in the list is not a digit
                    System.out.println("Leaving function: DIGITS Unsuccessful");
                    outStream.println("Leaving function: DIGITS Unsuccessful");
                    return false;
                }
            }
        }
        else { // if the first digit is not a digit
            System.out.println("Leaving function: DIGITS Unsuccessful");
            outStream.println("Leaving function: DIGITS Unsuccessful");
            return false;
        }

        System.out.println("Leaving function: DIGITS Success");
        outStream.println("Leaving function: DIGITS Success");
        return true;
    } // end DIGITS

    public static boolean DIGIT(String registerValueLine,PrintWriter outStream, int digitCounter) {
        /*
         * Method: public static boolean DIGIT(String registerValueLine,PrintWriter outStream, int digitCounter)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method evaluates the DIGIT grammar rule: which is a terminal symbol of '0', '1', '2', '3', '4', '5', '6', '7', '8', or '9'.
         *              0-9 will evaluate as true upon first digit only, for all other digits it will evaluate as true for 0-9 as well as null.
         *              The method prints to the terminal and logs to the output file to track the flow of execution.
         * 
         *              DIGIT -> ‘0’ | .. | ‘9’
         * 
         * Variables: 
         * registerValueLine - the register or value token to be evaluated (not necessarily a digit, but the method will check if it is a digit or not)
         * outStream - for writing to the output file
         * digitCounter - a counter to determine if the first digit has been evaluated or not
         * Arguments:
         * registerValueLine - the register or value token to be evaluated (not necessarily a digit, but the method will check if it is a digit or not)
         * outStream - for writing to the output file
         * digitCounter - a counter to determine if the first digit has been evaluated or not
         * Methods Called:    equals() - compares the registerValueLine to the valid digit tokens
         * Returns:           true if the registerValueLine conforms to the DIGIT grammar rule, otherwise it returns false
        */

        System.out.println("Entering function: DIGIT");
        outStream.println("Entering function: DIGIT");

        if (digitCounter == 0) { // if it is the first digit, we do not allow null or empty string
            if (registerValueLine.equals("0") || registerValueLine.equals("1") || registerValueLine.equals("2") || registerValueLine.equals("3") || registerValueLine.equals("4") || registerValueLine.equals("5") || registerValueLine.equals("6") || registerValueLine.equals("7") || registerValueLine.equals("8") || registerValueLine.equals("9")){
                System.out.println("Leaving function: DIGIT Success");
                outStream.println("Leaving function: DIGIT Success");
                return true;
            }
            else { // not a digit
                System.out.println("Leaving function: DIGIT Unsuccessful");
                outStream.println("Leaving function: DIGIT Unsuccessful");
                return false;
            }
        }
        else { // if it is not the first digit, we allow null or empty string
            if (registerValueLine.equals("0") || registerValueLine.equals("1") || registerValueLine.equals("2") || registerValueLine.equals("3") || registerValueLine.equals("4") || registerValueLine.equals("5") || registerValueLine.equals("6") || registerValueLine.equals("7") || registerValueLine.equals("8") || registerValueLine.equals("9") || registerValueLine.equals(null)){
                System.out.println("Leaving function: DIGIT Success");
                outStream.println("Leaving function: DIGIT Success");
                return true;
            }
            else { // not a digit or empty string
                System.out.println("Leaving function: DIGIT Unsuccessful");
                outStream.println("Leaving function: DIGIT Unsuccessful");
                return false;
            }
        }

    } // end DIGIT

    public static String toLower(String input) {
        /*
         * Method: public static String toLower(String input)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method simply converts the input string to lowercase. This method is used to convert the input tokens to lowercase for case insensitivity as it said in the instructions:
         *              "The language is not case sensitive."
         * 
         * Variables: 
         * input - the input string to be converted to lowercase
         * Arguments:
         * input - the input string to be converted to lowercase
         * Methods Called:    toLowerCase() - converts the input string to lowercase
         * Returns:           the input string converted to lowercase
        */

        // No comment announcing entrance of function as it is helper method, not a grammar rule.
        // Not entirely necessary to have a method for this, could have just used the toLowerCase() method directly in the code, but this method is here for clarity and readability.

        return input.toLowerCase(); // returning the input string to lowercase
    } // end toLower

    public static String getNextLine(Scanner inStream) {
        /*
         * Method: public static String getNextLine(Scanner inStream)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method simply returns the next line in the input file if there is one, otherwise it returns null. This method is used to read the next line in the input file.
         * 
         * Variables:
         * instream - for reading from the input file, will use it's .hasNextLine() and .nextLine() methods to read the next line
         * Arguments:
         * inStream - for reading from the input file, will use it's .hasNextLine() and .nextLine() methods to read the next line
         * Methods Called:    hasNextLine() - checks if there is another line in the input file
         *                    nextLine() - reads the next line in the input file
         * Returns:           the next line in the input file if there is one, otherwise it returns null
        */

        //No comment announcing entrance of function as it is helper method, not a grammar rule.

        if (inStream.hasNextLine()) {
            return inStream.nextLine(); // return the next line if there is one
        } else {
            return null; // return null if there are no more lines to read
        }
    } // end getNextLine
} // end class
