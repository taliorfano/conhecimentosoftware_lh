package br.cefet.tcc.aplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/*
 * Classe que realiza o Matching dos arquivos CVS: Little House e Ownership 
 * Alem de salvar este resultado em um arquivo CVS
 * 
 * @author: Talita Santana Orfano
 * @date: set/2016
 */
public class MatchingCSV {

	public static ArrayList<ComponentLittleHouse> componentsLittleHouse = new ArrayList<ComponentLittleHouse>();
	public static ArrayList<ComponentOwnership> componentsOwnership = new ArrayList<ComponentOwnership>();
	// Map que contem matching do LH com Ownership, contendo apenas os arquivos que possuem correspondentes
	public static HashMap<String, ComponentOwnershipLittleHouse> ownershipLittleHouseMapComplete = new HashMap<String, ComponentOwnershipLittleHouse>();
	//Map que contem matching do LH com Ownership, contendo os arquivos que possuem correspondentes e os que nao possuem componente LH
	public static HashMap<String, ComponentOwnershipLittleHouse> ownershipLittleHouseMap = new HashMap<String, ComponentOwnershipLittleHouse>();
	
	public static void main(String[] args) {
		// String pathOwnership =
		// "/home/talita/AutoriaCSV/ownership_wikidata.csv";
		// String pathLittleHouse = "/home/talita/AutoriaCSV/lh_wikidata.csv";
		
		String name = "androidannotations";
		String pathOwnership = "/home/talita/AutoriaCSV/ownership_"+name+".csv";
		String pathLittleHouse = "/home/talita/AutoriaCSV/lh_"+name+".csv";
		String nameFile = "LHOwnership_"+name;

		readCsvFile(pathLittleHouse, false);
		readCsvFile(pathOwnership, true);
		mergeDataOwnershipAndLittleHouse();

		// getListComponentLittleHouse();
		// getListComponentOwnership();

		readMapOwnershipLittleHouse();
		CVSOperations cvs = new CVSOperations();
		
		// Apenas dados completos, isto e, com o componente LH
		cvs.createOwnershipLittleHouseCsvFile(nameFile+"Complete", ownershipLittleHouseMapComplete);
		// Todos dados resultantes do matching
		cvs.createOwnershipLittleHouseCsvFileAll(nameFile, ownershipLittleHouseMap);
	}
	
	/*
	 * Retira a extensao .java e altera formato do caminho para ficar semelhante
	 * ao gerado pelo LttleHouse
	 */
	public static void AdaptPathOwnership(String[] TableLine) {
		String file = "";
		
		// Limpa o caminho para deixar no mesmo formato do arquivo LH
		String[] nameFile = TableLine[0].split("java/");
		//String[] nameFile = TableLine[0].split("/src/");
		
		  if(nameFile.length == 1){ // exclusivo para o glide
			  String[] File = TableLine[0].split("src/");
			  if(File.length == 1){
				  file = File[0].replace(".java", ""); 
			  }
			  else{
				  file = File[1].replace(".java", ""); 
			  }
		  }
		  else {
			  file = nameFile[1].replace(".java", "");
		  }
		 
		

		// ArrayList<String> authors = new ArrayList<String>();
		// authors.add(TableLine[1]);

		ComponentOwnership compLH = new ComponentOwnership(file, TableLine[1], Double.parseDouble(TableLine[2]));
		componentsOwnership.add(compLH);
	}

	/*
	 * Substitui caracteres . por / para ficar semelhante ao gerado pelos script
	 * de ownership
	 */
	public static void AdaptPathLittleHouse(String[] TableLine) {
		String path = TableLine[0].replace('.', '/');

		ComponentLittleHouse compLH = new ComponentLittleHouse(path, TableLine[1], TableLine[2]);
		componentsLittleHouse.add(compLH);
	}

	/*
	 * Lê arquivo CSV e trata de acordo com sua caracteristica
	 */
	public static void readCsvFile(String path, boolean ownership) {
		try {
			BufferedReader StrR = new BufferedReader(new FileReader(path));
			String Str;
			String[] TableLine;
			boolean primeiraLinha = true;

			while ((Str = StrR.readLine()) != null) {
				// Divide a linha lida em um array de String passando como
				// parametro o divisor ";".
				TableLine = Str.split(";");
				if (primeiraLinha) {
					primeiraLinha = false;
					continue;
				}

				if (ownership) {
					AdaptPathOwnership(TableLine);
				} else {
					AdaptPathLittleHouse(TableLine);
				}
				System.out.println("\n");
			}
			StrR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * Realiza o merge dos dados referentes ao Little House e a Ownership para
	 * cada classe analisada
	 */
	public static void mergeDataOwnershipAndLittleHouse() {
		ComponentOwnershipLittleHouse componentOwnershipLittleHouse;

		// Adiciona no Map os arquivos e componentes LH
		for (ComponentLittleHouse lh : componentsLittleHouse) {
			componentOwnershipLittleHouse = new ComponentOwnershipLittleHouse(lh.getPath(), lh.getComponent());
			ownershipLittleHouseMap.put(componentOwnershipLittleHouse.getPath(), componentOwnershipLittleHouse);
			ownershipLittleHouseMapComplete.put(componentOwnershipLittleHouse.getPath(), componentOwnershipLittleHouse);
		}

		// Adiciona no mesmo Map os autores e a metrica de ownership no arquivo correspondente
		int count = 0;
		for (ComponentOwnership componentsOwnership : componentsOwnership) {
			if (ownershipLittleHouseMap.containsKey(componentsOwnership.getPath())) {
				ComponentOwnershipLittleHouse cOwnLH = ownershipLittleHouseMap.get(componentsOwnership.getPath());

				if (cOwnLH.getOwnership() == 0) {
					cOwnLH.setOwnership(componentsOwnership.getOwnership());
				}

				ArrayList<String> authorsStored = cOwnLH.getAuthors();
				authorsStored.add(componentsOwnership.getAuthor());
				cOwnLH.setAuthors(authorsStored);
				ownershipLittleHouseMap.put(componentsOwnership.getPath(), cOwnLH);
				ownershipLittleHouseMapComplete.put(componentsOwnership.getPath(), cOwnLH);
			} else {
				count++;
				// Caso nao possua correspondencia no LH add no Map o arquivo, autor(es) e metrica
				ArrayList<String> authors = new ArrayList<String>();
				authors.add(componentsOwnership.getAuthor());
				componentOwnershipLittleHouse = new ComponentOwnershipLittleHouse(
						"" + componentsOwnership.getPath(), "", authors, componentsOwnership.getOwnership());
				ownershipLittleHouseMap.put(componentsOwnership.getPath(), componentOwnershipLittleHouse);
			}
			System.out.println("Totaaaal: " + count);
		}
		
		// Trata arquivo que contem mais de uma classe (Aluno$AlunoSuperior.java)
		treatFileMultiClass();		
	}
	
	/**
	 * Trata arquivo que contem mais de uma classe (Aluno$AlunoSuperior.java)
	 */
	public static void treatFileMultiClass(){
		String name = "";
		ArrayList<String> keysToRemove = new ArrayList<String>();
		for (Entry<String, ComponentOwnershipLittleHouse> key : ownershipLittleHouseMapComplete.entrySet()) {
			name = key.getKey();
			if(name.contains("$")){
				// trata casos de classes internas no arquivo
				String[] namePrincipal = name.split(Pattern.quote ("$"));
				ComponentOwnershipLittleHouse sameFile = ownershipLittleHouseMapComplete.get(namePrincipal[0]);
				if(sameFile != null){
					ComponentOwnershipLittleHouse fileUpdate = 
							new ComponentOwnershipLittleHouse(name, key.getValue().getComponentLittleHouse());
					fileUpdate.setAuthors(sameFile.getAuthors());
					fileUpdate.setOwnership(sameFile.getOwnership());
					ownershipLittleHouseMapComplete.put(name, fileUpdate);
				}
				else{
					keysToRemove.add(name);
					//ownershipLittleHouseMapComplete.remove(key.getKey());
				}
			}
		}
		for(String key : keysToRemove){
			ownershipLittleHouseMapComplete.remove(key);
		}
		name = "";	
	}

	/*
	 * Imprime lista de componentes de propriedade de código
	 */
	public static void getListComponentOwnership() {
		for (ComponentOwnership componentOwnership : componentsOwnership) {
			System.out.println(componentOwnership.getPath() + " " + componentOwnership.getAuthor().toString()
					+ componentOwnership.getOwnership());
		}
	}

	/*
	 * Imprime lista de componentes com informacoes do Little House
	 */
	public static void getListComponentLittleHouse() {
		for (ComponentLittleHouse componentLittleHouse : componentsLittleHouse) {
			System.out.println(componentLittleHouse.getPath() + " " + componentLittleHouse.getComponent());
		}
	}

	/*
	 * Lê e imprime hash contendo os dados de autoria e Little House
	 */
	public static void readMapOwnershipLittleHouse() {
		for (Entry<String, ComponentOwnershipLittleHouse> key : ownershipLittleHouseMap.entrySet()) {

			String authors = null;
			boolean first = true;
			for (String s : key.getValue().getAuthors()) {
				if (first) {
					authors = s;
					first = false;
				} else {
					authors.concat("&" + s);
				}
			}

			System.out.println(key.getKey() + " " + key.getValue().getComponentLittleHouse() + " " + authors + " "
					+ key.getValue().getOwnership());
		}
	}

}
