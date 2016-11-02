package br.cefet.tcc.aplication;

import java.util.ArrayList;

public class ComponentOwnership {
	private String path;
	//private ArrayList<String> authors;
	private String author;
	private double ownership;
	
	public ComponentOwnership(String path, String author, double ownership) {
		this.path = path;
		this.author = author;
		this.ownership = ownership;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthors(String author) {
		this.author = author;
	}
	public double getOwnership() {
		return ownership;
	}
	public void setOwnership(double ownership) {
		this.ownership = ownership;
	}
}
