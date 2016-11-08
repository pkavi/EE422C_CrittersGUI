/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Pranav Kavikondala
 * pk6994
 * 16470
 * Slip days used: 0
 * Fall 2016
 */
package assignment5;
import java.io.PrintStream;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


/**
 * @author Pranav Kavikondala
 * 
 * Critter abstract base class from which every Critter shall extend
 * This class takes care of simulating all critters. Any new critter made should be added to the population field of this class
 * In addition, any Critter extending this class must implement a fight and doTimeStep method
 */
public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	//Coordinates that are occupied
	private static Critter[][] grid=new Critter[Params.world_height][Params.world_width];
	
	//boolean to keep track of if critter walk or ran during this time step
	protected boolean walkRun=false;
	//boolean to keep track whether walkRun is being called in doTimeStep or in fight
	protected boolean inTimeStep=false; //Set to false when doing a time step but set to true otherwise
	protected int oldX=0;
	protected int oldY=0;
	

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
	
	/**
	 * If called in doTimeStep() of Critter: makes Critter move 1 space in specified direction(always successful)
	 * If called in fight() of Critter: attempts to move Critter 1 space in specified direction(only successful if space open)
	 * @param direction the direction in which the critter should move
	 */
	protected final void walk(int direction) {
		//Only walk if the not walked this time step
		if(walkRun==false && inTimeStep){
			runWalkExecute(1,direction);
			walkRun=true;
		}
		//Walk during fight step if not walked yet
		else if(walkRun==false){
			if(validMove(1,direction)){
				runWalkExecute(1,direction);
			}
			walkRun=true;
		}
		energy-=Params.walk_energy_cost;
	}
	protected String look(int direction, boolean steps){
		int x_coord=this.x_coord;
		int y_coord=this.y_coord;
		int stepsActual;
		if(steps){
			stepsActual=2;
		}else{
			stepsActual=1;
		}
		if(direction==0 || direction==1 || direction==7){
			x_coord+=stepsActual;
		}
		if(direction==3 || direction==4 || direction==5){
			x_coord-=stepsActual;
		}
		if(direction==1 || direction==2 || direction==3){
			y_coord-=stepsActual;
		}
		if(direction==5 || direction==6 || direction==7){
			y_coord+=stepsActual;
		}
		if(x_coord<0){
			x_coord+=Params.world_width;
		}
		if(y_coord<0){
			y_coord+=Params.world_height;
		}
		x_coord=x_coord%Params.world_width;
		y_coord=y_coord%Params.world_height;
		
		energy-=Params.look_energy_cost;
		
		
		for(Critter c:population){
			if(inTimeStep){
				if(c.energy>0 && c.oldX==this.oldX && c.oldY==this.oldY){
					return c.toString();
				}
			}else{
				if(c.energy>0 && c.x_coord==this.x_coord && c.y_coord==this.y_coord){
					return c.toString();
				}
			}
		}
		return null;
		
	}
	/**
	 * Checks if the Critter can move into the spot specified by steps and direction in relation to Critter's current position
	 * @param steps Number of steps to take in direction
	 * @param direction Direction of movement
	 * @return True if the Critter can move into that spot(i.e. the spot is not occupied)
	 */
	protected boolean validMove(int steps,int direction){
		int xNew=x_coord;
		int yNew=y_coord;
		if(direction==0 || direction==1 || direction==7){
			xNew+=steps;
		}
		if(direction==3 || direction==4 || direction==5){
			xNew-=steps;
		}
		if(direction==1 || direction==2 || direction==3){
			yNew-=steps;
		}
		if(direction==5 || direction==6 || direction==7){
			yNew+=steps;
		}
		if(xNew<0){
			xNew+=Params.world_width;
		}
		if(yNew<0){
			yNew+=Params.world_height;
		}
		xNew=xNew%Params.world_width;
		yNew=yNew%Params.world_height;
		//Check if position is occupied if not then return true
		if(!occupied(xNew,yNew)){
			return true;
		}
		return false;
	}
	
	/**
	 * Moves critter in a direction with specified number of steps
	 * @param steps The number of steps to move the Critter.
	 * @param direction The direction to move the Critter.
	 */
	protected final void runWalkExecute(int steps, int direction){
		if(direction==0 || direction==1 || direction==7){
			x_coord+=steps;
		}
		if(direction==3 || direction==4 || direction==5){
			x_coord-=steps;
		}
		if(direction==1 || direction==2 || direction==3){
			y_coord-=steps;
		}
		if(direction==5 || direction==6 || direction==7){
			y_coord+=steps;
		}
		if(x_coord<0){
			x_coord+=Params.world_width;
		}
		if(y_coord<0){
			y_coord+=Params.world_height;
		}
		x_coord=x_coord%Params.world_width;
		y_coord=y_coord%Params.world_height;
	}
	
	/**
	 * If called in doTimeStep() of Critter: makes Critter move 2 spaces in specified direction(always successful)
	 * If called in fight() of Critter: attempts to move Critter 2 spaces in specified direction(only successful if space open)
	 * @param direction the direction in which the critter should move
	 */
	protected final void run(int direction) {
		if(walkRun==false && inTimeStep){
		runWalkExecute(2,direction);
		walkRun=true;
		}else if(walkRun==false){
			if(validMove(2,direction)){
				runWalkExecute(2,direction);
			}
			walkRun=true;
		}
		energy-=Params.run_energy_cost;
	}
	
	/**
	 * Sets child's location, energy, etc and changes parents energy; adds child to babies collection
	 * @param offspring The offspring generated by the parent Critter.
	 * @param direction The direction from the parent Critter in which to spawn the offspring.
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(energy<=Params.min_reproduce_energy){
			return;
		}
		offspring.x_coord=this.x_coord;
		offspring.y_coord=this.y_coord;
		offspring.walkRun=false;
		offspring.energy=this.energy/2;
		this.energy=this.energy-(int)this.energy/2;
		offspring.runWalkExecute(1,direction);
		babies.add(offspring);
		
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
	 * @param critter_class_name String name of the critter class
	 * @throws InvalidCritterException Problem making Critter
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		Class<?> in;
		Critter c;
		Object objectIn;
		//Check if class exists
		try {
			 in=Class.forName(myPackage+"."+critter_class_name);
		}catch(ClassNotFoundException ex ) {
			 throw new InvalidCritterException(critter_class_name);
		}
		catch(LinkageError ex){
			throw new InvalidCritterException(critter_class_name);
		}
		//Check if class is instance of critter
		try{
			objectIn=in.newInstance();
			if(objectIn instanceof Critter){
				c=(Critter)objectIn;
			}
			else{
				throw new InvalidCritterException(critter_class_name);
			}
		}
		catch(IllegalAccessException ex){
			throw new InvalidCritterException(critter_class_name);
		}
		catch(InstantiationException ex){
			throw new InvalidCritterException(critter_class_name);
		}
		//Install critter into collection
		population.add(c);
		//Set critter attributes
		c.energy=Params.start_energy;
		c.x_coord=getRandomInt(Params.world_width);
		c.y_coord=getRandomInt(Params.world_height);
		//Re-populate the grid 
		makeGrid();
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException Problem getting instances of Critter
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		Class<?> in;
		try {
			 in=Class.forName(myPackage+"."+critter_class_name);
		} catch( ClassNotFoundException e ) {
			if(Main.DEBUG){
				System.out.println("Problem in getInstances method: ClassNotFound");
			}
			 throw new InvalidCritterException(critter_class_name);
		}
		//Check if object is actually instance of critter if so initialize it
		for(Critter c:population){
			if(in.isAssignableFrom(c.getClass())){
				result.add(c);
			}
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
		grid=new Critter[Params.world_height][Params.world_width];
	}
	
	/**
	 * Simulates one time step of the world
	 */
	public static void worldTimeStep() {
		//Set the old x and y values for each critter; implemented to satisfy look method requirements
		setOldXandY();
		//Do time step for each critter
		doTimeStepForEachCritter();
		//Resolve encounters over overlapping critters
		resolveEncountersBetweenCritters();
		//Update the rest energy
		updateEnergy();
		//Generate the algae
		generateAlgae();
		//Remove the dead from the population
		removeDead();
		//Add the babies to the population
		addBabies();
		//Make the new display grid
		makeGrid();
	}
	public static void setOldXandY(){
		for(Critter c:population){
			c.oldX=c.x_coord;
			c.oldY=c.y_coord;
		}
	}
	
	/**
	 * Generates algae and adds them to population
	 */
	private static void generateAlgae(){
		Critter al;
		int made=0;
		while(made<Params.refresh_algae_count){
			al=new Algae();
			al.energy=Params.start_energy;
			al.x_coord=getRandomInt(Params.world_width);
			al.y_coord=getRandomInt(Params.world_height);
			population.add(al);
			made++;
		}
	}
	
	/**
	 * Removes the dead critters from population
	 */
	private static void removeDead(){
		//Remove dead critters
		for(int i=0;i<population.size();i++){
			if(population.get(i).energy<=0){
				population.remove(i);
				i--;
			}   
		}
		
		
	}
	
	/**
	 * Adds the babies to the population
	 */
	public static void addBabies(){
		//Add babies to population
				population.addAll(babies);
				babies.clear();
		
		
	}
	
	/**
	 * Executes the doTimeStep() function for each Critter
	 */
	private static void doTimeStepForEachCritter(){
		//Do time step for each critter
		for(Critter c:population){
			c.walkRun=false; 
			c.inTimeStep=true; 
			c.doTimeStep();
			c.inTimeStep=false; 
		}
	}
	
	/**
	 * Updates the energy of each Critter and populates a 2d grid array
	 */
	private static void updateEnergy(){
			//Update rest energy and grid
		for(Critter c:population){
			c.energy=c.getEnergy()-Params.rest_energy_cost;
		}
	}
	
	/**
	 * Make a new grid for every
	 */
	private static void makeGrid(){
		//Check if grid changed
		if(grid.length!=Params.world_height){
			grid=new Critter[Params.world_height][Params.world_width];
		}
		else if(grid.length>0 && grid[0].length!=Params.world_width){
			grid=new Critter[Params.world_height][Params.world_width];
		}
		//Nullify grid
		for(int i=0;i<grid.length;i++){
			for(int j=0;j<grid[0].length;j++){
				grid[i][j]=null;
			}
		}
		//Update rest energy and grid
		for(Critter c:population){
			if(c.energy>0){
				grid[c.y_coord][c.x_coord]=c;
			}
		}
	}
	
	/**
	 * Resolves encounters between critters
	 */
	private static void resolveEncountersBetweenCritters(){
		//Resolve conflicts for each coordinate
				Critter a,b;
				boolean aFightB,bFightA;
				int diceRollA;
				int diceRollB;
				//Go through each Critter in the population
				for(int i=0;i<population.size();i++){
					a=population.get(i);
					a.inTimeStep=false;
					for(int j=i+1;j<population.size();j++){
						b=population.get(j);
						b.inTimeStep=false;
						//If a's energy is less then 0 no need to check
						if(a.energy<=0){
							break;
						}
						//If b's energy is less then 0 go to next Critter
						if(b.energy<=0){
							continue;
						}
						if(!sameLocation(a, b)){
							continue;
						}
						//See if each Critter want to fight each other
						aFightB=a.fight(b.toString());
						bFightA=b.fight(a.toString());
						diceRollA=0;
						diceRollB=0;
						//Only replace one critter with the other if both are in the same location and have energy greater than zero
						if(a.getEnergy()>0&& b.getEnergy()>0 && sameLocation(a,b)){
							if(aFightB){
								diceRollA=getRandomInt(a.energy+1);
							}
							if(bFightA){
								diceRollB=getRandomInt(b.energy+1);
							}
							if(diceRollA>diceRollB){
								a.energy=a.getEnergy()+b.getEnergy()/2;
								b.energy=0;
							}else{
								b.energy=b.getEnergy()+a.getEnergy()/2;
								a.energy=0;
							}
						}
						
					}
				}
	}
	
	
	/**
	 * Displays the grid with critters
	 */
	public static void displayWorld() {
		displayHeader(grid[0].length);
		for(int row=0;row<grid.length;row++){
			System.out.print("|");
			for(int col=0;col<grid[0].length;col++){
				if(grid[row][col]!=null){
					System.out.print(grid[row][col].toString());
				}
				else{
					System.out.print(" ");
				}
			}
			System.out.print("|\n");
		}
		displayHeader(grid[0].length);
		Main.fxDisplayGrid();
	}
	
	/**
	 * Displays the top/bottom portion of the grid view plus sign followed by hyphens then a plus sign
	 * @param length The width of the world.
	 */
	private static void displayHeader(int length){
		System.out.print("+");
		for(int i=0;i<length;i++){
			System.out.print("-");
		}
		System.out.print("+\n");
	}
	
	/**
	 * Checks if the critters are in the same location 
	 * @param a First Critter
	 * @param b Second Critter
	 * @return True if both critters are in the same location, false otherwise
	 */
	private static boolean sameLocation(Critter a,Critter b){
		if(a.x_coord==b.x_coord && b.y_coord==a.y_coord){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the current critter's location is occupied
	 * @param a Critter
	 * @return True if the space the critter is in is occupied
	 */
	public static boolean occupied(Critter a){
		for(Critter c:population){
			if(c.energy>0 && sameLocation(a,c)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the current x and y coordinate are free
	 * @param x X coordinate or column to check
	 * @param y Y coordinate or row to check
	 * @return True if the (x,y) coordinate is occupied
	 */
	private static boolean occupied(int x, int y){
		for(Critter c:population){
			if(c.energy>0 && c.x_coord==x && c.y_coord==y){
				return true;
			}
		}
		
		return false;
	}
	
}
