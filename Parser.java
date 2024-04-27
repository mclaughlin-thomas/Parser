import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.PrintWriter; // Import the PrintWriter class to write text files

public class Parser {
    public static void main(String[] args) {
        
        Scanner inStream = null; // initialize read
        PrintWriter outStream=null; // initialize write

        //try/catch for accessing input file to account for errors
        try
        {
            inStream = new Scanner(new File("prog4TMTokens.txt"));
        } // end try
        catch(FileNotFoundException e)
        {
            System.out.println("Error opening the file prog4TMTokens.txt");
            System.exit(0);
        }

        //try/catch for accessing output file to account for errors
        try
        {
            outStream = new PrintWriter("Prog4TMout.txt");
        } // end try
        catch(FileNotFoundException e)
        {
            System.out.println("error opening the file Prog4TMout.txt");
            System.exit(0);
        } // end catch
        
        //call the Start grammar rule

        START(inStream, outStream);

        inStream.close();
        outStream.close();
    }

    // START -> { COMMANDS }
    // COMMANDS -> COMMAND ‘[‘ (REGISTER | VALUE )  [ ‘,’ REGISTER] ‘]’
    // COMMAND -> ‘st’ | ‘ld’ | ‘sub’ | ‘add’ | ‘sq’  | ‘rt’ 
    // REGISTER -> ‘a’ | ‘b’ | ‘c’ | ‘d’ | ‘e’ | ‘f’
    // VALUE -> DIGITS
    // DIGIT -> ‘0’ | .. | ‘9’
    // DIGITS -> DIGIT {DIGIT}


    public static boolean START(Scanner inStream,PrintWriter outStream) {
        System.out.println("Entering function: START");
        outStream.println("Entering function: START");
        int StartCounter = 0;

        if (COMMANDS(inStream, outStream, StartCounter)) {
            System.out.println("Leaving function: START Success");
            outStream.println("Leaving function: START Success");
            return true;
        } else {
            System.out.println("Leaving function: START Failure");
            outStream.println("Leaving function: START Failure");
            return false;
        }

    }

    public static boolean COMMANDS(Scanner inStream, PrintWriter outStream, int StartCounter) {
        System.out.println("Entering function: COMMANDS");
        outStream.println("Entering function: COMMANDS");

        if (COMMAND(inStream, outStream, StartCounter)) {
            System.out.println("Command Good!");

        } else {
            System.out.println("Leaving function: COMMANDS Failure");
            outStream.println("Leaving function: COMMANDS Failure");
            return false;
        }

        return true;
    }

    public static boolean COMMAND(Scanner inStream, PrintWriter outStream, int StartCounter) {
        System.out.println("Entering function: COMMAND");
        outStream.println("Entering function: COMMAND");

        String command = getNextLine(inStream);

        if(command == null && StartCounter == 0){
            //for the first command, if there is no command, return true
            System.out.println("Leaving function: START Success");
            outStream.println("Leaving function: START Success");
        } // SHOULD BE IN START SOMEHOW SHOULD BE IN START SOMEHOW SHOULD BE IN START SOMEHOW SHOULD BE IN START SOMEHOW
        if (command == null) {
            System.out.println("Leaving function: COMMAND Failure");
            outStream.println("Leaving function: COMMAND Failure");
            return false;
        }

        command = toLower(command);

        if (command.equals("st") || command.equals("ld") || command.equals("sub") || command.equals("add") || command.equals("sq") || command.equals("rt")) {
            System.out.println("Command Good!");
            outStream.println("Command Good!");
            return true;
        } else {
            System.out.println("Leaving function: COMMAND Failure");
            outStream.println("Leaving function: COMMAND Failure");
            return false;
        }
    }

    public static String toLower(String input) {
        //Per instructions: "The language is not case sensitive." All input is converted to lower case to conform to logic of parser.
        return input.toLowerCase();
    }

    public static String getNextLine(Scanner inStream) {
        if (inStream.hasNextLine()) {
            return inStream.nextLine();
        } else {
            return null;
        }
    }
}
