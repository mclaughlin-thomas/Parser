/*
 * Java Programming Assignment #2  Spring 2024 – Parser (Strictly Provided Grammar Version)
 * Description: This program reads a file, parses the tokens based on grammar rules, and notifies the user if the program is legal or not.
 * CS-310 Programming Languages
 * Thomas McLaughlin
 * 04/30/2024
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
         *              This method reads from a file named "prog4TMTokens.txt" containing tokens and the program multiple functions that act as the grammar rules to validate and parse
         *              the tokens. At the end, it outputs whether the tokens in the file are legal or not based on the grammar provided below. The method prints to the terminal and logs
         *              to the output file throughout the process for tracking the flow of execution.
         * 
         *              Grammar rules:
         *              START -> { COMMANDS }
         *              COMMANDS -> COMMAND ‘[‘ (REGISTER | VALUE )  [ ‘,’ REGISTER] ‘]’
         *              COMMAND -> ‘st’ | ‘ld’ | ‘sub’ | ‘add’ | ‘sq’  | ‘rt’ 
         *              REGISTER -> ‘a’ | ‘b’ | ‘c’ | ‘d’ | ‘e’ | ‘f’
         *              VALUE -> DIGITS
         *              DIGIT -> ‘0’ | .. | ‘9’
         *              DIGITS -> DIGIT {DIGIT}
         * 
         * 
         * Variables: 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * tokenPath - the path to the input file
         * outputPath - the path to the output file
         * parseIndication - a boolean to indicate if the program derived from the tokens is legal or not
         * 
        */
        
        Scanner inStream = null; // initialize read
        PrintWriter outStream=null; // initialize write
        String tokenPath = "prog4TMTokens.txt"; // path to input file
        String outputPath = "Prog4TMout.txt"; // path to output file

        //try/catch for accessing input file to account for errors
        try
        {
            System.out.println("Checking the file " + tokenPath);
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

        if(parseIndication){
            System.out.println("The program in the input file successfully parsed and is a legal program!");
        }
        else{
            System.out.println("The program in the input file did not successfully parse and is not a legal program!");
        }

        inStream.close(); // close the input file
        outStream.close(); // close the output file
    }

    public static boolean START(Scanner inStream,PrintWriter outStream) {
        /*
         * Method: public static boolean START(Scanner inStream,PrintWriter outStream)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method reads a file and tokenizes the input. It then writes the tokens to an output file.
         *              It uses the lexicalAnalyzer method to tokenize the input.
         * Variables: 
         * in - a scanner object to read from the input file 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * line - the current line being read from the input file
         * tokens - a LinkedList of strings to store tokens from lexicalAnalyzer
        */

        System.out.println("Entering function: START");
        outStream.println("Entering function: START");

        while (inStream.hasNextLine()) {

            if (!COMMANDS(inStream, outStream)) {
                System.out.println("Leaving function: START Unsuccessful");
                outStream.println("Leaving function: START Unsuccessful");
                return false;
            }
            
        }

        System.out.println("Leaving function: START Success");
        outStream.println("Leaving function: START Success");
        return true;
    }

    public static boolean COMMANDS(Scanner inStream, PrintWriter outStream) {
        /*
         * Method:public static boolean COMMANDS(Scanner inStream, PrintWriter outStream)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method reads a file and tokenizes the input. It then writes the tokens to an output file.
         *              It uses the lexicalAnalyzer method to tokenize the input.
         * Variables: 
         * in - a scanner object to read from the input file 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * line - the current line being read from the input file
         * tokens - a LinkedList of strings to store tokens from lexicalAnalyzer
        */

        System.out.println("Entering function: COMMANDS");
        outStream.println("Entering function: COMMANDS");

        String command = toLower(getNextLine(inStream));

        if (COMMAND(inStream, outStream, command)) {
            try {
                String nextLine = getNextLine(inStream);
                if (!nextLine.equals("[")) {
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                } 
            } catch (NullPointerException e) {
                System.out.println("Leaving function: COMMANDS Unsuccessful");
                outStream.println("Leaving function: COMMANDS Unsuccessful");
                return false;
            }
                
            String registerValueLine = toLower(getNextLine(inStream));

            if (REGISTER(outStream, registerValueLine) == 1) {
                
                //optional part of the grammar
                try {
                    String nextLine = getNextLine(inStream);
                    if(nextLine.equals("]")){
                        System.out.println("Leaving function: COMMANDS Success");
                        outStream.println("Leaving function: COMMANDS Success");
                        return true;
                    }
                    else if( !nextLine.equals(",")){
                        System.out.println("Leaving function: COMMANDS Unsuccessful");
                        outStream.println("Leaving function: COMMANDS Unsuccessful");
                        return false;
                    }
                } catch (NullPointerException e) {
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                }

                if( REGISTER(outStream, toLower(getNextLine(inStream))) == 1) {

                    try {
                        String nextLine = getNextLine(inStream);
                        if (nextLine != null && nextLine.equals("]")) {
                            System.out.println("Leaving function: COMMANDS Success");
                            outStream.println("Leaving function: COMMANDS Success");
                            return true;
                        } 
                        else {
                            System.out.println("Leaving function: COMMANDS Unsuccessful");
                            outStream.println("Leaving function: COMMANDS Unsuccessful");
                            return false;
                        }
                    } catch (NullPointerException e) {
                        System.out.println("Leaving function: COMMANDS Unsuccessful");
                        outStream.println("Leaving function: COMMANDS Unsuccessful");
                        return false;
                    }
                }
                else {
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                }
            }
            else if (VALUE(outStream, registerValueLine) == 2) {
                //optional part of the grammar
                try {
                    String nextLine = getNextLine(inStream);
                    if(nextLine.equals("]")){
                        System.out.println("Leaving function: COMMANDS Success");
                        outStream.println("Leaving function: COMMANDS Success");
                        return true;
                    }
                    else if( !nextLine.equals(",")){
                        System.out.println("Leaving function: COMMANDS Unsuccessful");
                        outStream.println("Leaving function: COMMANDS Unsuccessful");
                        return false;
                    }
                } catch (NullPointerException e) {
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                }

                if( REGISTER(outStream, toLower(getNextLine(inStream))) == 1) {

                    try {
                        String nextLine = getNextLine(inStream);
                        if (nextLine != null && nextLine.equals("]")) {
                            System.out.println("Leaving function: COMMANDS Success");
                            outStream.println("Leaving function: COMMANDS Success");
                            return true;
                        } 
                        else {
                            System.out.println("Leaving function: COMMANDS Unsuccessful");
                            outStream.println("Leaving function: COMMANDS Unsuccessful");
                            return false;
                        }
                    } catch (NullPointerException e) {
                        System.out.println("Leaving function: COMMANDS Unsuccessful");
                        outStream.println("Leaving function: COMMANDS Unsuccessful");
                        return false;
                    }
                }
                else {
                    System.out.println("Leaving function: COMMANDS Unsuccessful");
                    outStream.println("Leaving function: COMMANDS Unsuccessful");
                    return false;
                }
            }
            else {
                System.out.println("Leaving function: COMMANDS Unsuccessful");
                outStream.println("Leaving function: COMMANDS Unsuccessful");
                return false;
            }
        }

        System.out.println("Leaving function: COMMANDS Unsuccessful");
        outStream.println("Leaving function: COMMANDS Unsuccessful");
        return false;
    }

    public static boolean COMMAND(Scanner inStream, PrintWriter outStream, String command) {
        /*
         * Method: public static boolean COMMAND(Scanner inStream, PrintWriter outStream, String command)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method reads a file and tokenizes the input. It then writes the tokens to an output file.
         *              It uses the lexicalAnalyzer method to tokenize the input.
         * Variables: 
         * in - a scanner object to read from the input file 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * line - the current line being read from the input file
         * tokens - a LinkedList of strings to store tokens from lexicalAnalyzer
        */

        System.out.println("Entering function: COMMAND");
        outStream.println("Entering function: COMMAND");

        if (command.equals("st") || command.equals("ld") || command.equals("sub") || command.equals("add") || command.equals("sq") || command.equals("rt")) {
            System.out.println("Leaving function: COMMAND Success");
            outStream.println("Leaving function: COMMAND Success");
            return true;
        } else {
            System.out.println("Leaving function: COMMAND Unsuccessful");
            outStream.println("Leaving function: COMMAND Unsuccessful");
            return false;
        }
    }

    public static int REGISTER(PrintWriter outStream, String registerValueLine) {
        /*
         * Method: public static int REGISTER(PrintWriter outStream, String registerValueLine)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method reads a file and tokenizes the input. It then writes the tokens to an output file.
         *              It uses the lexicalAnalyzer method to tokenize the input.
         * Variables: 
         * in - a scanner object to read from the input file 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * line - the current line being read from the input file
         * tokens - a LinkedList of strings to store tokens from lexicalAnalyzer
        */

        System.out.println("Entering function: REGISTER");
        outStream.println("Entering function: REGISTER");

        if (registerValueLine.equals("a") || registerValueLine.equals("b") || registerValueLine.equals("c") || registerValueLine.equals("d") || registerValueLine.equals("e") || registerValueLine.equals("f")) {
            System.out.println("Leaving function: REGISTER Success");
            outStream.println("Leaving function: REGISTER Success");
            return 1;
        }
        else {
            System.out.println("Leaving function: REGISTER Unsuccessful");
            outStream.println("Leaving function: REGISTER Unsuccessful");
            return 0;
        }
    }

    public static int VALUE(PrintWriter outStream, String registerValueLine){
        /*
         * Method: public static int VALUE(PrintWriter outStream, String registerValueLine)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method reads a file and tokenizes the input. It then writes the tokens to an output file.
         *              It uses the lexicalAnalyzer method to tokenize the input.
         * Variables: 
         * in - a scanner object to read from the input file 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * line - the current line being read from the input file
         * tokens - a LinkedList of strings to store tokens from lexicalAnalyzer
        */

        System.out.println("Entering function: VALUE");
        outStream.println("Entering function: VALUE");
        
        if (DIGITS(registerValueLine, outStream)) {
            System.out.println("Leaving function: VALUE Success");
            outStream.println("Leaving function: VALUE Success");
            return 2;
        } else {
            System.out.println("Leaving function: VALUE Unsuccessful");
            outStream.println("Leaving function: VALUE Unsuccessful");
            return 0;
        }
    }

    public static boolean DIGITS(String registerValueLine, PrintWriter outStream) {
        /*
         * Method: public static boolean DIGITS(String registerValueLine, PrintWriter outStream)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method reads a file and tokenizes the input. It then writes the tokens to an output file.
         *              It uses the lexicalAnalyzer method to tokenize the input.
         * Variables: 
         * in - a scanner object to read from the input file 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * line - the current line being read from the input file
         * tokens - a LinkedList of strings to store tokens from lexicalAnalyzer
        */

        System.out.println("Entering function: DIGITS");
        outStream.println("Entering function: DIGITS");

        int digitCounter = 0; // 0 means first digit, 1 means not first digit

        List<String> digitList = new ArrayList<>();
        for(int i = 0; i < registerValueLine.length(); i++) {
            digitList.add(registerValueLine.substring(i, i+1));
        }

        if (DIGIT(digitList.get(0), outStream, digitCounter)) {
            digitCounter++;
            for (int i = 1; i < digitList.size(); i++) {
                if (DIGIT(digitList.get(i), outStream, digitCounter)) {
                    digitCounter++;
                } else {
                    System.out.println("Leaving function: DIGITS Unsuccessful");
                    outStream.println("Leaving function: DIGITS Unsuccessful");
                    return false;
                }
            }
        }
        else {
            System.out.println("Leaving function: DIGITS Unsuccessful");
            outStream.println("Leaving function: DIGITS Unsuccessful");
            return false;
        }

        System.out.println("Leaving function: DIGITS Success");
        outStream.println("Leaving function: DIGITS Success");
        return true;
    }

    public static boolean DIGIT(String registerValueLine,PrintWriter outStream, int digitCounter) {
        /*
         * Method: public static boolean DIGIT(String registerValueLine,PrintWriter outStream, int digitCounter)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method reads a file and tokenizes the input. It then writes the tokens to an output file.
         *              It uses the lexicalAnalyzer method to tokenize the input.
         * Variables: 
         * in - a scanner object to read from the input file 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * line - the current line being read from the input file
         * tokens - a LinkedList of strings to store tokens from lexicalAnalyzer
        */

        System.out.println("Entering function: DIGIT");
        outStream.println("Entering function: DIGIT");

        if (digitCounter == 0) {
            if (registerValueLine.equals("0") || registerValueLine.equals("1") || registerValueLine.equals("2") || registerValueLine.equals("3") || registerValueLine.equals("4") || registerValueLine.equals("5") || registerValueLine.equals("6") || registerValueLine.equals("7") || registerValueLine.equals("8") || registerValueLine.equals("9")){
                System.out.println("Leaving function: DIGIT Success");
                outStream.println("Leaving function: DIGIT Success");
                return true;
            }
            else {
                System.out.println("Leaving function: DIGIT Unsuccessful");
                outStream.println("Leaving function: DIGIT Unsuccessful");
                return false;
            }
        }
        else {
            if (registerValueLine.equals("0") || registerValueLine.equals("1") || registerValueLine.equals("2") || registerValueLine.equals("3") || registerValueLine.equals("4") || registerValueLine.equals("5") || registerValueLine.equals("6") || registerValueLine.equals("7") || registerValueLine.equals("8") || registerValueLine.equals("9") || registerValueLine.equals(null)){
                System.out.println("Leaving function: DIGIT Success");
                outStream.println("Leaving function: DIGIT Success");
                return true;
            }
            else {
                System.out.println("Leaving function: DIGIT Unsuccessful");
                outStream.println("Leaving function: DIGIT Unsuccessful");
                return false;
            }
        }

    }

    public static String toLower(String input) {
        /*
         * Method: public static String toLower(String input)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method reads a file and tokenizes the input. It then writes the tokens to an output file.
         *              It uses the lexicalAnalyzer method to tokenize the input.
         * Variables: 
         * in - a scanner object to read from the input file 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * line - the current line being read from the input file
         * tokens - a LinkedList of strings to store tokens from lexicalAnalyzer
        */

        //"The language is not case sensitive."
        return input.toLowerCase();
    }

    public static String getNextLine(Scanner inStream) {
        /*
         * Method: public static String getNextLine(Scanner inStream)
         * Programmer: Thomas McLaughlin
         * Date Created: 4/26/2024
         * Last Modified: 4/30/2024
         * Description: This method reads a file and tokenizes the input. It then writes the tokens to an output file.
         *              It uses the lexicalAnalyzer method to tokenize the input.
         * Variables: 
         * in - a scanner object to read from the input file 
         * inStream - for reading from the input file
         * outStream - for writing to the output file
         * line - the current line being read from the input file
         * tokens - a LinkedList of strings to store tokens from lexicalAnalyzer
        */

        if (inStream.hasNextLine()) {
            return inStream.nextLine();
        } else {
            return null;
        }
    }
}