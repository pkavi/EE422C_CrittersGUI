/* CRITTERS Critter2.java
 * EE422C Project 4 submission by
 * Pranav Kavikondala
 * pk6994
 * 16470
 * Slip days used: 0
 * Fall 2016
 */
package assignment4;
/**
 * @author Pranav Kavikondala
 * 
 * Randomized fight, walk and reproduce critter: Will only fight if against certain opponents and will only walk or reproduce 
 * Will walk only if randomized number is correct, Will only reproduce if randomized number is correct
 */
public class Critter2 extends Critter{

	/**
	 * Returns String representing Critter2
	 * @return "2"
	 * {@inheritDoc}
	 */
	public String toString() { return "2"; }
	
	/**
	 * This makes a new Critter2
	 */
	public Critter2() {

	}
	

	/**
	 * Critter2 will not fight against Craig
	 * @param critterType String representing the other critter
	 * @return true if Critter will fight against specified critter type
     * {@inheritDoc}
     */
	public boolean fight(String critterType) { 
		if(critterType.equals("1")){
			return true;
		}
		else if(critterType.equals("C")){
			return false;
		}
		else if(critterType.equals("2")){
			return true;
		}else{
			return true;
		}
	}

	/**
	 * Makes Critter2 reproduce and walk at random time steps
     * {@inheritDoc}
     */
	public void doTimeStep() {
		if(Critter.getRandomInt(3)==2){
			walk(Critter.getRandomInt(8));
		}
		if(Critter.getRandomInt(3)==0){
			Critter2 newCritter2=new Critter2();
			reproduce(newCritter2,Critter.getRandomInt(8));
		}
	}
	
}
