package it.polito.tdp.borders.model;

public class Country implements Comparable<Country>{
	private String stateName;
	private String stateAbb;
	private int cCode;
	
	

	public Country(String stateName, String stateAbb, int stateCode) {
		super();
		this.stateName = stateName;
		this.stateAbb = stateAbb;
		this.cCode = stateCode;
	}
	
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStateAbb() {
		return stateAbb;
	}

	public void setStateAbb(String stateAbb) {
		this.stateAbb = stateAbb;
	}

	public int getcCode() {
		return cCode;
	}

	public void setcCode(int stateCode) {
		this.cCode = stateCode;
	}

	@Override
	public String toString() {
		return this.stateName;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cCode;
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
		Country other = (Country) obj;
		if (cCode != other.cCode)
			return false;
		return true;
	}

	@Override
	public int compareTo(Country o) {
		return this.getStateName().compareTo(o.getStateName());
	}
	

}
