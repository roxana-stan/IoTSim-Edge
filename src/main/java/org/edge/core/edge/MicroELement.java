package org.edge.core.edge;

import java.util.List;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;
import org.edge.core.feature.operation.EdgeOperation;

public class MicroELement extends Vm {

	private EdgeOperation edgeOperation;
	private int datasize;
	private int payload;
	private MicroELement upLink;
	private List<MicroELement> downLink;

  /**
   * TODO 
   * Structure
   * MI 
   * *change the computing time for VMs
   * 
   * 
   */
	
	public MicroELement(int id, int userId, double mips, int numberOfPes,
						int ram, long bw, long size, String vmm,
						CloudletScheduler cloudletScheduler) {
		super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
	}
	
	public MicroELement(int id, int userId, double mips, int numberOfPes,
						int ram, long bw, long size, String vmm,
						CloudletScheduler cloudletScheduler, String type, float shrinkingFactor) {
		super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
	}
	
	public EdgeOperation getEdgeOperation() {
		return edgeOperation;
	}
	
	public void setEdgeOperation(EdgeOperation edgeOperation) {
		this.edgeOperation = edgeOperation;
	}
	
	public void updateDataSize() {
	}
	
	public MicroELement getUpLink() {
		return upLink;
	}

	public void setUpLink(MicroELement upLink) {
		this.upLink = upLink;
	}

	public List<MicroELement> getDownLink() {
		return downLink;
	}

	public void setDownLink(List<MicroELement> downLink) {
		this.downLink = downLink;
	}

	public int getDatasize() {
		return datasize;
	}

	public void setDatasize(int datasize) {
		this.datasize = datasize;
	}

	public int getPayload() {
		return payload;
	}

	public void setPayload(int payload) {
		this.payload = payload;
	}

}
