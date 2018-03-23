package assignment4;

/* CRITTERS Critter2.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Daniel Canterino
 * djc3323
 * 15460
 * Spring 2018
 */

/**
 * 
 * @author Daniel Canterino
 * @version 1.0
 * This class extends the Critter class. This Critter runs in the same circle forever until death either clockwise or counterclockwise.
 */
public class Critter2 extends Critter{
	@Override
	public String toString() {return "2";}
	private int previousDir;
	int startingDir;
	int numCirclesMade;
	int rotationDirection;
	
	//always fights if given the chance
	public boolean fight(String not_used) {
		return true;
	}
	
	/**
	 * Critter2 constructor
	 */
	public Critter2() {
		previousDir = getRandomInt(8);
		startingDir = previousDir;
		numCirclesMade = 0;
		rotationDirection = getRandomInt(2);
		if (rotationDirection == 0) {
			rotationDirection = -1;
		}
	}
	
	/**
	 * Runs the critter in its continued circle and if it reaches back to its starting position, it spawns a child given enough energy
	 */
	public void doTimeStep() {
		previousDir = (previousDir + rotationDirection) % 8;
		if (rotationDirection == -1 && previousDir < 0) {
			previousDir = 8;
		}
		run(previousDir);
		if (previousDir == startingDir) {
			if (getEnergy() > 150) {
				Critter2 child = new Critter2();
				reproduce(child, 2);
			}
			numCirclesMade += 1;
		}
	}
	
	/**
	 * Prints how many circles have been made by the living critter2s and how many are running in each rotational direction
	 */
	public static void runStats(java.util.List<Critter> critter2s) {
		int numClockwise = 0;
		int numCounterClockwise = 0;
		int totalCircles = 0;
		for (Critter c : critter2s) {
			Critter2 critter = (Critter2) c;
			if (critter.rotationDirection == 1) {
				numCounterClockwise++;
			}else {
				numClockwise++;
			}
			totalCircles += critter.numCirclesMade;
		}
		System.out.println("There are " + numCounterClockwise + " Critter2s running counter-clockwise and " 
				+ numClockwise + " Critters2s running clockwise");
		System.out.println("The living currently living Critter2s have ran in " + totalCircles + " total circles");
	}
	
}
