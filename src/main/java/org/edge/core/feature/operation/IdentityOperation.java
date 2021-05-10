package org.edge.core.feature.operation;

public class IdentityOperation implements EdgeOperation {

	@Override
	public String type() {
		return "identity";
	}

	@Override
	public double getShinkingFactor() {
		return 1.0;
	}

	@Override
	public double getIncreasingFactor() {
		return 1.0;
	}

}
