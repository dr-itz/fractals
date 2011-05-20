package ch.sfdr.fractals.fractals;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * Listener for ComplexOrbitCycleFinder. Called whenever a new cycle is found
 * @author D.Ritz
 */
public interface ComplexOrbitCycleListener
{
	/**
	 * Called when a cycle was found. The return value determines if searching
	 * should continue.
	 * @param start The start of the cycle as complex number
	 * @param length The length of the cycle found
	 */
	boolean cycleFound(ComplexNumber start, int length);
}
