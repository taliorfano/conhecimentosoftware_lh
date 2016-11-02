package br.cefet.tcc.aplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
 * Classe responsavel pela geração dos CVS de autoria e propriedade de codigo
 * Classe responsavel pela geração dos CVS de propriedade de codigo a partir do Little House
 * 
 * @author: Talita Santana Orfano
 * @date: set/2016
 */
public class CVSOperations {
	/*
	 * Cria CSV contendo informacoes de autoria para cada clase analisada
	 * Salva caminho da classe, autor e quantidade de modificacoes realizadas pelo autor
	 */
	public void createAuthorshipCsvFile(String nameFile, Map<String, HashMap<String, Integer>> fileAndAuthorsMap) {
		try {

			// Criação de um buffer para a escrita em uma stream
			BufferedWriter bufferwriter = new BufferedWriter(new FileWriter("/home/talita/AutoriaCSV/" + nameFile + ".csv"));
			
			for (Entry<String, HashMap<String, Integer>> key : fileAndAuthorsMap.entrySet()) {
				 System.out.println("Key:"+ key.getKey());
				 bufferwriter.write("Nome arquivo: " + key.getKey() + "\n"); 
				 bufferwriter.write("Autor; Quantidade de modificacoes \n");
				 for ( Entry<String, Integer> key2 : key.getValue().entrySet()) {
					 bufferwriter.write(""+key2.getKey()+";"+key2.getValue()+"\n"); 
					 System.out.println(" " + key2.getKey()+": "+ key2.getValue());
				 }
			 }
			
			bufferwriter.close();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Cria CVS contendo informacoes de metrica de propriedade
	 * Salva caminho do arquivo, proprietario e valor da metrica de ownership 
	 */
	public void createOwnershipMetricCsvFile(String nameFile, ArrayList<OwnershipMetric> ownershipMetricList) {
		try {
			BufferedWriter bufferwriter = new BufferedWriter(new FileWriter("/home/talita/AutoriaCSV/" + nameFile + ".csv"));
			bufferwriter.write("Nome arquivo; Proprietario; Valor da métrica \n");
			for(OwnershipMetric ownershipMetric:ownershipMetricList){
				bufferwriter.write(""+ownershipMetric.getFile()+"; "+ownershipMetric.getAuthor()+
						";"+ownershipMetric.getValueMetric()+"\n");
			}			
			bufferwriter.close();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Cria CVS contendo informacoes da autoria e propriedade de codigo
	 * Salva o caminho do arquivo, autores e quantidade de modificacoes por autor
	 */
	public void createAuthorshipAndMetricOwnershipCsvFile(String nameFile, Map<String, HashMap<String, Integer>> fileAndAuthorsMap) {
		try {

			// Criação de um buffer para a escrita em uma stream
			BufferedWriter bufferwriter = new BufferedWriter(new FileWriter("/home/talita/AutoriaCSV/" + nameFile + ".csv"));
			
			int countAuthor = 0;
			int totalContribuitions = 0;
			double ownershipMetric = 0.0;
			String authorOwner = "";
			
			for (Entry<String, HashMap<String, Integer>> key : fileAndAuthorsMap.entrySet()) {
				 System.out.println("Key:"+ key.getKey());
				 bufferwriter.write("Nome arquivo: " + key.getKey() + "\n"); 
				 bufferwriter.write("Autor; Quantidade de modificacoes \n");
				 for ( Entry<String, Integer> key2 : key.getValue().entrySet()) {
					 bufferwriter.write(""+key2.getKey()+";"+key2.getValue()+"\n"); 
					 System.out.println(" " + key2.getKey()+": "+ key2.getValue());
				 }
			 }
			
			bufferwriter.close();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Cria CSV contendo informacoes de propriedade de codigo a partir do Little House
	 * Salva caminho da classe, autor e quantidade de modificacoes realizadas pelo autor
	 */
	public void createOwnershipLittleHouseCsvFileAll(String nameFile, Map<String, ComponentOwnershipLittleHouse> mapOwnershipLittleHouse) {
		try {

			// Criação de um buffer para a escrita em uma stream
			BufferedWriter bufferwriter = new BufferedWriter(new FileWriter("/home/talita/AutoriaCSV/" + nameFile + ".csv"));
			
			bufferwriter.write("Nome; Componente LH; Proprietario; Valor métrica \n");
			for (Entry<String, ComponentOwnershipLittleHouse> key : mapOwnershipLittleHouse.entrySet()) {
				
				String authors = null;
				 boolean first = true;
				 for (String s : key.getValue().getAuthors())
				 {
					if(first){
						authors = s;
						first = false;
					} 
					else{
						authors = authors +" &"+s;
						System.out.println(authors);
					}
				 }
				 
				if(key.getValue().getComponentLittleHouse().equals("")){
					bufferwriter.write(""+key.getKey()+"; _;"
							+authors +";"+ key.getValue().getOwnership()+"\n");				
				}
				else{
					bufferwriter.write(""+key.getKey()+";"+key.getValue().getComponentLittleHouse()+";"
						+authors +";"+ key.getValue().getOwnership()+"\n");
				}
			}
			
			bufferwriter.close();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Cria CSV contendo informacoes de propriedade de codigo a partir do Little House
	 * Salva caminho da classe, autor e quantidade de modificacoes realizadas pelo autor
	 */
	public void createOwnershipLittleHouseCsvFile(String nameFile, Map<String, ComponentOwnershipLittleHouse> mapOwnershipLittleHouse) {
		try {

			// Criação de um buffer para a escrita em uma stream
			BufferedWriter bufferwriter = new BufferedWriter(new FileWriter("/home/talita/AutoriaCSV/" + nameFile + ".csv"));
			
			bufferwriter.write("Nome; Componente LH; Proprietario; Valor métrica \n");
			for (Entry<String, ComponentOwnershipLittleHouse> key : mapOwnershipLittleHouse.entrySet()) {
				
				String authors = null;
				 boolean first = true;
				 for (String s : key.getValue().getAuthors())
				 {
					if(first){
						authors = s;
						first = false;
					} 
					else{
						authors = authors +" &"+s;
						System.out.println(authors);
					}
				 }
				 
				if(key.getValue().getComponentLittleHouse().equals("")){
					continue;			
				}
				else{
					bufferwriter.write(""+key.getKey()+";"+key.getValue().getComponentLittleHouse()+";"
						+authors +";"+ key.getValue().getOwnership()+"\n");
				}
			}
			
			bufferwriter.close();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
