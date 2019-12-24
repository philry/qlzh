package com.sy.vo;

import java.io.Serializable;

public class EngineeringResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3262781987325948181L;

	private String name;
	
	private String pName;
	
	private Integer deptId;

	private Integer workTime;
	
	private Integer time;
	
	private Double efficiency;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Integer workTime) {
		this.workTime = workTime;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Double getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(Double efficiency) {
		this.efficiency = efficiency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((efficiency == null) ? 0 : efficiency.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((workTime == null) ? 0 : workTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EngineeringResult other = (EngineeringResult) obj;
		if (efficiency == null) {
			if (other.efficiency != null)
				return false;
		} else if (!efficiency.equals(other.efficiency))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (workTime == null) {
			if (other.workTime != null)
				return false;
		} else if (!workTime.equals(other.workTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EngineeringResult [name=" + name + ", workTime=" + workTime + ", time=" + time + ", efficiency="
				+ efficiency + "]";
	}
	
	
	
}
