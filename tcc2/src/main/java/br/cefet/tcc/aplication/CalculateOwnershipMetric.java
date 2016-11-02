package br.cefet.tcc.aplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
 * Classe que calcula as metricas de propriedade de codigo para arquivos Java,
 * segundo modelo proposto por Bird e replicado por Foucault
 * @author: Talita Santana Orfano
 * @date: set/2016
 */
public class CalculateOwnershipMetric {
	private ArrayList<OwnershipMetric> ownershipMetricsList;

	public CalculateOwnershipMetric() {
		this.ownershipMetricsList = new ArrayList<OwnershipMetric>();
	}

	public CalculateOwnershipMetric(ArrayList<OwnershipMetric> ownershipMetricsList) {
		super();
		this.ownershipMetricsList = ownershipMetricsList;
	}

	public static String AdaptPathOwnership(String path) {
		String file = "";
		// Limpa o caminho para deixar no mesmo todos arquivos
		String[] nameFile = path.split("org/");

		if (nameFile.length == 1) { // exclusivo para o glide

			/*
			 * String[] File = nameFile[0].split("javax/"); if (File.length ==
			 * 1) { file = nameFile[0]; } else { file = "javax/" + File[1]; }
			 */

			file = nameFile[0];
		} else {
			file = "org/" + nameFile[1];
		}
		return file;
	}

	public static String transformNameEquivalent(String name) {
		//String nameBasic = "Markus Krötzsch";
		//String nameBasicNew = "Markus Kroetzsch";
		//String nameBasic = "Csaba Kozák";
		//String nameBasicNew = "Csaba Kozak";
		String nameBasic = "limpbizkit@gmail.com";
		String nameBasicNew ="limpbizkit";
		
		if (name.equals(nameBasic)) {
			return nameBasicNew;
		}

		return null;
	}

	public static HashMap<String, Integer> transformHashMapNameEquivalent(HashMap<String, Integer> autores) {
		boolean entrou = false;
		HashMap<String, Integer> autores2;
		autores2 = (HashMap<String, Integer>) autores.clone();
		
		for (Entry<String, Integer> key2 : autores.entrySet()) {
			String newName = transformNameEquivalent(key2.getKey());
			if (newName != null) {
				if(autores2.containsKey(newName)){
					int total = key2.getValue() + autores2.get(newName);
					autores2.put(newName, total);
				}
				else{
					autores2.put(newName, key2.getValue());
				}
				entrou = true;
			}
		}		
		
		if(entrou){
			//autores2.remove("Markus Krötzsch"); Wikidata
			//autores2.remove("Csaba Kozák");	  Android annotations
			autores2.remove("limpbizkit@gmail.com");
		}
		return autores2;
	}

	/*
	 * Retira duplicidade de arquivos
	 */
	public static Map<String, HashMap<String, Integer>> RemoveDuplicityFileAndAuthor(
			Map<String, HashMap<String, Integer>> hashAuthorship) {
		String file = "";
		Map<String, HashMap<String, Integer>> hashAuthorshipNotDouble = new HashMap<String, HashMap<String, Integer>>();

		for (Entry<String, HashMap<String, Integer>> key : hashAuthorship.entrySet()) {
			file = AdaptPathOwnership(key.getKey());
						
			if (hashAuthorshipNotDouble.containsKey(file)) {
				// Pega autores ja existentes
				HashMap<String, Integer> autores = hashAuthorshipNotDouble.get(file);

				// Faz o merge dos autores das duas hashmaps com mesmo arquivo
				// para evitar arquivos e autores duplicados
				for (Entry<String, Integer> key2 : key.getValue().entrySet()) {
					if (autores.containsKey(key2.getKey())) {
						int total = autores.get(key2.getKey());
						total = total + key2.getValue();
						autores.put(key2.getKey(), total);
					} else {
						autores.put(key2.getKey(), key2.getValue());
					}
				}
				hashAuthorshipNotDouble.put(file, autores);
			} else {
				hashAuthorshipNotDouble.put(file, key.getValue());
			}
		}
		return hashAuthorshipNotDouble;
	}

	/*
	 * Calcula metrica de propriedade de codigo para cada arquivo conforme
	 * determinado na literatura de Foucault
	 */
	public void Calculate(Map<String, HashMap<String, Integer>> hashAuthorshipDup) {

		OwnershipMetric ownership;
		double totalContribuitions = 0.0;
		double ownershipMetric = 0.0;
		double maiorOwnershipMetric = 0.0;
		ArrayList<OwnershipMetric> lista;

		// Retira arquivos duplicados e agrupa respectivas contribuicoes dos
		// desenvolvedores
		Map<String, HashMap<String, Integer>> hashAuthorship = RemoveDuplicityFileAndAuthor(hashAuthorshipDup);

		// Adapta nome - Softwares com autores de mesmo nome, duplicados: Wikidata, Guice e Android Annotations
		/*for(Entry<String, HashMap<String, Integer>> key : hashAuthorship.entrySet()) {
			HashMap<String, Integer> newAutores = transformHashMapNameEquivalent(hashAuthorship.get(key.getKey()));
			hashAuthorship.put(key.getKey(), newAutores);
			key.getKey();
		}*/
		
		for (Entry<String, HashMap<String, Integer>> key : hashAuthorship.entrySet()) {
			ownership = new OwnershipMetric();
			totalContribuitions = 0.0;

			// Total de contribuicoes de todos desenvolvedores neste modulo
			// (classe)
			for (Entry<String, Integer> key2 : key.getValue().entrySet()) {
				totalContribuitions += key2.getValue();
			}

			maiorOwnershipMetric = 0.0;
			ownershipMetric = 0.0;
			lista = new ArrayList<OwnershipMetric>();

			// Calcula a metrica de propriedade para cada autor deste modulo
			for (Entry<String, Integer> key3 : key.getValue().entrySet()) {
				ownershipMetric = key3.getValue() / totalContribuitions;
				ownership = new OwnershipMetric(key.getKey(), key3.getKey(), ownershipMetric);
				if (ownershipMetric > maiorOwnershipMetric) {
					maiorOwnershipMetric = ownershipMetric;
				}
				lista.add(ownership);
			}

			// Encontra o proprietario deste modulo
			for (OwnershipMetric owner : lista) {
				if (owner.getValueMetric() == maiorOwnershipMetric) {
					// Adiciona o proprietario do modulo na lista de
					// proprietarios
					ownershipMetricsList.add(owner);
				}
			}
		}
	}

	public ArrayList<OwnershipMetric> getOwnershipMetricsList() {
		return ownershipMetricsList;
	}

	public void setOwnershipMetricsList(ArrayList<OwnershipMetric> ownershipMetricsList) {
		this.ownershipMetricsList = ownershipMetricsList;
	}

}
