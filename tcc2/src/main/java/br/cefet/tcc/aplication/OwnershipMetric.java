package br.cefet.tcc.aplication;

/*
 * Entidade de metrica de propriedade
 * Atributos: nome do arquivo, autor e valor da metrica,
 * segundo metodo de Bird replicado por Foucault
 * 
 * @author: Talita Santana Orfano
 * @date: set/2016
 */
public class OwnershipMetric {
	
	// Nome do arquivo
	private String File;
	// Nome do autor
	private String Author;
	// Valor da metrica de propriedade de codigo, segundo Foucault
	private double ValueMetric;
	
	public OwnershipMetric() {
		File = "";
		Author = "";
		ValueMetric = 0.0;
	}
	
	public OwnershipMetric(String file, String author, double valueMetric) {
		File = file;
		Author = author;
		ValueMetric = valueMetric;
	}
	
	public String getFile() {
		return File;
	}
	public void setFile(String file) {
		File = file;
	}
	public String getAuthor() {
		return Author;
	}
	public void setAuthor(String author) {
		Author = author;
	}
	public double getValueMetric() {
		return ValueMetric;
	}
	public void setValueMetric(double valueMetric) {
		ValueMetric = valueMetric;
	}
}
