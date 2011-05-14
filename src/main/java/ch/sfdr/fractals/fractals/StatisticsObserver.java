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
	void statisticsDataAvailable();
}
