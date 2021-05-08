package org.edge.core.edge;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import org.edge.core.feature.Battery;
import org.edge.core.feature.EdgeLet;
import org.edge.core.feature.EdgeType;
import org.edge.core.feature.Mobility;
import org.edge.entity.ConnectionHeader;
import org.edge.network.NetworkModel;
import org.edge.utils.LogUtil;

public class EdgeDevice extends Host {

	private EdgeType type;
	private boolean enabled;
	private int max_IoTDevice_capacity;
	private double battery_drainage_rate;
	private List<Integer> attached_IoTDevices;
	private Battery battery;
	private List<EdgeLet> pendingResponse;
	private Mobility geo_location;

	/**
	(int,
	RamProvisioner,
	BwProvisioner,
	long,
	List<Pe>,
	VmScheduler, Object, Location, NetworkModel, int, double, double, double) is undefined
	 */
	public EdgeDevice(int id,
			RamProvisioner ramProvisioner,
			BwProvisioner bwProvisioner,
			long storage,
			List<? extends Pe> peList,
			VmScheduler vmScheduler,
			EdgeType edgeType,
			NetworkModel networkModel,
			int max_IoTDevice_capacity,
			double max_battery_capacity,
			double battery_drainage_rate,
			double current_battery_capacity) {
		super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
		this.type = edgeType;
		this.enabled = true;

		this.max_IoTDevice_capacity = max_IoTDevice_capacity;

		this.battery_drainage_rate = battery_drainage_rate;

		this.attached_IoTDevices = new ArrayList<>();
		this.battery = new Battery(max_battery_capacity, current_battery_capacity);
		this.pendingResponse = new ArrayList<>();
	}

	public List<EdgeLet> getPendingResponse() {
		return this.pendingResponse;
	}

	public double getCurrentBatteryCapacity() {
		return this.battery.getCurrentCapacity();
	}

	public double getMaxBatteryCapacity() {
		return this.battery.getMaxCapacity();
	}

	public double getBattery_drainage_rate() {
		return this.battery_drainage_rate;
	}

	public Mobility getLocation() {
		return this.geo_location;
	}

	public void setMobility(Mobility geo_location) {
		this.geo_location = geo_location;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getMax_IoTDevice_capacity() {
		return this.max_IoTDevice_capacity;
	}

	public void setMax_IoTDevice_capacity(int max_IoTDevice_capacity) {
		this.max_IoTDevice_capacity = max_IoTDevice_capacity;
	}

	public boolean getAvailability() {
		return this.isEnabled() && (this.getMax_IoTDevice_capacity() > this.attached_IoTDevices.size())
				&& (this.getVmList().size() > 0);
	}

	public int getNumberOfAttachedLoTDevice() {
		return this.attached_IoTDevices.size();
	}

	public boolean connect_IoT_device(ConnectionHeader header) {
		if (this.getNumberOfAttachedLoTDevice() >= this.max_IoTDevice_capacity) {
			LogUtil.info("Connection failure due to excess the max capacity of IoTDevice in the host");
			return false;
		}
		this.attached_IoTDevices.add(header.ioTId);
		return true;
	}

	public void removeConnection(ConnectionHeader header) {
		Integer ioTId = header.ioTId;
		this.attached_IoTDevices.remove(ioTId);
	}

	public void update_geolocation() {
	}

	//TODO
	public void updateBatteryByProcessingCloudLet() {
		//LogUtil.info(battery.getMaxCapacity() + " " + battery.getCurrentCapacity() + " " + battery_drainage_rate);
		this.battery.setCurrentCapacity(this.battery.getCurrentCapacity()-this.battery_drainage_rate);
		if (this.battery.getCurrentCapacity() <= 0) {
			LogUtil.info("Edge device " + this.getId() + "( VM " + this.getVmList().get(0).getId() + " )" + "'s battery has drained");
			this.setEnabled(false);
		}
	}

	public void updateBatteryByProcessingCloudLetAndSend(double fileSize, double shrinkFactor, double drangeRateForProcess, double drangeRateForSending) {
		//LogUtil.info(battery.getMaxCapacity() + " " + battery.getCurrentCapacity() + " " + battery_drainage_rate);

		double updateByProcess = fileSize * (1-shrinkFactor) * drangeRateForProcess;
		double updateBySending = fileSize * shrinkFactor * drangeRateForSending;

		LogUtil.info("Edge device " + this.getId() + " -  " + this.getVmList().get(0).getId()
				+ " ( updateByProcess = " + updateByProcess + " )" + " ( updateBySending = " + updateBySending + " )");
		this.battery.setCurrentCapacity(this.battery.getCurrentCapacity()-(updateByProcess+updateBySending));

		if (this.battery.getCurrentCapacity() <= 0) {
			LogUtil.info("Edge device " + this.getId() + "( VM " + this.getVmList().get(0).getId() + " )" + "'s battery has drained");
			this.setEnabled(false);
			CloudSim.terminateSimulation();
		}
	}

	public void updateBatteryByProcessingCloudLetAndSend2(double fileSize, double shrinkFactor, double drangeRateForProcess, double drangeRateForSending) {
		//LogUtil.info(battery.getMaxCapacity() + " " + battery.getCurrentCapacity() + " " + battery_drainage_rate);

		double updateByProcess = fileSize * (1-shrinkFactor) * drangeRateForProcess;
		double updateBySending = fileSize * shrinkFactor * drangeRateForSending;

		LogUtil.info("Edge device " + this.getId() + " -  " + this.getVmList().get(0).getId()
				+ " ( updateByProcess = " + updateByProcess + " )" + " ( updateBySending = " + updateBySending + " )");
		this.battery.setCurrentCapacity(this.battery.getCurrentCapacity()-(updateByProcess+updateBySending));

		if (this.battery.getCurrentCapacity() <= 0) {
			LogUtil.info("Edge device " + this.getId() + "( VM " + this.getVmList().get(0).getId() + " )" + "'s battery has drained");
			this.setEnabled(false);
		}
	}

	/**
	 * Edge-to-edge communication:
	 * when the edge device cannot get connected with the previous IoT device,
	 * and has found a new replacement of itself that can get connected with the IoT,
	 * it will set this data package to this edge device
	 * @param let
	 */
	public void addPendingResponse(EdgeLet let) {
		ConnectionHeader connectionHeader = let.getConnectionHeader();
		connectionHeader.vmId = this.getVmList().get(0).getId();
		let.setVmId(connectionHeader.vmId);
		this.pendingResponse.add(let);
		LogUtil.info("Add pending " + pendingResponse.size() + " VmId");
	}

}
