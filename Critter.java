package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Daniel Canterino
 * djc3323
 * 15460
 * Spring 2018
 */


import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	
	private static int timestep = 0;
	private static List<Critter> alreadyMoved = new java.util.ArrayList<Critter>();
	
	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	protected final void walk(int direction) {
		move(direction, 1);
		energy = energy - Params.walk_energy_cost;
		
	}
	
	protected final void run(int direction) {
		move(direction, 2);
		energy = energy - Params.run_energy_cost;
	}
	
	private void move(int dir, int offset) {
		switch (dir) {
		case 1:
			x_coord = getNewXLocation(x_coord - (1 * offset));
			y_coord = getNewYLocation(y_coord - (1 * offset));
			break;
		case 2:
			x_coord = getNewXLocation(x_coord - (1 * offset));
			break;
		case 3:
			x_coord = getNewXLocation(x_coord - (1 * offset));
			y_coord = getNewYLocation(y_coord + (1 * offset));
			break;
		case 4:
			y_coord = getNewYLocation(y_coord - (1 * offset));
			break;
		case 5:
			y_coord = getNewYLocation(y_coord + (1 * offset));
			break;
		case 6:
			x_coord = getNewXLocation(x_coord + (1 * offset));
			y_coord = getNewYLocation(y_coord - (1 * offset));
			break;
		case 7:
			x_coord = getNewXLocation(x_coord + (1 * offset));
			break;
		case 8:
			x_coord = getNewXLocation(x_coord + (1 * offset));
			y_coord = getNewYLocation(y_coord + (1 * offset));
			break;
		}
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		if (energy < Params.min_reproduce_energy) {
			return;
		}else {
			if (energy % 2 == 0) {
				offspring.energy = energy/2;
				energy = energy/2;
			}else {
				offspring.energy = energy/2;
				energy = energy/2 + 1;
			}
			offspring.x_coord = x_coord;
			offspring.y_coord = y_coord;
			offspring.walk(direction);
			babies.add(offspring);
			return;
		}
		
		
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			Class <?> c = Class.forName(myPackage + "." + critter_class_name);
			@SuppressWarnings("deprecation")
			Critter v = (Critter) c.newInstance();
			
			
		}catch (ClassNotFoundException e){
			throw new InvalidCritterException(critter_class_name);
		}catch (IllegalAccessException e) {
			throw new InvalidCritterException(critter_class_name);
		}catch (InstantiationException e) {
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try{
			Class.forName(critter_class_name);
			for (Critter c : population) {
				if (c.getClass().getName().equalsIgnoreCase(critter_class_name)) {
					result.add(c);
				}
			}
		}catch (ClassNotFoundException e){
			throw new InvalidCritterException(critter_class_name);
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
		// Complete this method.
	}
	
	public static void worldTimeStep() {
		// Complete this method.
		// 1. increment timestep; timestep++;
		timestep++;
		// 2. doTimeSteps();
		for (Critter c : population) {
			c.doTimeStep();
		}
		// 3. Do the fights. doEncounters();
		for (Critter A : population) {
			for (Critter B : population) {
				if ((A != B) && (A.x_coord == B.x_coord) && (A.y_coord == B.y_coord) && (A.getEnergy() >= 0) && (B.getEnergy() >= 0)){
					fight(A, B);
				}
			}
		}
		alreadyMoved.clear();//resets the list tracking who moved during an encounter that turn
		// 4. updateRestEnergy();
		for (Critter c : population) {
			c.energy = c.energy - Params.rest_energy_cost;
		}
		// 5. Generate Algae 
		for (int i = 0; i < Params.refresh_algae_count; i++) {
			Algae newAlgae = new Algae();
			newAlgae.setX_coord(getRandomInt(Params.world_width));
			newAlgae.setY_coord(getRandomInt(Params.world_height));
			newAlgae.setEnergy(Params.start_energy);
		}
		// 6. Move babies to general population. 
		population.addAll(babies);
		babies.clear();
		// 7. Cull dead critters from population.
		for (Critter c : population) {
			if (c.getEnergy() <= 0) {
				population.remove(c);
			}
		}
	}
	
	public static void displayWorld() {
		for (int i = -1; i < Params.world_width + 1; i++) {
			if (i == -1 || i == Params.world_width) {
				System.out.print('+');
			}else {
				System.out.print('-');
			}
		}
		System.out.print("\n");
		for (int i = 0; i < Params.world_height; i++) {
			for (int j = -1; j < Params.world_width + 1; j++) {
				if (j == -1 || j == Params.world_width) {
					System.out.print('|');
				}else if(isLocationOccupied(j, i)){
					for (Critter c : population) {
						if (c.x_coord == j && c.y_coord == i) {
							System.out.print(c.toString());
						}
					}
				}else {
					System.out.print(" ");
				}
			}
			System.out.print("\n");
		}
		System.out.print("\n");
		for (int i = -1; i < Params.world_width + 1; i++) {
			if (i == -1 || i == Params.world_width) {
				System.out.print('+');
			}else {
				System.out.print('-');
			}
		}
		
	}
	
	private static void fight(Critter A, Critter B) {
		int rollA = 0;
		int rollB = 0;
		if ( A.fight(B.toString()) ) {
			rollA = getRandomInt(A.energy);
		}else {
			if(checkForFlee(A)) {
				if (!B.fight(A.toString())) {//will allow B to flee as well before returning if both B and A select to flee
					if (checkForFlee(B)) {
						return;
					}
				}
				return;
			}
		}
		if (B.fight(A.toString())) {
			rollB = getRandomInt(B.energy);
		}else {
			if (checkForFlee(B)) {
				return;
			}
		}
		if (rollA > rollB) {
			A.energy += B.getEnergy()/2;
			B.energy = 0;
		}else {//B is default winner in case of tie as well if B roll is greater
			B.energy += A.getEnergy()/2;
			A.energy = 0;
		}
	}
	
	private static boolean checkForFlee(Critter A) {
		int dir = 1;
		if (alreadyMoved.contains(A)) {
			A.energy = A.energy - Params.run_energy_cost;
			return false;
		}
		for (int x = -2; x < 3; x += 2) {
			for (int y = -2; y < 3; y += 2) {
				if (x == 0 && y == 0) {
				}else{
					int newX = getNewXLocation(A.x_coord + x);
					int newY = getNewYLocation(A.y_coord + y);
					if (!isLocationOccupied(newX, newY)) {//if the location is not occupied AND critter has not already moved this turn, run to it
						A.run(dir);
						alreadyMoved.add(A);
						A.energy = A.energy - Params.run_energy_cost;
						return true;
						}
					dir++;
				}
			}
		}
		return false;
	}
	
	private static boolean isLocationOccupied(int x, int y) {
		for (Critter c : population) {
			if (c.x_coord == x && c.y_coord == y) {
				return true;
			}
		}
		return false;
	}
	
	private static int getNewXLocation(int x) {
		int newX = x;
		if (x < 0) {
			newX = Params.world_width + x;
		}else if (x > Params.world_width) {
			newX = x - Params.world_width;
		}
		return newX;
	}
	
	private static int getNewYLocation(int y) {
		int newY = y;
		if (y < 0) {
			newY = Params.world_height + y;
		}else if (y > Params.world_height) {
			newY = y - Params.world_height;
		}
		return newY;
	}
}
