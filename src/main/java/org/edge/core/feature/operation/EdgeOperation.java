package org.edge.core.feature.operation;

public interface EdgeOperation {
	
	/**
	 *-Type of Analytics operation (filtering, statistics)
	 * @return
	 */
	String type();

	/**
	 *   -MI (million Instructions) operation per MB of data

	 * @return
	 *//*
	long speedInMB();
	*/
	
	
	/**
	 * 
	 * return the factor representing the size of processed edgelet
	 * example: 
	 *  return 0.5d, meaning the size of processed edgelet will be decreased by a factor of 2 
	 * 
	 * @return
	 */
	public double getShinkingFactor();
	
	/**
	 * 
	 * return the factor representing the how many times the the size of edgelet will be increased  
	 * example: 
	 *  return 2, meaning the size of processing edgelet will be increased by a factor of 2 
	 * 
	 * @return
	 */
	public double getIncreasingFactor();
}
