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
            System.out.println("Error opening the file prog4TMTokens.txt.");
            System.exit(0);
        }

        //try/catch for accessing output file to account for errors
        try
        {
            outStream = new PrintWriter("Prog4TMout.txt");
        } // end try
        catch(FileNotFoundException e)
        {
            System.out.println("error opening the file Prog4TMout.txt.");
            System.exit(0);
        } // end catch

        inStream.close();
        outStream.close();
    }

    public static boolean START() {
        System.out.println("Entering function: START");
        
        return true; 
    }

    public static String toLower(String input) {
        //Per instructions: "The language is not case sensitive." All input is converted to lower case to conform to logic of parser.
        
        return input.toLowerCase();
    }
}
