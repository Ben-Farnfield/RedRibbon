package uk.ac.bradford.pisoc.redribbon.util;

import java.util.Date;

/**
 * Takes the place of 'no date'.
 * 
 * @author 	Ben Farnfield
 */
@SuppressWarnings("serial")
public class NullDate extends Date {

	/**
	 * Default constructor does not call its parents constructor.
	 */
	public NullDate() { }
	
	/**
	 * Returns an empty String.
	 */
	@Override
	public String toString() {
		return "";
	}
}
