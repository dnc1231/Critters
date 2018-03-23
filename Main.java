package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Daniel Canterino
 * djc3323
 * 15460
 * Spring 2018
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        
        while (true) {
        	System.out.print("critters>" );
        	String line = kb.nextLine();
        	System.out.print("\n");
        	if (line.equals("quit")) {
        		break;
        	}else if (line.equals("show")){
        		Critter.displayWorld();
        	}else {
        		if (!parse(line)) {
        			if(validCommand(line)) {
        				System.out.println("error processing: " + line);
        			}else {
        				System.out.println("invalid command: " + line);
        			}
        		}
        	}
        }
        System.out.flush();
    }
    
	/**
	 * checks if input command is able to be executed from any of the console commands
	 * @param line, the string input by the user
	 */
    private static boolean parse(String line) {
    	if (isStep(line)) {
    		return true;
    	}else if (isSeed(line)) {
    		return true;
    	} else if (isMake(line)) {
			return true;
		}else if (isStats(line)) {
			return true;
		}else {
			return false;
		}
    }
    
	/**
	 * checks if the input command matches that for the step command
	 * @param line
	 * @return true if it ran successfully
	 */
    private static boolean isStep(String line) {
    	String command = "step";
    	if (line.regionMatches(0, command, 0, 4)) {
    		if (line.length() == 4) {
    			Critter.worldTimeStep();
    			return true;
    		}else {
    			if (line.charAt(4) != ' ') {
    				return false; 
    			}else {
    				for (int i = 5; i < line.length(); i++) {
    					if (line.charAt(i) < '0' || line.charAt(i) > '9') {
    						return false;
    					}
    				}
    				String number = line.substring(5, line.length());
    				Integer num = Integer.valueOf(number);
    				for (int i = 0; i < num; i++) {
    					Critter.worldTimeStep();
    				}
    				return true;
    			}
    		}
    	}else {
    		return false;
    	}
    }
    
	/**
	 * checks if the input command matches that for the Seed command
	 * @param line
	 * @return true if it ran successfully
	 */
    private static boolean isSeed(String line) {
    	String command = "seed";
    	if (line.regionMatches(0, command, 0, 4)) {
    		if (line.charAt(4) != ' ') {
    			return false;
    		}else {
				for (int i = 5; i < line.length(); i++) {
					if (line.charAt(i) < '0' || line.charAt(i) > '9') {
						return false;
					}
				}
				String number = line.substring(5, line.length());
				Long num = Long.valueOf(number);
				Critter.setSeed(num);
				return true;
    		}
    	}else {
    		return false;
    	}
    }
    
	/**
	 * checks if the input command matches that for the make command
	 * @param line
	 * @return true if it ran successfully
	 */
    private static boolean isMake(String line){
    	String command = "make";
    	if (line.regionMatches(0, command, 0, 4)) {
    		if (line.charAt(4) != ' ') {
    			return false;
    		}else {
    			int spaceIndex = line.indexOf(' ', 5);
    			if (spaceIndex >= 5) {
    				String critterType = line.substring(5, spaceIndex);
    				for (int i = spaceIndex + 1; i < line.length(); i++) {
    					if (line.charAt(i) < '0' || line.charAt(i) > '9') {
    						return false;
    					}
    				}
    				String number = line.substring(spaceIndex + 1, line.length());
    				Integer num = Integer.valueOf(number);
    				for (int i = 0; i < num; i++) {
    					try {
							Critter.makeCritter(critterType);
						} catch (InvalidCritterException e) {
							return false;
						}
    				}
    				return true;
    			}else {
    				try {
						Critter.makeCritter(line.substring(5, line.length()));
					} catch (InvalidCritterException e) {
						return false;
					}
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
	/**
	 * checks if the input command matches that for the stats command
	 * @param line
	 * @return true if it ran successfully
	 */
    private static boolean isStats(String line) {
    	String command = "stats";
    	if (line.length() <= 5) {
    		return false;
    	}
    	if (line.regionMatches(0, command, 0, 5)) {
    		if (line.charAt(5) != ' ') {
    			return false;
    		}else {
    			String critterType = line.substring(6, line.length());
    			try {
    				 List<Critter> critterList = Critter.getInstances(critterType);
    				 
    				 String className = critterType.substring(0, 1).toUpperCase() + critterType.substring(1);
    				 String classType = myPackage + "." + className;
    				 Class c = Class.forName(classType);
    				 Critter v = (Critter) c.newInstance();
    				 v.runStats(critterList);
    				 
    				 return true;
				} catch (InvalidCritterException e) {
					return false;
				} catch (Exception e) {
					return false;
				}
    		}
    	}else {
    		return false;
    	}
    }
    
	/**
	 * checks if the input command first word matches any of the console commands
	 * @param line
	 * @return true if it found a command it matches
	 */
    private static boolean validCommand (String line) {
    	ArrayList<String> commands = new ArrayList<String>();
    	commands.add("show");
    	commands.add("quit");
    	commands.add("step");
    	commands.add("seed");
    	commands.add("make");
    	commands.add("stats");
    	if (line.indexOf(' ') > 0) {
    		line = line.substring(0, line.indexOf(' '));
    	}
    	if (commands.contains(line)) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
}
