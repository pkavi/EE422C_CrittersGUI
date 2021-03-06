/* CRITTERS Critter3.java
 * EE422C Project 5 submission by
 * Pranav Kavikondala
 * pk6994
 * 16470
 * Daniel John
 * dcj597
 * 16480
 * Slip days used: 0
 * Fall 2016
 */
package assignment5;

import assignment5.Critter.CritterShape;

/**
 * @author Pranav Kavikondala
 * 
 * Simple Reproducing Critter: Will only reproduce and will always fight
 */
public class Critter3 extends Critter{
	
	/**
	 * Retruns String representing Critter3
	 * @return "3"
	 * {@inheritDoc}
	 */
	public String toString() { return "3"; }
	
	/**
	 * This makes a new Critter3
	 */
	public Critter3() {

	}
	public  CritterShape viewShape(){
		return CritterShape.SQUARE;
	}
	
	/**
	 * Critter3 will always fight
	 * @param critterType String representing the other critter
	 * @return true
     * {@inheritDoc}
     */
	public boolean fight(String critterType) { 
		return true;
	
	}

	/**
	 * Makes Critter3 keep on reproducing till not enough energy
     * {@inheritDoc}
     */
	public void doTimeStep() {
		while(this.getEnergy()>Params.min_reproduce_energy){
			Critter newCrit=new Critter3();
			reproduce(newCrit,Critter.getRandomInt(8));
		}
	}
}
