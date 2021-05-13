package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private BordersDAO dao;
	private List<Country> countries;
	private CountryIdMap countryIdMap;
	private SimpleGraph<Country, DefaultEdge> grafo;
	Map<Country, Country> predecessore;
	
	public Model() {
		dao= new BordersDAO();
		
		
		
	}
	
	
	public void creaGrafo(int anno) {
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		this.countryIdMap= new CountryIdMap();
		this.countries= dao.loadAllCountries(countryIdMap);
		List<Border> confini = dao.getCountryPairs(countryIdMap, anno);
		
		if(confini.isEmpty()) {
			throw new RuntimeException("No country pairs for this year");
		}
		
		/*
		//aggiunta vertici
		Graphs.addAllVertices(grafo, countries);
		//aggiungo archi
		for(Border c: confini) {
			this.grafo.addEdge(c.getC1(), c.getC2());
		}
		*/
		
		for(Border b: confini) {
			grafo.addVertex(b.getC1());
			grafo.addVertex(b.getC2());
			grafo.addEdge(b.getC1(), b.getC2());
		}
		
		System.out.println("Inseriti" +grafo.vertexSet().size()+ "vertici e "+grafo.edgeSet().size()+" archi" );
		
		countries = new ArrayList<>(grafo.vertexSet());
		Collections.sort(countries);
	}
	
	public List<Country> getCountries(){
		if(countries==null) {
			return new ArrayList<Country>();
		}
		return countries;
	}
	
	public Map<Country, Integer> getCountryCounts(){
		if(this.grafo==null) {
			throw new RuntimeException("Grafo inesistente");
		}
		
		Map<Country, Integer> result = new HashMap<Country, Integer>();
		for(Country c: grafo.vertexSet()) {
			result.put(c, grafo.degreeOf(c));
		}
		return result;
	}
	
	public int getNumberOfConnectedComponents() {
		if(this.grafo==null) {
			throw new RuntimeException("Grafo inesistente");
		}
		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<Country, DefaultEdge>(grafo);
		return ci.connectedSets().size();
	}
	
	public List<Country> getReachableCountries(Country choosedCountry, String mode){
		if(!grafo.vertexSet().contains(choosedCountry)) {
			throw new RuntimeException("The country is not into the graph");		
		}
		
		List<Country> reachableCountry;
		if(mode.equals("BreadthFirstIterator")) {
			reachableCountry = displayAllNeighboursJGraphTBFV(choosedCountry);
			return reachableCountry;
		}else if(mode.equals("DepthFirstIterator")) {
			reachableCountry = displayAllNeighboursJGraphTDFV(choosedCountry);
			return reachableCountry;
		}else if(mode.equals("Iterative")) {
			reachableCountry = displayAllNeighboursIterative(choosedCountry);
			return reachableCountry;
		}else if(mode.equals("Recursive")) {
			reachableCountry = displayAllNeighboursRecursive(choosedCountry);
			return reachableCountry;
		}else {
			return null;
		}
		
		/*List<Country> reachableCountry = displayAllNeighboursIterative(choosedCountry);
		System.out.println("Paesi raggiungibili: "+reachableCountry.size());
		
		reachableCountry = displayAllNeighboursJGraphTDFV(choosedCountry);
		System.out.println("Paesi raggiungibili: "+reachableCountry.size());
		
		reachableCountry = displayAllNeighboursJGraphTBFV(choosedCountry);
		System.out.println("Paesi raggiungibili: "+reachableCountry.size());
		
		reachableCountry = displayAllNeighboursRecursive(choosedCountry);
		System.out.println("Paesi raggiungibili: "+reachableCountry.size());*/
		
		
		
	}

	

	private List<Country> displayAllNeighboursJGraphTDFV(Country choosedCountry) {
		List<Country> visited = new LinkedList<Country>();
		DepthFirstIterator<Country, DefaultEdge> dfv = new DepthFirstIterator<Country, DefaultEdge>(this.grafo, choosedCountry);
		
		while(dfv.hasNext()) {
			visited.add(dfv.next());
		}
		return visited;
	}
	
	private List<Country> displayAllNeighboursJGraphTBFV(Country choosedCountry) {
		List<Country> visited = new LinkedList<Country>();
		BreadthFirstIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<Country, DefaultEdge>(this.grafo, choosedCountry);
		
		while(bfv.hasNext()) {
			visited.add(bfv.next());
		}
		return visited;
	}

	private List<Country> displayAllNeighboursIterative(Country choosedCountry) {
		List<Country> visited = new LinkedList<Country>();
		List<Country> toBeVisited = new LinkedList<Country>();
		
		visited.add(choosedCountry);
		toBeVisited.addAll(Graphs.neighborListOf(grafo, choosedCountry));
		
		while(!toBeVisited.isEmpty()) {
			Country temp = toBeVisited.remove(0);
			visited.add(temp);
			List<Country> listaDeiVicini = Graphs.neighborListOf(grafo, temp);
			listaDeiVicini.removeAll(visited);
			listaDeiVicini.removeAll(toBeVisited);
			toBeVisited.addAll(listaDeiVicini);
		}
		return visited;
	}
	
	private List<Country> displayAllNeighboursRecursive(Country choosedCountry) {
		List<Country> visited = new LinkedList<Country>();
		cerca(visited, choosedCountry);

		return visited;
	}

	private void cerca(List<Country> visited, Country choosedCountry) {
		visited.add(choosedCountry);
		
		for(Country c: Graphs.neighborListOf(grafo, choosedCountry)) {
			if(!visited.contains(c)) {
				cerca(visited, c);
			}
		}
		
	}

}
