
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
	@FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbPaese"
    private ComboBox<Country> cmbPaese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbModalità"
    private ComboBox<String> cmbModalità; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	this.txtResult.clear();
    	int anno;
    	
    	try {
    		anno = Integer.parseInt(txtAnno.getText());
    		if((anno<1816)||(anno>2016)) {
    			this.txtResult.setText("Inserire un anno tra il 1816 e il 2016");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un anno tra il 1816 e il 2016");
    		return;
    	}
    	
    	try {
    		model.creaGrafo(anno);
    		List<Country> countries = model.getCountries();
    		this.cmbPaese.getItems().addAll(countries);
    		
    		this.txtResult.appendText("Numero di componenti connesse "+model.getNumberOfConnectedComponents()+"\n");
    		
    		Map<Country, Integer> stats = model.getCountryCounts();
    		for(Country c: stats.keySet()) {
    			this.txtResult.appendText(c+" "+stats.get(c)+"\n");
    		}
    		
    	}catch(RuntimeException e) {
    		this.txtResult.setText("Errore: "+e+"\n");
    		return;
    	}
    }

    @FXML
    void doCalcolaStatiRaggiungibili(ActionEvent event) {
    	this.txtResult.clear();
    	List<Country> reachableCountries;
    	if(this.cmbPaese.getItems().isEmpty()) {
    		this.txtResult.setText("Graph is empty. Create a graph or select another year");
    	}
    	
    	Country selectedCountry = this.cmbPaese.getValue();
    	if(selectedCountry==null) {
    		txtResult.setText("Select a country first");
    	}
    	String mode = this.cmbModalità.getValue();
    	if(mode==null) {
    		txtResult.setText("Select a mode first");
    	}
    	try {
    		switch(mode) {
    			case "BreadthFirstIterator":
    				try {
    					reachableCountries =model.getReachableCountries(selectedCountry, "BreadthFirstIterator");
    					for(Country c: reachableCountries) {
    						this.txtResult.appendText(c+"\n");
    					}
    				}catch(RuntimeException e) {
    					this.txtResult.setText("Selected country is not into the graph");
    				}
    				break;
    			case "DepthFirstIterator":
    				try {
    					reachableCountries =model.getReachableCountries(selectedCountry, "DepthFirstIterator");
    					for(Country c: reachableCountries) {
    						this.txtResult.appendText(c+"\n");
    					}
    				}catch(RuntimeException e) {
    					this.txtResult.setText("Selected country is not into the graph");
    				}
    				break;
    			case "Iterative":
    				try {
    					reachableCountries =model.getReachableCountries(selectedCountry, "Iterative");
    					for(Country c: reachableCountries) {
    						this.txtResult.appendText(c+"\n");
    					}
    				}catch(RuntimeException e) {
    					this.txtResult.setText("Selected country is not into the graph");
    				}
    				break;
    			case "Recursive":
    				try {
    					reachableCountries =model.getReachableCountries(selectedCountry, "Recursive");
    					for(Country c: reachableCountries) {
    						this.txtResult.appendText(c+"\n");
    					}
    				}catch(RuntimeException e) {
    					this.txtResult.setText("Selected country is not into the graph");
    				}
    				break;
    			default:
    				this.txtResult.setText("Select a mode first");
    				break;
    		}
    		
    	}catch(NullPointerException e) {
    		this.txtResult.setText("Select a mode first");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbPaese != null : "fx:id=\"cmbPaese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbModalità != null : "fx:id=\"cmbModalità\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbModalità.getItems().addAll("BreadthFirstIterator", "DepthFirstIterator","Iterative","Recursive");
    }
}
