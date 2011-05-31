package ch.sfdr.fractals.fractals;

/**
 * A class can implement the StatisticsObserver interface
 * when it wants to be informed of changes in statistics.
 * @author S.Freihofer
 */
public interface StatisticsObserver
{
	/**
	 * Method is called when new statistic data is available
	 */
	void statisticsDataAvailable(Object source);

	/**
	 * indicates an update in the progress for the specified component
	 * @param source
	 * @param percent
	 */
	void updateProgess(Object source, int percent);
}
