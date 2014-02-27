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
	public NullDate() {}
	
	/**
	 * Parses a long into a Date or NullDate.
	 * 
	 * @param 	date long representing a date.
	 * @return	Date object unless the long passed is 0l; in this case
	 * 			a NullDate is returned.
	 */
	public static Date parseDate(long date) {
		if (date == 0l) return new NullDate();
		else return new Date(date);
	}
	
	/**
	 * Returns zero.
	 */
	@Override
	public long getTime() {
		return 0l;
	}
	
	/**
	 * Returns an empty String.
	 */
	@Override
	public String toString() {
		return "";
	}
}
