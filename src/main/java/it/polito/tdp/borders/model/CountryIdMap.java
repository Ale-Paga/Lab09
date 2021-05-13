package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;

public class CountryIdMap {
	private Map<Integer, Country> map;
	
	public CountryIdMap() {
		map=new HashMap<>();
	}

	public Country get(int c) {
		return map.get(c);
	}
	
	public Country get(Country c) {
		Country old = map.get(c.getcCode());
		if(old==null) {
			map.put(c.getcCode(), c);
			return c;
		}
		return old;
	}
	
	public void put(Country c, int cCode) {
		map.put(cCode, c);
	}
}
