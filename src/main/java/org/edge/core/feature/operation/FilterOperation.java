package org.edge.core.feature.operation;

public class FilterOperation implements EdgeOperation {

	private static final String type = "filter";
	
	@Override
	public String type() {
		return type;
	}

	@Override
	public double getShinkingFactor() {
		return 0.5;
	}

	@Override
	public double getIncreasingFactor() {
		return 2;
	}

}
