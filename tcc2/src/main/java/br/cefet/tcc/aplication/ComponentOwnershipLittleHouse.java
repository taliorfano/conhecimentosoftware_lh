package br.cefet.tcc.aplication;

import java.util.ArrayList;

public class ComponentOwnershipLittleHouse {
	private String path;
	private String componentLittleHouse;
	private ArrayList<String> authors;
	private double ownership;
	
	
	public ComponentOwnershipLittleHouse(String path, String componentLittleHouse) {
		this.path = path;
		this.componentLittleHouse = componentLittleHouse;
		this.authors = new ArrayList<>();
		this.ownership = 0.0;
	}
	
	public ComponentOwnershipLittleHouse(String path, String componentLittleHouse, ArrayList<String> authors,
			double ownership) {
		this.path = path;
		this.componentLittleHouse = componentLittleHouse;
		this.authors = authors;
		this.ownership = ownership;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getComponentLittleHouse() {
		return componentLittleHouse;
	}
	public void setComponentLittleHouse(String componentLittleHouse) {
		this.componentLittleHouse = componentLittleHouse;
	}
	public ArrayList<String> getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}
	public double getOwnership() {
		return ownership;
	}
	public void setOwnership(double ownership) {
		this.ownership = ownership;
	}
}
