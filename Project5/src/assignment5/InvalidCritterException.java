/* CRITTERS InvalidCritterException.java
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

public class InvalidCritterException extends Exception {
	String offending_class;
	
	public InvalidCritterException(String critter_class_name) {
		offending_class = critter_class_name;
	}
	
	public String toString() {
		return "Invalid Critter Class: " + offending_class;
	}

}
