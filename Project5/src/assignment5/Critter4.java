/* CRITTERS Critter4.java
 * EE422C Project 4 submission by
 * Pranav Kavikondala
 * pk6994
 * 16470
 * Slip days used: 0
 * Fall 2016
 */
package assignment5;

import assignment5.Critter.CritterShape;

/**
 * @author Pranav Kavikondala
 * 
 * Pacifist Critter: always runs away never wants to fight
 */
public class Critter4 extends Critter{
	
	/**
	 * Returns String representing Critter4
	 * @return "4"
	 * {@inheritDoc}
	 */
	public String toString() { return "4"; }
	
	/**
	 * This makes a new Critter4
	 */
	public Critter4() {

	}
	public  CritterShape viewShape(){
		return CritterShape.TRIANGLE;
	}
	/**
	 * Critter4 will never fight
	 * @param critterType String representing the other critter
	 * @return false
     * {@inheritDoc}
     */
	public boolean fight(String critterType) { 
		return false;
	}

	

	/**
	 * Makes Critter4 run in random direction
     * {@inheritDoc}
     */
	public void doTimeStep() {
		run(Critter.getRandomInt(8));
	}
}