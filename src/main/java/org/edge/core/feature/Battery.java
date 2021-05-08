package org.edge.core.feature;

public class Battery {

	private double maxCapacity;
	private double currentCapacity;

	public double getCurrentCapacity() {
		return currentCapacity;
	}

	public void setCurrentCapacity(double currentCapacity) {
		this.currentCapacity = currentCapacity;
	}

	public double getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(double maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public Battery(double maxCapacity, double currentCapacity) {
		this.maxCapacity = maxCapacity;
		this.currentCapacity = currentCapacity;
		if (currentCapacity > maxCapacity)
			currentCapacity = maxCapacity;
	}

}
