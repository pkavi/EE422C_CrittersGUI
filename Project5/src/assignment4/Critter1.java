/* CRITTERS Critter1.java
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
 * Selective  Run Reproduce Critter: This critter will have minimum fight energy and reproduce energy set by the Param file and will fight at any opportunity where energy is enough or  will try to run away.
 * Critter will try to reproduce first if energy is enough and on correct time step. Or it will run away.
 */
public class Critter1 extends Critter{
	//Field to randomize actions at timeSteps and energy levels
	private int critter1timeStep=0;
	private int reproducePeriod=1;
	private int runPeriod=1;
	private int reproduceEnergyMinimum=1;
	private int fightEnergyMinimum=1;

	/**
	 * This makes a new Critter1 and initializes its params in relation to the Params file
	 */
	public Critter1() {
		fightEnergyMinimum=Params.rest_energy_cost*10;
		reproduceEnergyMinimum=Params.rest_energy_cost*5;
		reproducePeriod=5;
		runPeriod=2;
	}
	
	/**
	 * Returns String representing Critter1
	 * @return "1"
	 * {@inheritDoc}
	 */
	public String toString() { return "1"; }
	
	/**
	 * Critter1 will always fight but will only stay in current position if above a certain energy 
	 * @param critterType String representing the other critter
	 * @return false
     * {@inheritDoc}
     */
	public boolean fight(String critterType) { 
		if(getEnergy()>fightEnergyMinimum){
			return true;
		}else{
			run(Critter.getRandomInt(8));
			return true;
		}
	}

	
	/**
	 * Makes Critter1 reproduce and run based on time step number and energy
     * {@inheritDoc}
     */
	public void doTimeStep() {
		if(getEnergy()>reproduceEnergyMinimum && critter1timeStep%reproducePeriod==0){
			Critter1 newCritter=new Critter1();
			reproduce(newCritter,Critter.getRandomInt(8));
		}
		if(critter1timeStep%runPeriod==0){
			run(Critter.getRandomInt(8));
		}
		critter1timeStep++;
	}
}
