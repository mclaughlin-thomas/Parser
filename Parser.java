import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.List;
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

        //loop will be here

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
            System.exit(0);
            return false;
        }

    }

    public static boolean COMMANDS(Scanner inStream, PrintWriter outStream, int StartCounter) {
        System.out.println("Entering function: COMMANDS");
        outStream.println("Entering function: COMMANDS");

        if (COMMAND(inStream, outStream, StartCounter)) {
            try {
                String nextLine = getNextLine(inStream);
                if (nextLine != null && nextLine.equals("[")) {
                    System.out.println("Bracket Good!");
                } 
                else {
                    System.out.println("bracket bad");
                    System.out.println("Leaving function: COMMANDS Failure");
                    outStream.println("Leaving function: COMMANDS Failure");
                    return false;
                }
            } catch (NullPointerException e) {
                System.out.println("bracket bad");
                System.out.println("Leaving function: COMMANDS Failure");
                outStream.println("Leaving function: COMMANDS Failure");
                return false;
            }

                
            String registerValueLine = toLower(getNextLine(inStream));

            if (REGISTER(outStream, registerValueLine) == 1) {
                System.out.println("Register Good!"); 
                
                
                //if it is a register, we need case for:
                // 1.    comma, register, ]
                // 2.    ], null               DONE

                Boolean isComma = false;
                try {
                    String nextLine = getNextLine(inStream);
                    if (nextLine != null && nextLine.equals(",")) {
                        System.out.println("comma Good!");
                        isComma = true;
                    } 
                    else if(nextLine != null && nextLine.equals("]")){
                        System.out.println("Leaving function: COMMANDS Success");
                        outStream.println("Leaving function: COMMANDS Success");
                        return true;
                    }
                    else {
                        System.out.println("no comma or bracket... nothing there");
                        System.out.println("Leaving function: COMMANDS Failure");
                        outStream.println("Leaving function: COMMANDS Failure");
                        return false;
                    }
                } catch (NullPointerException e) {
                    System.out.println("no comma or bracket... nothing there");
                    System.out.println("Leaving function: COMMANDS Failure");
                    outStream.println("Leaving function: COMMANDS Failure");
                    return false;
                }

                //registere check
                if (REGISTER(outStream, toLower(getNextLine(inStream))) == 1) {
                    //end bracket check
                    System.out.println("Register Good! almost there!");
                    try {
                        String nextLine = getNextLine(inStream);
                        if(nextLine != null && nextLine.equals("]")){
                            System.out.println("Leaving function: COMMANDS Success");
                            outStream.println("Leaving function: COMMANDS Success");
                            return true;
                        }
                        else {
                            System.out.println("no bracket");
                            System.out.println("Leaving function: COMMANDS Failure");
                            outStream.println("Leaving function: COMMANDS Failure");
                            return false;
                        }
                    } catch (NullPointerException e) {
                        System.out.println("no bracket... nothing there");
                        System.out.println("Leaving function: COMMANDS Failure");
                        outStream.println("Leaving function: COMMANDS Failure");
                        return false;
                    }
                }
                else {
                    System.out.println("Leaving function: COMMANDS Failure");
                    outStream.println("Leaving function: COMMANDS Failure");
                    return false;
                }



            }
            else if (VALUE(outStream, registerValueLine) == 2) {
                System.out.println("Value Good!");

                //if it is a value, we need case for:
                // 1. comma, register
                // This is the only case for a value, as a value cannot be followed by another value

                //optional part of the grammar
                Boolean isComma = false;
                try {
                    String nextLine = getNextLine(inStream);
                    if (nextLine != null && nextLine.equals(",")) {
                        System.out.println("comma Good!");
                        isComma = true;
                    } 
                    else {
                        System.out.println("comma bad");
                        isComma = false;
                    }
                } catch (NullPointerException e) {
                    System.out.println("no comma, nothing there");
                    System.out.println("Leaving function: COMMANDS Failure");
                    outStream.println("Leaving function: COMMANDS Failure");
                }

                if (isComma) {
                    System.out.println("Comma detected!");

                    registerValueLine = toLower(getNextLine(inStream));

                    if (REGISTER(outStream, registerValueLine) == 1) {
                        System.out.println("Register Good!");
                        try {
                            String nextLine = getNextLine(inStream);
                            if (nextLine != null && nextLine.equals("]")) {
                                System.out.println("Bracket Good!");
                                System.out.println("Leaving function: COMMANDS Success");
                                outStream.println("Leaving function: COMMANDS Success");
                                return true;
                            } 
                            else {
                                System.out.println("Bracket bad");
                                System.out.println("Leaving function: COMMANDS Failure");
                                outStream.println("Leaving function: COMMANDS Failure");
                                return false;
                            }
                        } catch (NullPointerException e) {
                            System.out.println("no bracket, nothing there");
                            System.out.println("Leaving function: COMMANDS Failure");
                            outStream.println("Leaving function: COMMANDS Failure");
                            return false;
                        }

                    }
                    else {
                        System.out.println("Leaving function: COMMANDS Failure");
                        outStream.println("Leaving function: COMMANDS Failure");
                        return false;
                    }
                }
                if (!isComma) {
                    System.out.println("Comma NOT detected!");
                    System.out.println("Leaving function: COMMANDS Failure");
                    outStream.println("Leaving function: COMMANDS Failure");
                    return false;

                }

            }
            else {
                System.out.println("Leaving function: COMMANDS Failure");
                outStream.println("Leaving function: COMMANDS Failure");
                return false;
            }
            
            

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
            return true;
        } // SHOULD BE IN START SOMEHOW SHOULD BE IN START SOMEHOW SHOULD BE IN START SOMEHOW SHOULD BE IN START SOMEHOW
        if (command == null) {
            System.out.println("Leaving function: COMMAND Failure");
            outStream.println("Leaving function: COMMAND Failure");
            System.exit(0);
            return false;
        }

        command = toLower(command);

        if (command.equals("st") || command.equals("ld") || command.equals("sub") || command.equals("add") || command.equals("sq") || command.equals("rt")) {
            //System.out.println("Command Good!");
            //outStream.println("Command Good!");
            System.out.println("Leaving function: COMMAND Success");
            outStream.println("Leaving function: COMMAND Success");
            return true;
        } else {
            System.out.println("Leaving function: COMMAND Failure");
            outStream.println("Leaving function: COMMAND Failure");
            System.exit(0);
            return false;
        }
    }

    public static int REGISTER(PrintWriter outStream, String registerValueLine) {
        System.out.println("Entering function: REGISTER");
        outStream.println("Entering function: REGISTER");

        if (registerValueLine.equals("a") || registerValueLine.equals("b") || registerValueLine.equals("c") || registerValueLine.equals("d") || registerValueLine.equals("e") || registerValueLine.equals("f")) {
            //System.out.println("Command Good!");
            //outStream.println("Command Good!");
            System.out.println("Leaving function: REGISTER Success");
            outStream.println("Leaving function: REGISTER Success");
            return 1;
        }
        else {
            System.out.println("Leaving function: REGISTER Failure");
            outStream.println("Leaving function: REGISTER Failure");
            return 0;
        }
    }

    public static int VALUE(PrintWriter outStream, String registerValueLine){
        System.out.println("Entering function: VALUE");
        outStream.println("Entering function: VALUE");
        
        if (DIGITS(registerValueLine, outStream)) {
            System.out.println("Leaving function: VALUE Success");
            outStream.println("Leaving function: VALUE Success");
            return 2;
        } else {
            System.out.println("Leaving function: VALUE Failure");
            outStream.println("Leaving function: VALUE Failure");
            return 0;
        }
    }

    public static boolean DIGITS(String registerValueLine, PrintWriter outStream) {
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
                    System.out.println("Leaving function: DIGITS Failure");
                    outStream.println("Leaving function: DIGITS Failure");
                    return false;
                }
            }
            
        }

        else {
            System.out.println("Leaving function: DIGITS Failure");
            outStream.println("Leaving function: DIGITS Failure");
            return false;
        }

        return true;
    }

    public static boolean DIGIT(String registerValueLine,PrintWriter outStream, int digitCounter) {
        System.out.println("Entering function: DIGIT");
        outStream.println("Entering function: DIGIT");

        System.out.println("Register Value Line: " + registerValueLine);

        if (digitCounter == 0) {
            if (registerValueLine.equals("0") || registerValueLine.equals("1") || registerValueLine.equals("2") || registerValueLine.equals("3") || registerValueLine.equals("4") || registerValueLine.equals("5") || registerValueLine.equals("6") || registerValueLine.equals("7") || registerValueLine.equals("8") || registerValueLine.equals("9")){
                System.out.println("Leaving function: DIGIT Success");
                outStream.println("Leaving function: DIGIT Success");
                return true;
            }
            else {
                System.out.println("Leaving function: DIGIT Failure");
                outStream.println("Leaving function: DIGIT Failure");
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
                System.out.println("Leaving function: DIGIT Failure");
                outStream.println("Leaving function: DIGIT Failure");
                return false;
            }
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
